package at.nocheat.framework.event;

import java.util.ArrayList;
import java.util.List;

public class MiniListenerNode<E, P> {
    
    /**
     * This is intended to be "complete" in terms of containing all information
     * for order, extra properties like ignoreCancelled.
     * 
     * @author mc_dev
     * 
     * @param <E>
     */
    protected static class ListenerEntry<E> {
        public final MiniListener<E> listener;
        public final boolean ignoreCancelled;
        
        public ListenerEntry(MiniListener<E> listener, boolean ignoreCancelled) {
            this.listener = listener;
            this.ignoreCancelled = ignoreCancelled;
        }
    }
    
    protected List<ListenerEntry<E>> listeners = new ArrayList<ListenerEntry<E>>();
    
    /**
     * Stored for the case of exceptions.
     */
    protected final P basePriority;
    
    public MiniListenerNode(P basePriority) {
        this.basePriority = basePriority;
    }
    
    /**
     * "Full" signature adding method.
     * 
     * @param listener
     */
    public void addMiniListener(MiniListener<E> listener, boolean ignoreCancelled) {
        // Add to internals.
        // TODO: Change signature to contain EVERYTHING (order, ...).
        addListenerEntry(new ListenerEntry<E>(listener, ignoreCancelled));
    }
    
    /**
     * Add a ListenerEntry to the internals (final stage, should be fully
     * implemented in the abstract node).
     * 
     * @param entry
     */
    public void addListenerEntry(ListenerEntry<E> entry) {
        // TODO: Sanity checks? Visibility?
        // TODO: Remove if it was previously registered !? [contested]
        // TODO: Order.
        listeners.add(entry);
    }
    
    /**
     * Override to implement events that can be cancelled.
     * 
     * @param event
     */
    protected boolean isCancelled(E event) {
        return false;
    }
    
    public void onEvent(final E event) {
        // Go through mini listeners....
        // Note that cancelled events get in here too (!).
        // TODO: Events could get cancelled any time during the loop !
        for (int i = 0; i < listeners.size(); i++) {
            final ListenerEntry<E> entry = listeners.get(i);
            if (!entry.ignoreCancelled || !isCancelled(event)) {
                // TODO: try - catch (+log)!
                entry.listener.onEvent(event);
            }
        }
    }
    
}
