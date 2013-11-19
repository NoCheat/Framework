package at.nocheat.framework.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * One registry for MiniListener instances.<br>
 * Conventions:<br>
 * <ul>
 * <li>There should be only one event-registry per event-subsystem, unless a
 * registry is using another existing registry (...). Replacing
 * listener-registries might be solved by configuration, later.</li>
 * </ul>
 * Missing:<br>
 * <ul>
 * <li>Annotations for order and ignoreCancelled.</li>
 * <li>A class for order.</li>
 * <li>Use arrays within MiniListenerNode (copyOnWrite).</li>
 * <li>Allow unregister.</li>
 * </ul>
 * Uncertain:<br>
 * <ul>
 * <li>Name of this class.</li>
 * </ul>
 * 
 * @param <EB>
 *            Event base type, e.g. Event (Bukkit).
 * @param <P>
 *            Priority type of the underlying event system, e.g. EventPriority
 *            (Bukkit).
 * 
 * @author mc_dev
 * 
 */
public abstract class MiniListenerRegistry<EB, P> {
    
    protected static interface NodeFactory<EB, P> {
        public <E extends EB> MiniListenerNode<E, P> newNode(Class<E> eventClass, P basePriority);
    }
    
    ///////////////
    // Instance.
    ///////////////
    
    /**
     * Override for efficient stuff.
     */
    protected NodeFactory<EB, P> nodeFactory = new NodeFactory<EB, P>() {
        @Override
        public <E extends EB> MiniListenerNode<E, P> newNode(Class<E> eventClass, P basePriority) {
            return new MiniListenerNode<E, P>(basePriority);
        }
    };
    
    /**
     * Map event class -> base priority -> node. Note that this does no merging
     * based on super-classes like the Bukkit implementation of the Listener
     * registry would do.
     */
    protected final Map<Class<? extends EB>, Map<P, MiniListenerNode<? extends EB, P>>> classMap = new HashMap<Class<? extends EB>, Map<P, MiniListenerNode<? extends EB, P>>>();
    
    /**
     * Store attached MiniListener instances by anchor objects.
     */
    protected final Map<Object, Set<MiniListener<? extends EB>>> attachments = new HashMap<Object, Set<MiniListener<? extends EB>>>();
    
    public void attach(MiniListener<? extends EB>[] listeners, Object anchor) {
        attach(Arrays.asList(listeners), anchor);
    }
    
    public void attach(Collection<MiniListener<? extends EB>> listeners, Object anchor) {
        for (MiniListener<? extends EB> listener : listeners) {
            attach(listener, anchor);
        }
    }
    
    /**
     * "Attach" a listener to an object, such that the listener is removed if
     * removeAttachment is called.<br>
     * Note that removing a MiniListener will also remove the attachment.
     * 
     * @param listener
     * @param anchor
     */
    public <E extends EB> void attach(MiniListener<E> listener, Object anchor) {
        if (listener == null) {
            throw new NullPointerException("Must not be null: listener");
        } else if (anchor == null) {
            throw new NullPointerException("Must not be null: anchor");
        } else if (anchor.equals(listener)) {
            throw new IllegalArgumentException("Must not be equal: listener and anchor");
        }
        Set<MiniListener<? extends EB>> attached = attachments.get(anchor);
        if (attached == null) {
            attached = new HashSet<MiniListener<? extends EB>>();
            attachments.put(anchor, attached);
        }
        attached.add(listener);
    }
    
