package at.nocheat.framework.time;

/**
 * Some kind of clock.
 * @author mc_dev
 *
 */
public interface Clock {
	
	/**
	 * Get the clock counter. There is no guarantee that this is monotonic, nor need it be thread-safe.
	 * @return
	 */
	public long clock();
	
}
