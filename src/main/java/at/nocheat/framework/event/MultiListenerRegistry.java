package at.nocheat.framework.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Support for registering multiple event-handler methods at once.<br>
 * 
 * For more details and conventions see:
 * {@link at.nocheat.framework.event.MiniListenerRegistry}<br>
 * 
 * Uncertain:<br>
 * <ul>
 * <li>Name of this class.</li>
 * <li>Store multi-listeners by arguments (ignoreCancelled, priority, ...) as
 * well and map to the wrappers vs merge/prevent-register.</li>
 * </ul>
 * 
 * @author mc_dev
 * 
 * @param <EB>
 *            Event base class, e.g. Event for Bukkit.
 * @param <P>
 *            Priority class, e.g. EventPriority for Bukkit.
 */
public abstract class MultiListenerRegistry<EB, P> extends MiniListenerRegistry<EB, P> {
    
    @SuppressWarnings("unchecked")
    protected <E extends EB> MiniListener<E> register(Method method, P basePriority, boolean ignoreCancelled) {
        MiniListener<E> listener = getMiniListener(method);
        if (listener == null) {
            return null;
        }
        register((Class<E>) method.getParameterTypes()[0], listener, basePriority, ignoreCancelled);
        return listener;
    }
    
    /**
     * Auxiliary method to get a MiniListener instance for a given method.
     * 
     * @param method
     * @return
     */
    protected <E extends EB> MiniListener<E> getMiniListener(final Method method) {
        if (!method.getReturnType().equals(void.class)) {
            return null;
        }
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length != 1) {
            return null;
        }
        try {
            @SuppressWarnings({ "unchecked", "unused" })
            Class<E> eventClass = (Class<E>) parameters[0];
        } catch (Throwable t) {
            return null;
        }
        MiniListener<E> listener = new MiniListener<E>() {
            @Override
            public void onEvent(E event) {
                try {
                    method.invoke(event);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return listener;
    }
    
    /**
     * Provided for convenience.
     * 
     * @param listener
     *            All internally created MiniListener instances will be attached
     *            to the listener by default.
     * @return Collection of MiniListener instances for attaching.
     */
    protected Collection<MiniListener<? extends EB>> register(Object listener, P defaultPriority, boolean defaultIgnoreCancelled) {
        Collection<MiniListener<? extends EB>> listeners = new ArrayList<MiniListener<? extends EB>>();
        for (Method method : listener.getClass().getMethods()) {
            if (shouldBeEventHandler(method)) {
                // TODO: exceptions ?
                MiniListener<? extends EB> miniListener = register(method, getPriority(method, defaultPriority), getIgnoreCancelled(method, defaultIgnoreCancelled));
                if (miniListener == null) {
                    // TODO: Inconsistency, raise an exception right here?
                } else {
                    listeners.add(miniListener);
                }
            }
        }
        if (!listeners.isEmpty()) {
            attach(listeners, listener);
        }
        return listeners;
    }
    
    /**
     * Solely meant for check for annotations (not return type etc., use
     * getMiniListener for that).
     * 
     * @param method
     * @return
     */
    protected abstract boolean shouldBeEventHandler(Method method);
    
    /**
     * This is meant to process platform specific annotations.
     * 
     * @param method
     * @return
     */
    protected abstract boolean getIgnoreCancelled(Method method, boolean defaultIgnoreCancelled);
    
    /**
     * This is meant to process platform specific annotations.
     * 
     * @param method
     * @return
     */
    protected abstract P getPriority(Method method, P defaultPriority);
    
}