    /**
     * Convenience method, e.g. for use with Listener registration and plugins
     * to remove all attachments on plugin-disable.
     * 
     * @param registeredAnchor
     * @param otherAnchor
     */
    public void inheritAttached(Object registeredAnchor, Object otherAnchor) {
        // TODO: More signatures (Collection/Array).
        if (registeredAnchor == null) {
            throw new NullPointerException("Must not be null: registeredAnchor");
        } else if (otherAnchor == null) {
            throw new NullPointerException("Must not be null: newAnchor");
        }
        if (registeredAnchor.equals(otherAnchor)) {
            throw new IllegalArgumentException("Must not be equal: registeredAnchor and newAnchor");
        }
        Set<MiniListener<? extends EB>> attached = attachments.get(registeredAnchor);
        if (attached == null) {
            // TODO: throw something or return value or ignore?
        } else {
            Set<MiniListener<? extends EB>> attached2 = attachments.get(otherAnchor);
            if (attached2 == null) {
                attached2 = new HashSet<MiniListener<? extends EB>>();
                attachments.put(otherAnchor, attached2);
            }
            attached2.addAll(attached);
        }
    }
    
    /**
     * Unregister all attached MiniListener instances for a given anchor.
     * 
     * @param anchor
     */
    public void unregisterAttached(Object anchor) {
        // TODO: Consider more signatures for Collection + Array.
        Set<MiniListener<? extends EB>> attached = attachments.get(anchor);
        if (attached != null) {
            for (MiniListener<? extends EB> listener : new ArrayList<MiniListener<? extends EB>>(attached)) {
                unregister(listener);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <E extends EB> void unregister(MiniListener<E> listener) {
        // TODO: Consider allowing to pinpoint by priority?
        /*
         * Somewhat inefficient, as all attachments and all priority levels are checked,
         * this might/should be improved by adding extra mappings (consider check class by reflection).
         */
        // Remove listener registrations.
        for (Map<P, MiniListenerNode<? extends EB, P>> prioMap : classMap.values()) {
            for (MiniListenerNode<? extends EB, P> node : prioMap.values()) {
                try {
                    ((MiniListenerNode<E, P>) node).removeMiniListener(listener);
                } catch (ClassCastException e) {
                }
            }
        }
        // Remove attachment references.
        Iterator<Entry<Object, Set<MiniListener<? extends EB>>>> it = attachments.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Set<MiniListener<? extends EB>>> entry = it.next();
            Set<MiniListener<? extends EB>> attached = entry.getValue();
            attached.remove(listener); // TODO: can throw?
            if (attached.isEmpty()) {
                it.remove();
            }
        }
    }
    
    /**
     * Full signature registration method, given parameters override any
     * annotations and interfaces. <br>
     * This will update the internal classMap to contain a MiniListenerNode for
     * the given eventClass and basePriority. The miniListener will be added to
     * the node.
     * 
     * @param eventClass
     * @param listener
     * @param basePriority
     *            Priority for the underlying event system.
     * @param ignoreCancelled
     */
    public <E extends EB> void register(Class<E> eventClass, MiniListener<E> listener, P basePriority, boolean ignoreCancelled) {
        // TODO: Can/should the eventClass be read from listener parameters [means constraints on MiniListener?] ?
        Map<P, MiniListenerNode<? extends EB, P>> prioMap = classMap.get(eventClass);
        if (prioMap == null) {
            prioMap = new HashMap<P, MiniListenerNode<? extends EB, P>>();
            classMap.put(eventClass, prioMap);
        }
        // TODO: Concept for when to cast.
        @SuppressWarnings("unchecked")
        MiniListenerNode<E, P> node = (MiniListenerNode<E, P>) prioMap.get(basePriority);
        if (node == null) {
            node = nodeFactory.newNode(eventClass, basePriority);
            // TODO: Consider try-catch.
            registerNode(eventClass, node, basePriority);
            prioMap.put(basePriority, node);
        }
        node.addMiniListener(listener, ignoreCancelled);
    }
    
    /**
     * Register a MiniListenerNode instance with the underlying event-system
     * (unique nodes are ensured in register(...)). <br>
     * Note that the node is put to the internals map after this has been
     * called, to be able to recover from errors.
     * 
     * @param eventClass
     * @param node
     * @param basePriority
     */
    protected abstract <E extends EB> void registerNode(Class<E> eventClass, MiniListenerNode<E, P> node, P basePriority);
    
}
