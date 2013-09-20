package at.nocheat.framework.data;

/**
 * Data access point.
 * @author mc_dev
 *
 */
public interface IDataBroker {
	/**
	 * Maybe also by name, though id could be name, or UUID.
	 * @param id
	 * @return
	 */
	public IPlayerData getPlayerData(String id);
	
	/**
	 * 
	 */
	public <D> void registerDataFactory(IDataFactory<D> factory);
	
}
