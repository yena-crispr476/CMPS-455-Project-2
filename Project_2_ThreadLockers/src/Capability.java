import java.util.Set;

public class Capability {
    private final int objectID;
    private final Set<String> permissions; // Consider using an EnumSet if you have a fixed set of permissions

    public Capability(int objectID, Set<String> permissions) {
        this.objectID = objectID;
        this.permissions = permissions;
    }

    public int getObjectID() {
        return objectID;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}