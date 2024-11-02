import java.util.*;

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
        Random random = new Random();
        int N = random.nextInt(5) + 3; // Number of domains: 3 to 7
        System.out.println("The number of domains is " + N);
        int M = random.nextInt(5) + 3; // Number of objects: 3 to 7
        System.out.println("The number of objects is " + M);

        String[] domains = new String[N + 1];
        for (int i = 1; i <= N; i++) {
            domains[i] = "Domain" + i;
        }

        String[][] permissions = new String[M + N + 1][N + 1];
        for (int i = 1; i <= M; i++) {
            permissions[i][0] = "File" + i;
        }
        for (int i = M + 1; i <= M + N; i++) {
            permissions[i][0] = "Domain" + (i - M);
        }

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                int permOption = random.nextInt(4);
                if (permOption == 0) {
                    permissions[j][i] = null;
                } else if (permOption == 1) {
                    permissions[j][i] = "read";
                } else if (permOption == 2) {
                    permissions[j][i] = "write";
                } else {
                    permissions[j][i] = "read write";
                }
            }
            for (int j = M + 1; j <= M + N; j++) {
                if (j - M == i) {
                    permissions[j][i] = null;
                } else {
                    int switchOption = random.nextInt(2);
                    if (switchOption == 0) {
                        permissions[j][i] = null;
                    } else {
                        permissions[j][i] = "switch";
                    }
                }
            }
        }

        String[] files = new String[M + 1];
        for (int i = 1; i <= M; i++) {
            files[i] = "File" + i;
        }

        int numberOfThreads = random.nextInt(5) + 3; // Number of threads: 3 to 7
        System.out.println("The number of threads is " + numberOfThreads);

        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            int initialDomain = random.nextInt(N) + 1;
            int initialPermission = random.nextInt(M + N) + 1;
            threads[i] = new Thread(new AccessListsForObjects(N, M, initialDomain, initialPermission, i + 1, domains, permissions, files));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Access Lists:");
        for (int i = 1; i <= M + N; i++) {
            System.out.println("The Access List for " + permissions[i][0] + " is:");
            for (int j = 1; j <= N; j++) {
                System.out.println(permissions[i][j]);
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

