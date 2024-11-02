import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        printUsage();
        runTask1();
    }

    // Added by Aaron Delahoussaye: To print command usage
    private static void printUsage() {
        System.out.println("Usage: java Main -S <task_number>");
        System.out.println("Tasks:");
        System.out.println("  -S 1 : Task 1: Access Matrix");
        System.out.println("  -S 2 : Task 2: Access List for Objects");
        System.out.println("  -S 3 : Task 3: Capability List for Domains");
    }

    private static void runTask1() {
        System.out.println("Running: Task 1 -- Access Matrix");
        Access_Control_Matrix.main(new String[]{});
    }


    // Added by Aaron Delahoussaye: to access and run Task 3
    private static void runTask3() {
        Domain domain = new Domain(1);

        Set<String> permissions = new HashSet<>();
        permissions.add("read");
        permissions.add("write");

        List<Capability> capabilities = new ArrayList<>();
        capabilities.add(new Capability(0, permissions));
        capabilities.add(new Capability(1, permissions));

        LockableFile[] files = new LockableFile[] {
                new LockableFile("file0"),
                new LockableFile("file1")
        };

        int numberOfThreads = 5;
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new CapabilityBasedAccessControl(domain, capabilities, i, files));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

