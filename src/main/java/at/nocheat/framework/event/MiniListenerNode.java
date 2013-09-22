package at.nocheat.framework.event;

import java.util.ArrayList;
import java.util.List;

public class MiniListenerNode<E>{
	
	protected static class ListenerEntry<E> {
		public final MiniListener<E> listener;
		public final boolean ignoreCancelled;
		public ListenerEntry(MiniListener<E> listener, boolean ignoreCancelled) {
			this.listener = listener;
			this.ignoreCancelled = ignoreCancelled;
		}
	}
	
	protected List<ListenerEntry<E>> listeners = new ArrayList<ListenerEntry<E>>();
	
	public void addMiniListener(MiniListener<E> listener) {
		// Add to internals.
		// TODO: Order, entries with cancelled property (adjust signature!).
		// Sort list (maybe use an array though generics get a little bit lost , so uncertain how much gain)
	}
	
	public void onEvent(final E event, final boolean isCancelled) {
		// Go through mini listeners....
		// Note that cancelled events get in here too (!).
		for (int i = 0; i < listeners.size(); i++) {
			final ListenerEntry<E> entry = listeners.get(i);
			if (!isCancelled || !entry.ignoreCancelled) {
				// TODO: try - catch (+log)!
				entry.listener.onEvent(event);
			}
		}
	}
	
}
