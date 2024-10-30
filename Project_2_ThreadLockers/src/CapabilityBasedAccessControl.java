import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CapabilityBasedAccessControl implements Runnable {
    private Domain domain;
    private List<Capability> capabilities;
    private int threadID;
    private AtomicInteger requests = new AtomicInteger(0);
    private LockableFile[] files;

    public CapabilityBasedAccessControl(Domain domain, List<Capability> capabilities, int threadID, LockableFile[] files) {
        if (domain == null || capabilities == null || files == null) {
            throw new IllegalArgumentException("Constructor arguments must not be null");
        }
        this.domain = domain;
        this.capabilities = capabilities;
        this.threadID = threadID;
        this.files = files;
    }

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