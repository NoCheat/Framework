package at.nocheat.framework;

/**
 * This is how to register something with NC. <br>
 * The instances declare dependencies and provide methods for actually adding it
 * (factory) and for removing it. Having a factory-like setup allows for adding
 * things delayed, ruling out unnecessary registrations, but also allow to
 * register directly, if needed.<br>
 * TODO: possible is also to specify other contexts to override (by class or by
 * class-name or some identifier or similar). <br>
 * Simple abstract classes for convenient registration of checks and other can
 * be provided.
 * 
 * 
 * 
 * 
 * @author mc_dev
 * 
 */
public interface IRegistrationContext {

}
