import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * The `CapabilityBasedAccessControl` class implements a capability-based access control system for files.
 * It manages a domain, a set of capabilities, and performs access control checks for read and write operations
 * on threads.
 */
// programmed by Aaron Delahoussaye
public class CapabilityBasedAccessControl implements Runnable {
    private Domain domain;
    private List<Capability> capabilities;
    private int threadID;
    private AtomicInteger requests = new AtomicInteger(0);
    private LockableFile[] files;

    /**
     * Constructor for initializing the CapabilityBasedAccessControl object with required parameters.
     *
     * @param domain The domain to which the capabilities belong.
     * @param capabilities A list of capabilities that define permissions.
     * @param threadID The ID of the thread being initialized.
     * @param files An array of files that can be accessed and locked by this object.
     * @throws IllegalArgumentException if any of the constructor arguments are null.
     */
    // Added by Aaron Delahoussaye: Constructor for initializing the object with required parameters.
    public CapabilityBasedAccessControl(Domain domain, List<Capability> capabilities, int threadID, LockableFile[] files) {
        if (domain == null || capabilities == null || files == null) {
            throw new IllegalArgumentException("Constructor arguments must not be null");
        }
        this.domain = domain;
        this.capabilities = capabilities;
        this.threadID = threadID;
        this.files = files;
    }

    /**
     * Executes the logic for a thread to perform random read/write operations on an array of files.
     * The method continuously selects a random file and attempts either a read or write operation
     * based on randomly generated criteria, respecting access control mechanisms.
     *
     * The thread selects operations randomly until a pre-defined number of requests is reached.
     * For each operation, it checks whether the operation is allowed using the provided capabilities.
     * If the operation is allowed, it acquires the appropriate lock, performs a simulated operation by sleeping,
     * and then releases the lock. If the operation is not allowed, it logs an access denied message.
     *
     * The method makes use of the following class fields:
     * - `requests`: An AtomicInteger tracking the number of requests made by the thread.
     * - `files`: An array of `LockableFile` objects on which the operations are performed.
     * - `threadID`: An identifier for the thread.
     *
     * The method relies on the `isOperationAllowed` method to determine if an operation can be performed
     * and uses the locking mechanism provided by the `LockableFile` class for handling concurrent read and write operations.
     */
    // Added by Aaron Delahoussaye: Method executed when the thread is run, performing random read/write operations.
    @Override
    public void run() {
        Random random = new Random();
        while (requests.get() < 5) {
            int objectID = random.nextInt(files.length);
            String operation = random.nextBoolean() ? "read" : "write";
            boolean allowed = isOperationAllowed(objectID, operation);
            if (allowed) {
                try {
                    if ("read".equals(operation)) {
                        files[objectID].acquireReadLock();
                        System.out.println("[Thread " + threadID + "] Reading file " + objectID);
                        Thread.sleep(1000); // Simulate read operation
                    } else {
                        files[objectID].acquireWriteLock();
                        System.out.println("[Thread " + threadID + "] Writing to file " + objectID);
                        Thread.sleep(1000); // Simulate write operation
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    files[objectID].releaseLock();
                }
            } else {
                System.out.println("[Thread " + threadID + "] Access denied for operation " + operation + " on object " + objectID);
            }
            requests.incrementAndGet();
        }
    }

    /**
     * Checks if the requested operation is allowed on the specified object.
     *
     * @param objectID The identifier of the object on which the operation is to be performed.
     * @param operation The type of operation requested (e.g., "read", "write").
     * @return true if the operation is allowed according to the capabilities; false otherwise.
     */
    // Added by Aaron Delahoussaye: Method checking if the requested operation is allowed.
    private boolean isOperationAllowed(int objectID, String operation) {
        for (Capability capability : capabilities) {
            if (capability.getObjectID() == objectID) {
                if ("read".equals(operation) && capability.getPermissions().contains("read")) {
                    return true;
                } else if ("write".equals(operation) && capability.getPermissions().contains("write")) {
                    return true;
                }
            }
        }
        return false;
    }
}