import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LockableFile adds read-write locking functionality to the standard File class.
 * It leverages ReentrantReadWriteLock to provide fine-grained access control.
 */
public class LockableFile extends File {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LockableFile(String pathname) {
        super(pathname);
    }

    public void acquireReadLock() {
        lock.readLock().lock();
    }

    public void acquireWriteLock() {
        lock.writeLock().lock();
    }

    public void releaseLock() {
        if (lock.isWriteLockedByCurrentThread()) {
            lock.writeLock().unlock();
        } else {
            lock.readLock().unlock();
        }
    }
}