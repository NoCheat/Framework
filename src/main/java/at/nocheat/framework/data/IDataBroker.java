package at.nocheat.framework.data;

/**
 * Data access point.
 * 
 * TODO: Allowed data classes might be set by (I)RegistrationContext instances.
 * Use reference counting or add+removal policy (and who sets that)?
 * 
 * @author mc_dev
 * 
 */
public interface IDataBroker {
    /**
     * Maybe also by name, though id could be name, or UUID.
     * 
     * @param id
     * @return
     */
    public IPlayerData getPlayerData(String id);
    
    /**
     * Alright ;:p
     */
    public <D> void registerDataFactory(IDataFactory<D> factory);
    
}
