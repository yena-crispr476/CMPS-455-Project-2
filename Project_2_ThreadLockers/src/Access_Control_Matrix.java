public class Access_Control_Matrix implements Runnable{


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
*
 */