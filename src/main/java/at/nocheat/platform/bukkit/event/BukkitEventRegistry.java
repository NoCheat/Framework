package at.nocheat.platform.bukkit.event;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import at.nocheat.framework.event.MiniListener;
import at.nocheat.framework.event.MiniListenerNode;
import at.nocheat.framework.event.MultiListenerRegistry;
import at.nocheat.platform.bukkit.NCStaticBukkit;

public class BukkitEventRegistry extends MultiListenerRegistry<Event, EventPriority> {
    
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
        // Auto register for plugin disable.
        // TODO: Add order to come pretty late.
        // TODO: Ensure the ignoreCancelled setting is correct (do listeners really not unregister if the event is cancelled).
        register(new MiniListener<PluginDisableEvent>() {
            @Override
            public void onEvent(PluginDisableEvent event) {
                unregisterAttached(event.getPlugin());
            }
        }, EventPriority.MONITOR, true);
    }
    
    @Override
    protected <E extends Event> void registerNode(final Class<E> eventClass, final MiniListenerNode<E, EventPriority> node, final EventPriority basePriority) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(ignoreCancelled = false, priority = basePriority)
            public void onEvent(E event) {
                node.onEvent(event);
            }
        }, NCStaticBukkit.getPlugin());
    }
    
    /**
     * Convenience method to have a listener unregister with disabling a plugin.
     * 
     * @param listener
     *            Do not call with a plugin class being the listener, use the
     *            other register method instead!
     * @param plugin
     */
    public void register(Listener listener, Plugin plugin) {
        attach(super.register((Object) listener, EventPriority.NORMAL, false), plugin);
    }
    
    public void register(Listener listener) {
        // Note: default ignoreCancelld and priority should have no effect, as EventHandler sets the defaults anyway.
        super.register((Object) listener, EventPriority.NORMAL, false);
    }
    
    @Override
    protected boolean shouldBeEventHandler(Method method) {
        return method.getAnnotation(EventHandler.class) != null;
    }
    
    @Override
    protected boolean getIgnoreCancelled(Method method, boolean defaultIgnoreCancelled) {
        EventHandler info = method.getAnnotation(EventHandler.class);
        if (info == null) {
            return defaultIgnoreCancelled;
        }
        else {
            return info.ignoreCancelled();
        }
    }
    
    @Override
    protected EventPriority getPriority(Method method, EventPriority defaultPriority) {
        EventHandler info = method.getAnnotation(EventHandler.class);
        if (info == null) {
            return defaultPriority;
        }
        else {
            return info.priority();
        }
    }
    
}
