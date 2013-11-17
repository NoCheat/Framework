package at.nocheat.framework.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.nocheat.framework.event.MiniListenerNode;
import at.nocheat.framework.event.MiniListenerRegistry;

public class BukkitEventRegistry extends MiniListenerRegistry<Event, EventPriority> {
    
    /**
     * Node for events that implement the Cancellable interface (Bukkit).
     * 
     * @author mc_dev
     * 
     * @param <E>
     */
    protected static class BukkitCancellableNode<E> extends MiniListenerNode<E, EventPriority> {
        
        public BukkitCancellableNode(EventPriority basePriority) {
            super(basePriority);
        }
        
        // TODO: Future java: E extends Cancellable ?
        @Override
        protected boolean isCancelled(E event) {
            return ((Cancellable) event).isCancelled();
        }
    }
    
    public BukkitEventRegistry() {
        nodeFactory = new NodeFactory<Event, EventPriority>() {
            @Override
            public <E extends Event> MiniListenerNode<E, EventPriority> newNode(Class<E> eventClass, EventPriority basePriority) {
                if (Cancellable.class.isAssignableFrom(eventClass)) {
                    // TODO: Check if order is right (eventClass extends Cancellable).
                    // TODO: Future java (see above) ?
                    return new BukkitCancellableNode<E>(basePriority);
                } else {
                    return new MiniListenerNode<E, EventPriority>(basePriority);
                }
            }
        };
    }
    
    // TODO: Sanity Checks for Event class in use.
    
    // TODO: Overrider other register method (which listener to use, inaugurate listeners / double register).
    
    // TODO: Allow Listener (Bukkit) registration, auto wrap in mini-listeners.
    
    @Override
    protected <E extends Event> void registerNode(final Class<E> eventClass, final MiniListenerNode<E, EventPriority> node, final EventPriority basePriority) {
        // TODO: DETAILS.
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(ignoreCancelled = false, priority = basePriority)
            public void onEvent(E event) {
                node.onEvent(event);
            }
        }, Bukkit.getPluginManager().getPlugin("NoCheat"));
    }
    
}
