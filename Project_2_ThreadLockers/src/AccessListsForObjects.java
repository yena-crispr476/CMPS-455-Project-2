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
    public String[] writing = {"White", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Black"};

    public AccessListsForObjects(int N, int M, int IDN, int IDM, int threadID, String[] domain, String[][] permissions, String[] files){
        this.N = N;
        this.M = M;
        this.IDN = IDN;
        this.IDM = IDM;
        this.threadID = threadID;
        this.domain = domain;
        this.permissions = permissions;
        this.files = files;
    }

    @Override
    public void run() {
        Random random = new Random();
        int readOrWrite = 0;
        boolean isNull = false;
        boolean succeeded = false;
        while (requests < 5) {
            isNull = false;
            succeeded = false;
            int permission = random.nextInt(1, M + N + 1);
            while(domain[IDN].equals(permissions[permission][0])){
                permission = random.nextInt(1, M + N + 1);
            }
            if (permission <= M){
                readOrWrite = random.nextInt(1, 3);
                if (readOrWrite == 1){
                    System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Attempting to read resource: " + permissions[permission][0]);
                }
                if (readOrWrite == 2){
                    System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Attempting to write resource: " + permissions[permission][0]);
                }
            }
            else if (permission > M){
                System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Attempting to switch from " + domain[IDN] + " to " + permissions[permission][0]);
            }
            if (permissions[permission][IDN] == null){
                System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Operation failed, permission denied");
                isNull = true;
            }
            if (!isNull) {
                if (permission <= M){
                    //if (permissions[permission][IDN].contains("read write")) {
                    //    mutex.acquireUninterruptibly();
                    //    System.out.println(files[permission]); // Read
                    //    files[permission] = files[permission].concat(writing[random.nextInt(0, 8)]); // Write
                    //    mutex.release();
                    //}
                    if (readOrWrite == 1 && permissions[permission][IDN].contains("read")) {
                        mutex.acquireUninterruptibly();
                        Thread.yield(); // Read
                        mutex.release();
                        succeeded = true;
                    }
                    if (readOrWrite == 2 && permissions[permission][IDN].contains("write")) {
                        mutex.acquireUninterruptibly();
                        System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Writing '" + writing[random.nextInt(0, 8)] + "' to resource " + permissions[permission][0]);
                        files[permission] = files[permission].concat(writing[random.nextInt(0, 8)]); // Write
                        Thread.yield();
                        mutex.release();
                        succeeded = true;
                    }
                }
                else if (permission > M){
                    if (permissions[permission][IDN].contains("switch")) {
                        this.IDN = permission - (M);
                        succeeded = true;
                    }
                }
                if (!succeeded){
                    System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Operation failed, permission denied");
                }
                else {
                    int j = random.nextInt(3, 8);
                    System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] is yielding " + j + " times.");
                    for (int i = 1; i < j; i++){
                        Thread.yield();
                    }
                    System.out.println("[Thread: " + threadID + "(" + domain[IDN] + ")(Request:" + (requests + 1) + ")] Operation Complete.");
                }
            }
            requests++;
        }
    }
}
