import java.util.Set;

/**
 * The Capability class represents the access permissions for a specific object.
 * Each capability is associated with an object identified by its ID and a set of permissions.
 */
public class Capability {
    private final int objectID;
    private final Set<String> permissions; // Consider using an EnumSet if you have a fixed set of permissions

    /**
     * Constructs a Capability object with the specified object ID and permissions.
     *
     * @param objectID the unique identifier for the object associated with this capability
     * @param permissions the set of permissions granted by this capability
     */
    public Capability(int objectID, Set<String> permissions) {
        this.objectID = objectID;
        this.permissions = permissions;
    }

    /**
     * Retrieves the unique identifier for the object associated with this capability.
     *
     * @return the object ID.
     */
    public int getObjectID() {
        return objectID;
    }

    /**
     * Retrieves the set of permissions associated with this capability.
     *
     * @return a set of strings representing the permissions granted by this capability.
     */
    public Set<String> getPermissions() {
        return permissions;
    }
}