import java.util.Random;
import java.util.concurrent.Semaphore;

public class AccessListsForObjects implements Runnable {
    Semaphore mutex = new Semaphore(1);
    public boolean available = true;
    public int N;
    public int M;
    public int IDN;
    public int IDM;
    public int threadID;
    public String[] domain;
    public String[][] permissions;
    public String[] files;
    public int requests = 0;
    public String[] writing = new String[]{"White", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Black"};

    public AccessListsForObjects(int N, int M, int IDN, int IDM, int threadID, String[] domain, String[][] permissions, String[] files) {
        this.N = N;
        this.M = M;
        this.IDN = IDN;
        this.IDM = IDM;
        this.threadID = threadID;
        this.domain = domain;
        this.permissions = permissions;
        this.files = files;
    }

    public void run() {
        Random random = new Random();
        int readOrWrite = 0;
        boolean isNull = false;

        for(boolean succeeded = false; this.requests < 5; ++this.requests) {
            isNull = false;
            succeeded = false;

            int permission;
            for(permission = random.nextInt(1, this.M + this.N + 1); this.domain[this.IDN].equals(this.permissions[permission][0]); permission = random.nextInt(1, this.M + this.N + 1)) {
            }

            if (permission <= this.M) {
                readOrWrite = random.nextInt(1, 3);
                if (readOrWrite == 1) {
                    System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Attempting to read resource: " + this.permissions[permission][0]);
                }

                if (readOrWrite == 2) {
                    System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Attempting to write resource: " + this.permissions[permission][0]);
                }
            } else if (permission > this.M) {
                System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Attempting to switch from " + this.domain[this.IDN] + " to " + this.permissions[permission][0]);
            }

            if (this.permissions[permission][this.IDN] == null) {
                System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Operation failed, permission denied");
                isNull = true;
            }

            if (!isNull) {
                if (permission <= this.M) {
                    if (readOrWrite == 1 && this.permissions[permission][this.IDN].contains("read")) {
                        this.mutex.acquireUninterruptibly();
                        Thread.yield();
                        this.mutex.release();
                        succeeded = true;
                    }

                    if (readOrWrite == 2 && this.permissions[permission][this.IDN].contains("write")) {
                        this.mutex.acquireUninterruptibly();
                        int var10001 = this.threadID;
                        System.out.println("[Thread: " + var10001 + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Writing '" + this.writing[random.nextInt(0, 8)] + "' to resource " + this.permissions[permission][0]);
                        this.files[permission] = this.files[permission].concat(this.writing[random.nextInt(0, 8)]);
                        Thread.yield();
                        this.mutex.release();
                        succeeded = true;
                    }
                } else if (permission > this.M && this.permissions[permission][this.IDN].contains("switch")) {
                    this.IDN = permission - this.M;
                    succeeded = true;
                }

                if (!succeeded) {
                    System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Operation failed, permission denied");
                } else {
                    int j = random.nextInt(3, 8);
                    System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] is yeilding " + j + " times.");

                    for(int i = 1; i < j; ++i) {
                        Thread.yield();
                    }

                    System.out.println("[Thread: " + this.threadID + "(" + this.domain[this.IDN] + ")(Request:" + (this.requests + 1) + ")] Operation Complete.");
                }
            }
        }

    }
}
