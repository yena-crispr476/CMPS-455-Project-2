import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-S")) {
            printUsage();
            return; // Exit if the command line arguments are invalid
        }

        String taskNumber = args[1];
        switch (taskNumber) {
            case "1":
                runTask1();
                break;
            case "2":
                runTask2();
                break;
            case "3":
                runTask3();
                break;
            default:
                System.out.println("Error: Invalid task number. Please provide a valid task number (1, 2, or 3).");
                printUsage();
                break;
        }
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

    private static void runTask2() {
        int N = 3; // Number of domains
        int M = 4; // Number of files

        // Example domains
        String[] domains = {"Domain1", "Domain2", "Domain3"};

        // Example permissions array [file/domain, domain1 permissions, domain2 permissions, ...]
        String[][] permissions = {
                {"File1", "read", null, "write"},
                {"File2", "write", "read write", null},
                {"File3", "read write", null, "read"},
                {"File4", null, "read write", "write"},
                {"Domain2", "switch", null, null},
                {"Domain3", null, "switch", null},
                {"Domain1", null, null, "switch"}
        };

        // Example files
        String[] files = new String[M];
        for (int i = 0; i < M; i++) {
            files[i] = "File" + (i + 1);
        }

        int numberOfThreads = 3;
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            int initialDomain = i % N; // Distribute initial domains among threads
            int initialPermission = initialDomain % M; // Distribute initial permissions among threads
            threads[i] = new Thread(new AccessListsForObjects(N, M, initialDomain, initialPermission, i, domains, permissions, files));
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

