import java.util.Random;
import java.util.concurrent.locks.Lock;


public class Access_Control_Matrix implements Runnable{
    // Could honestly have one random
    //
    static Random nValue = new Random();
    static Random mValue = new Random();
    static Random access_Value = new Random();
    static Random domain_Value = new Random();
    static Random X = new Random();
    static String [] [] access_Matrix;
    static Random random = new Random();
    static Lock accessLock;
    int thread_ID;
    int requestCount = 5;
    static int mBound;
    static int nBound;
    static int oDomains;

    public static void main (String args[]) {
        System.out.println("Hello World!");
        nBound = nValue.nextInt(3, 8);
        mBound = mValue.nextInt(3, 8);
        oDomains = nBound - 1;                          // Other Domains

        access_Matrix = new String [nBound] [mBound + oDomains];


        // Creating the array with the file acces and domain access established.
        for (int i = 0; i < nBound; i++) {
            // Fillin the second section array for the values
            for (int j = 0; j < (mBound + oDomains); j++) {
                if (j < mBound) {
                    access_Matrix[i][j] = file_Access();
                }
                else {
                    if (j == i) {
                        System.out.println("N/A");
                    }
                    else {
                        access_Matrix[i][j] = domain_Access();
                    }
                }
            }
        }
        System.out.println(nBound + " Domains \n" + mBound + " Files\n" );
        System.out.printf("%-15s", "Domain/File");
        for (int i = 0; i< mBound; i++) {
            System.out.printf("%-8s", "F" + i);
        }

        for (int i = 0; i < oDomains; i++) {
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
            Access_Control_Matrix ta = new Access_Control_Matrix(i, mBound, nBound, oDomains);
            Thread acT = new Thread (ta);

        }
    }

//------------------------- [Start Filling matrix functions] --------------------//
    static String file_Access() {
        int value = access_Value.nextInt(0,4);
        if (value == 0) {
            return (" ");
        }
        else if (value == 1) {
            return "R";
        }
        else if (value == 2) {
            return "W";
        }
        else {
            return "R/W";
        }
    }

    static String domain_Access () {
        int value = domain_Value.nextInt(0,2);
        if (value == 0) {
            return " ";
        }
        if (value == 1) {
            return "allow";
        }
        return "";
    }
// ---------------------- [End of Fill Matrix function] ---------------- //

    public Access_Control_Matrix (int tID, int mFiles, int nDomains, int otherDomains) {
        this.thread_ID = tID;
        this.mBound = mFiles;
        this.nBound = nDomains;
        this.oDomains = otherDomains;
    }

    public void threadFileAction (int x) {
        int rORw = random.nextInt(0,2); // 0 - Read, 1 - Write
        int randYield = random.nextInt(3, 8);

        if (access_Matrix[thread_ID][x] == "R" && rORw == 0) {
            accessLock.lock();
            for (int i = 0; i < randYield; i++) {
                Thread.yield();
            }
            accessLock.unlock();
            requestCount--;
        }
        else {
            
        }


    }

    public void threadDomainAction () {

    }

    public void threadRequestManager () {
        int x = X.nextInt(0, oDomains);
        while (requestCount != 0) {
            if (x < mBound) {
                threadFileAction(x);
            }
            if (x > mBound && x <= oDomains) {
                threadDomainAction();
            }
        }
    }






    @Override
     public void run() {



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
 */