package at.nocheat.framework.event;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * One registry for mini-listener instances.
 * (Not sure this is the final name.)
 * 
 * @param <EB> Event base type, e.g. Event (Bukkit).
 * @param <P> Priority type of the underlying event system, e.g. EventPriority (Bukkit).
 * 
 * @author mc_dev
 *
 */
public abstract class MiniListenerRegistry <EB, P>{
	
	// TODO: Adjust signatures, Add concept for ordering (extra annotation class or given comparator !?, might need generic node class specified here).
	
	protected static interface NodeFactory<P> {
		public <E>  MiniListenerNode<E> newNode(Class<E> eventClass, P basePriority);
	}
	
	///////////////
	// Instance.
	///////////////
	
	protected final Map<Class<?>, MiniListenerNode<?>> nodes = new HashMap<Class<?>, MiniListenerNode<?>>();
	
	/**
	 * Override for efficient stuff.
	 */
	protected NodeFactory<P> nodeFactory = new NodeFactory<P>() {
		@Override
		public <E> MiniListenerNode<E> newNode(Class<E> eventClass, P basePriority) {
			return new MiniListenerNode<E>();
		}
	};
	
	
	/**
	 * <br>Note: Policy is to direct map the event.
	 * @param eventClass
	 * @param listener
	 * @param basePriority Priority for the underlying event system.
	 */
	public <E extends EB> void register(Class<E> eventClass, MiniListener<E> listener, P basePriority) {
		// Check if exists.
		// Exists -> just add the listener.
		// Not Exists -> create node from factory, adds listener, register with registerNode with the underlying event system with given base priority.
	}
	
	/**
	 * Override this.
	 * @param eventClass
	 * @param node
	 * @param basePriority
	 */
	protected abstract <E extends EB> void registerNode(Class<E> eventClass, MiniListenerNode<E> node, P basePriority);
}

