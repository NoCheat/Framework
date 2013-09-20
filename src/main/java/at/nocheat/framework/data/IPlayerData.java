package at.nocheat.framework.data;

/**
 * Centralized object for player-related data. Will hold sub-objects for individual checks data.
 * @author mc_dev
 *
 */
public interface IPlayerData {
	/**
	 * The id by which players are stored, could be UUID or player name or whatever.
	 * @return
	 */
	public String getId();
	
	/**
	 * Players do have names. [subject to removal :p]
	 * @return
	 */
	public String getName();
	
	
	
}
