package at.nocheat.framework.event;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * One registry for MiniListener instances.
 * (Not sure this is the final name.)
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
    
    /*
     *  TODO:
     *  Adjust signatures, 
     *  Add concept for ordering (extra annotation class or given comparator !?, 
     *  might need generic node class specified here).
     *  Add a concept similar to the Bukkit Listener interface?
     */
    
    protected static interface NodeFactory<EB, P> {
        public <E extends EB> MiniListenerNode<E, P> newNode(Class<E> eventClass, P basePriority);
    }
    
    ///////////////
    // Instance.
    ///////////////
    
    /**
     * Map event class -> base priority -> node. Note that this does no merging
     * based on super-classes like the Bukkit implementation of the Listener
     * registry would do.
     */
    protected final Map<Class<? extends EB>, Map<P, MiniListenerNode<?, P>>> classMap = new HashMap<Class<? extends EB>, Map<P, MiniListenerNode<?, P>>>();
    
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
        // TODO: More simplified signatures, adding annotations and extra objects (order).
        Map<P, MiniListenerNode<?, P>> prioMap = classMap.get(eventClass);
        if (prioMap == null) {
            prioMap = new HashMap<P, MiniListenerNode<?, P>>();
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
