package at.nocheat.framework.checks;

import java.util.HashMap;
import java.util.Map;

import at.nocheat.framework.exceptions.IllegalOverrideException;
import at.nocheat.framework.exceptions.NotRegisteredException;

/**
 * (Consider a class with static CheckType instances for default check types!?)
 * 
 * Probably used by an actual registry internally.
 * @author mc_dev
 *
 */
public class CheckTypeRegistry {
	
	/**
	 * All registered CheckType instances by lower case full name.
	 */
	private final Map<String, CheckType> checkTypesLowerCase = new HashMap<String, CheckType>();
	
	/**
	 * All registered CheckType instances by exact case full name.
	 */
	private final Map<String, CheckType> checkTypes = new HashMap<String, CheckType>();
	
	/**
	 * TODO: Should this actually allow to return a present CheckType if already registered ?
	 * @param name
	 * @param parent
	 * @return
	 * @throws NotRegisteredException
	 * @throws IllegalOverrideException
	 */
	public CheckType registerCheckType(String name, CheckType parent) throws NotRegisteredException, IllegalOverrideException {
		name = name.trim(); // Check for empty !?
		CheckType newType = new CheckType(name, parent); // New type for name comparison (increases hashs though).
		String key = newType.getFullName().toLowerCase();
		CheckType presentType = checkTypes.get(key);
		if (presentType == null) {
			if (parent != null && parent != checkTypes.get(parent.getFullName().toLowerCase())) {
				throw new NotRegisteredException("Parent CheckType not registered: " + parent.getFullName());
			}
			checkTypesLowerCase.put(key, newType);
			checkTypes.put(newType.getFullName(), newType);
			// TODO: Register permissions (i.e. parent-child relations), psosibly other.
			return newType;
		}
		throw new IllegalOverrideException("Can not register CheckType " + newType.getFullName() + " , " + presentType.getFullName() + " is already registered.");
//		if (!presentType.getFullName().equals(newType.getFullName())) {
//			throw new IllegalOverrideException("Can not accept CheckType " + newType.getFullName() + " , because an instance with similar name already exists: " + presentType.getFullName());
//		}
//		if (parent != presentType.getParent()) {
//			throw new IllegalOverrideException("Can not accept CheckType " + newType.getFullName() + " , because parents mismatch.");
//		}
//		return presentType;
	}
	
	/**
	 * Case insensitive lookup by name.
	 * @param name
	 * @return
	 */
	public CheckType getCheckType(String name) {
		return checkTypesLowerCase.get(name.toLowerCase());
	}
	
	/**
	 * Lookup by exact name.
	 * @param name
	 * @return
	 */
	public CheckType getCheckTypeExact(String name) {
		return checkTypes.get(name);
	}
	
	/**
	 * Check if the CheckType instance is registered here.
	 * @param checkType
	 * @return
	 */
	public boolean isValid(CheckType checkType) {
		return checkType != null && checkType == checkTypes.get(checkType.getFullName());
	}
	
}