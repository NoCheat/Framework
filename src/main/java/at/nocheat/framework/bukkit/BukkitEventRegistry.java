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
	
	// TODO: Sanity Checks for Event class in use.

	@Override
	protected <E extends Event> void registerNode(final Class<E> eventClass, final MiniListenerNode<E> node, final EventPriority basePriority) {
		// TODO: DETAILS.
		Bukkit.getPluginManager().registerEvents(new Listener(){
			@EventHandler(ignoreCancelled = false, priority = basePriority)
			public void onEvent(E event) {
				node.onEvent(event, !(event instanceof Cancellable) || (((Cancellable) event).isCancelled()));
			}
		}, Bukkit.getPluginManager().getPlugin("NoCheat"));
	}
	
}
