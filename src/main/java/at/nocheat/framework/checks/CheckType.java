package at.nocheat.framework.checks;

/**
 * Name is to be CamelCase. Permissions will be lower case constructed of name.
 * Full name is joined by dots.
 * 
 * Usage: <li>Store data with this as key.</li> <li>Model check hierarchies.</li>
 * <li>Quick access to permission.</li> <li>Used for commands and notifications
 * to identify a check.</li>
 * 
 * @author mc_dev
 * 
 */
public class CheckType {
    
    private static int maxId = 0;
    
    /** TODO: Put somewhere useful. */
    private static String checkPermissionRoot = "nocheat.checks";
    
    private static String joinPermission(String root, String sub) {
        return (root + "." + sub).toLowerCase();
    }
    
    private static String joinName(String root, String sub) {
        return root + "." + sub;
    }
    
    private String name;
    private String fullName;
    private String permission; // Should this be a permissions object? - sspx
    private CheckType parent;
    private final int hashCode;
    
    CheckType(String name, CheckType parent) {
        this.name = name;
        this.fullName = parent == null ? name : joinName(parent.getFullName(), this.name);
        this.permission = parent == null ? joinPermission(checkPermissionRoot, name) : joinPermission(parent.getPermission(), name);
        this.parent = parent;
        this.hashCode = ++maxId * 53471161; // Discuss :p.
    }
    
    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    /**
     * ?
     * 
     * @return
     */
    public String getPermission() {
        return permission;
    }
    
    /**
     * Parent check (for grouping).
     * 
     * @return
     */
    public CheckType getParent() {
        return parent;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CheckType) {
            // TODO: We want unique instances dealt out by registry ?
            return this == obj;
        } else {
            // TODO: Consider allowing Strings ?
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        // TODO: Figure out whether to calculate manually or automatically.
        return hashCode;
    }
    
    @Override
    public String toString() {
        // TODO: return name ?
        return super.toString();
    }
    
}
