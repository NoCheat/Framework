package at.nocheat.framework;

/**
 * Core registry functionality.
 * 
 * @author mc_dev
 * 
 */
public interface NCRegistry {
    
    /**
     * General services.
     * 
     * @param serviceClass
     * @return
     */
    public <T> T getService(Class<T> serviceClass);
    
    public void register(IRegistrationContext context);
    
}
