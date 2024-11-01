import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;


public class Access_Control_Matrix implements Runnable{
    // Could honestly have one random
    //
    static String [] [] access_Matrix;
    static String [] fileContents = {"Red", "Yellow", "Blue", "Indigo", "Violet", "Rap", "Rock", "Punk", "and ohh... what's this? JAZZ"};
    static Semaphore[] fileSem;
    static Random random = new Random();
    int thread_ID;
    int requestCount = 5;
    static int mBound;
    static int nBound;
    static int oDomains;
    int rValue;
    int currentDomain;

    public static void main (String args[]) {
        System.out.println("Hello World!");
        nBound = random.nextInt(3, 8);
        mBound = random.nextInt(3, 8);
        oDomains = nBound - 1;                          // Other Domains

        access_Matrix = new String [nBound] [mBound + nBound];

        fileSem = new Semaphore[mBound];
        for (int i = 0; i < mBound; i++) {
            fileSem[i] = new Semaphore(1);
        }
        // Creating the array with the file acces and domain access established.
        for (int i = 0; i < nBound; i++) {
            // Fillin the second section array for the values
            for (int j = 0; j < (mBound + nBound); j++) {
                if (j < mBound) {
                    access_Matrix[i][j] = file_Access(i,j);
                }

                else if (j - mBound == i) {
                    access_Matrix[i][j] = "N/A";
                }
                else {
                    access_Matrix[i][j] = domain_Access(i,j);

                }
            }
        }
        System.out.println(nBound + " Domains \n" + mBound + " Files\n" );
        System.out.printf("%-15s", "Domain/File");
        for (int i = 0; i< mBound; i++) {
            System.out.printf("%-8s", "F" + i);
        }

        for (int i = 0; i < nBound; i++) {
            System.out.printf("%-8s", "D" + i);
        }
        System.out.println();

        // The Print Section
        for (int i = 0; i < nBound; i++) {
            System.out.printf("%-15s", "D" + (i));
            for (int j = 0; j < mBound + oDomains; j++) {
                System.out.printf("%-8s", access_Matrix[i][j]);
            }
            System.out.println();
        }


        for (int i = 0; i < nBound; i++) {
            Access_Control_Matrix ta = new Access_Control_Matrix(i);
            Thread acT = new Thread (ta);
            acT.start();

        }
    }

//------------------------- [Start Filling matrix functions] --------------------//
    static String file_Access(int i, int j) {
        int value = random.nextInt(0,4);
        int randContent = random.nextInt((fileContents.length) + 1);
        if (value == 0) {
            return (" ");
        }
        else if (value == 1) {
           // access_Matrix[i][j] = fileContents[randContent];
            return "R";
        }
        else if (value == 2) {
            return "W";
        }
        else {
//            access_Matrix[i][j] = fileContents[randContent];
            return "R/W";
        }
    }

    static String domain_Access (int i, int j) {
        int value = random.nextInt(0,2);
        if (value == 0) {
            return " ";
        }
        if (value == 1) {
            return "allow";
        }
        return "";
    }
// ---------------------- [End of Fill Matrix function] ---------------- //

    public Access_Control_Matrix (int tID) {
        this.thread_ID = tID;
        this.currentDomain = tID;
    }

    public void threadFileAction (int fileIndex) {
        int rORw = random.nextInt(2); // 0 - Read, 1 - Write
        int randYield = random.nextInt(3, 8);
        String permission = access_Matrix[currentDomain][fileIndex];

        if ("R".equals(permission) && rORw == 0 || "R/W".equals(permission) && rORw == 0) {
            System.out.println("[Thread " + thread_ID + "(D" + thread_ID + " )] Attempting to read F" + fileIndex+ ":  Permission Granted. " + "Contents Read: ");
            fileSem[fileIndex].acquireUninterruptibly();
            for (int i = 0; i < randYield; i++) {
                Thread.yield();
            }
            fileSem[fileIndex].release();
            }
        else if ("W".equals(permission) && rORw == 1 || "R/W".equals(permission) && rORw == 1) {
            System.out.println("[Thread " + thread_ID + "(D" + thread_ID + " )] Attempting to write F" + fileIndex + ":  Permission Granted......" );
            fileSem[fileIndex].acquireUninterruptibly();
            for (int i = 0; i < randYield; i++) {
                Thread.yield();
            }
            fileSem[fileIndex].release();

            } else {
                System.out.println("[Thread " + thread_ID + "(D" + thread_ID + " )] Attempting to access F" + fileIndex + ":  Permission Denied....." );
            }

    }

    public void threadDomainAction (int targetDomain) {
        String permission = access_Matrix[currentDomain][mBound + targetDomain];
        if ("allow".equals(permission) && currentDomain != targetDomain){
            System.out.println ("[Thread " + thread_ID + "(D" + currentDomain + " )] Attempting to switch from D"+ currentDomain + " to D" + targetDomain + ": Access Granted"); // Complete this part

            int randYield = random.nextInt(0, 8);
            for (int i = 0; i < randYield; i++) {
                Thread.yield();
            }
            currentDomain = targetDomain;
        }
        else if ("N/A".equals(permission) && currentDomain == targetDomain) {
            System.out.println("[Thread " + thread_ID + "(D" + currentDomain + " ) Attempting to switch into its own domain: Access Denied");
        }
        else {
            System.out.println("[Thread " + thread_ID + "(D" + currentDomain +" )] Attempting to switch to D" + targetDomain + " : Access Denied");
        }
    }

    public void threadRequestManager () {
        while (requestCount > 0) {
            int target = random.nextInt(mBound + oDomains);
            if (target < mBound) {
                threadFileAction(target);
            }
            else {
                threadDomainAction(target - mBound);
            }
            requestCount--;
        }
    }

    @Override
     public void run() {
        threadRequestManager();
    }


}
/*
*Part One --
*
* Goal: Implement an accesscontrol matrix to provide protection to file objects and guard against
* unauthorized domain switches
*
* IMplement matrix of size N x (M+N)
*       N: Domains
*       M: file objects
*       N - 1: Other Domains
*
*       Domains can read/write from files --> Domains allowed to switch or be prohibited from domains
*               Domains can't switch into themselves
*       Generate random values from N and M, [3 to 7] at the start of each run
*       Each Domain gets one thread. Each thread represents users in an OS.
*
*       Each file element can be represented by a string
*
*       Populate the access matrix randomly.
*
*       R - Read, W - Write, R/W - Read/Write, "allow" - Domain Allow, "N/A" Can't switch into self
*
*       Each request should be followed by a yeild amount from 3-7.
*
*       The user may also wish to switch domains. Arbitrator function handles if it's possible.
*       If permission is avaialbel switch, else deny access.
*
*       Print outputs for:
*           the contents of the matrix, each request to access a file object, Each request to a domain switch,
*           each time a request is accessed/denied, each time a thread starts yielding.
*
*       In each run generate five request for each thread.
*
*       For each request:
*           - Generate random number X [0, M + N] to correspond to the access matrix. X no bigger than M + N + !
*           - if X < M generate random int [0,1]
*               -Sem and locks must be used properly to protect file access. Use synchronization to control reader writer access
*               - After a thread gets access to a file object, it yields 3 - 7 times.
*
*           - if X>= M, then attempt to switch X-M. A thread shouldn't try to switch to a domain it is in.
*               You should generta a new value for X and try again .
*
*
*           Michael Vedol, C00459436
*
*
*           Report |     Task 1
*           1. Is there any chance of deadlock in this simulation? What changes could cause deadlocks.
*
 */


//
//