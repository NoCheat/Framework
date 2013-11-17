package at.nocheat.framework.exceptions;

/**
 * It is not allowed to override a component.
 * 
 * @author mc_dev
 * 
 */
public class IllegalOverrideException extends Exception {
    
    public IllegalOverrideException(String message) {
        super(message);
    }
    
    /**
	 * 
	 */
    private static final long serialVersionUID = -5775890567273535995L;
    
}
