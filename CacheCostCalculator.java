import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is a cache simulator -- it allows a user to select a cache architecture and
 * input a list of memory addresses.  The simulator will simulate accessing the memory
 * addresses given. The program will keep track of the hits and misses. Results will be 
 * output to the user.
 *
 * See Canvas for more details.
 *
 * @author Peter Jensen (starting code)
 * @author Daniel Kopta (updated starting code)
 * @author <your name here>
 * @version Fall 2023
 */
public class CacheCostCalculator
{
    // Students are welcome to add constants, fields, or helper methods to their class.  If you want additional
    // classes, feel free to put additional private classes at the end of this file.  (One .java file only.)

    /**
     * This helper method computes the ceiling of log base 2 of some number n.  (Any fractional
     * log is rounded up.)  For example:  logBase2(8) returns 3, logBase2(9) returns 4.
     *
     * This method is being provided to help students when they need to solve for x in 
     * 2^x = n.  
     *
     * @param n any positive integer
     * @return the ceiling of log_2(n)
     */
    public static int logBase2 (int n)
    {
        int x = 0;
        long twoToTheXth = 1;
        while (twoToTheXth < n)
        {
            x++;
            twoToTheXth *= 2;
        }
        return x;
    }

    /**
     * Application entry point.
     *
     * @param args unused
     */
    public static void main(String[] args)
    {
        // Working in main is not great.  Instead, let's just create an object
        // and use the run method below to do all the work.  This way, we can
        // create fields and helper methods without them having to be static.  :)
        // (The work really begins in 'run', below.)

        new CacheCostCalculator().run();
    }

    /**
     * Empty constructor -- feel free to add code if needed.
     */
    public CacheCostCalculator()
    {
        // Add code if needed
    }

    /**
     * Gathers input, runs the simulation, and produces output.
     */
    public void run ()
    {
        // Scan keyboard input from the console.

        Scanner userInput = new Scanner(System.in);

        // Determine which cache architecture is to be used.
        // Caution:  Do not change!!!  My autograder will expect these
        // exact prompts / responses.

        System.out.println ("Cache simulator CS 3810");
        System.out.println ("  (D)irect-mapped");
        System.out.println ("  (S)et associative");
        System.out.println ("  (F)ully associative");
        System.out.print ("Enter a letter to select a cache and press enter: ");

        String choice = userInput.next();    // Get the first 'word' typed by the user.
        choice = choice.toUpperCase();		 // Make it uppercase for consistency.

        boolean simulateDirectMapped     = choice.startsWith("D");
        boolean simulateSetAssociative   = choice.startsWith("S");
        boolean simulateFullyAssociative = choice.startsWith("F");

        // Each cache type has different customizations.  Get these inputs from the user.
        // Note:  All these variables are not needed.  You may rename them, but you
        // MUST NOT CHANGE THE ORDER OF INPUTS.  The autograder will give the inputs
        // in the order coded below.

        int blockDataBytes = 0;
        int sets = 0;
        int setWays = 0;

        // In all caches, we need to know how many data bytes are cached in each block.
        // Note that we are counting on this being a power of two.  (required)

        System.out.println();
        System.out.print("How many data bytes will be in each cache block? ");
        blockDataBytes = userInput.nextInt();  // Must be a power of two

        // Each cache will require different parameters...

        if (simulateDirectMapped || simulateSetAssociative)
        {
            setWays = 1;
            System.out.print("How many sets will there be? ");
            sets = userInput.nextInt();  // Must be a power of two
        }
        if (simulateSetAssociative)
        {
            System.out.print("How many 'ways' will there be for each set? ");
            setWays = userInput.nextInt();  // Any positive integer is OK
        }
        if (simulateFullyAssociative)
        {
            sets = 1;
            System.out.print("How many blocks will be in this fully associative cache? ");
            setWays = userInput.nextInt();  // Any positive integer is OK
        }

        // The last step is to gather the addresses.  We will allow an unlimited number of addresses.
        // Each address represents a memory request (a read from memory).

        List<Integer> addressList = new ArrayList<Integer>();  // Some students may prefer a list.
        int[]         addresses;                               // Some students may prefer an array.

        System.out.println("Enter a whitespace-separated list of addresses, type 'done' followed by enter at the end:");
        while (userInput.hasNextInt())
            addressList.add(userInput.nextInt());

        userInput.close();

        // The input was gathered in a list.  Make an array from it.  Students may use the array and/or the list
        // for their own purposes.

        addresses = new int[addressList.size()];
        for (int i = 0; i < addressList.size(); i++)
            addresses[i] = addressList.get(i);

        // Done gathering inputs.  Simulation code should be added below.

        // Step #1 - students should complete a few computations and update the output
        // statements below.  Do not change the text, only add integer answers to the output.  (No floating point results.)

        /* TODO - Compute the total storage size of the cache. Assume 32 bit addresses. */

        int mask;
        int secondMask; // for calculating blockOffset bits
        int setIndex;
        int blockOffset;
        int tag;
        // Found in lecture 21 at around 1:04:00

        // number of Block Offset bits (little b)
        int b = logBase2(blockDataBytes);
        // number of Set Index bits (s)
        int s = logBase2(sets);
        int t = 32-s-b;

        // assuming a Direct Mapped Cache to start

        int numDataBits = blockDataBytes*8;
        int numBlocksInCache = sets*setWays;
        int numLRUBits;
        // logBase2(setWays) must round up if there is a decimal in the answer
        if(logBase2(setWays) % 1 != 0){
            // round up by adding 1 and doing integer division to take off decimal
            numLRUBits = logBase2(setWays)+1/(int)1;
        }
        else{
            numLRUBits = logBase2(setWays);
        }
        int storageBitsPerBlock = 1+t+numDataBits+numLRUBits;
        // Report the various properties of the cache.  Reminder:  Do not change the messages
        // or printing order.  Replace the "fix_me" with a calculation or variable in each case.

        System.out.println();
        // DONE  vvvvvvvvvvvvvvvv
        System.out.println("Number of address bits used as offset bits:        " + b);
        if(simulateFullyAssociative){
            // Should be 0 for fully associative cache
            System.out.println("Number of address bits used as set index bits:     " + 0);
        }
        else{
            System.out.println("Number of address bits used as set index bits:     " + s);
        }
        System.out.println("Number of address bits used as tag bits:           " + t);
        // DONE ^^^^^^^^^^^^^^^^^^
        System.out.println();

        System.out.println("Number of valid bits needed in each cache block:   " + 1);
        System.out.println("Number of tag bits stored in each cache block:     " + t);
        // convert from bits to bytes
        System.out.println("Number of data bits stored in each cache block:    " + numDataBits);
        System.out.println("Number of LRU bits needed in each cache block:     " + numLRUBits);
        System.out.println("Total number of storage bits needed in each block: " + storageBitsPerBlock);
        System.out.println();

        System.out.println("Total number of blocks in the cache:               " + numBlocksInCache);
        System.out.println("Total number of storage bits needed for the cache: " + storageBitsPerBlock*numBlocksInCache);
        System.out.println();

        // Simulate the cache.  This step is entirely up to students.  Remember:
        //   Simulate memory requests using the addresses in the given order.  Do not sort
        //   or otherwise alter the order or number of the addresses.

        /* Your work here. */

        /*
        The set index is the first index into the array, and then you can
        look at each of the ways inside that set. For an W-way associative
         cache with S sets, the array would be SxW blocks. For a direct-mapped
          cache, there would only be one way per set, so the array would be Sx1.
         */

        // create a 2d array that will represent the cache
        // # sets will be the dimension of the first array dimension
        // # ways will be the second

        Block[][] cache = new Block[sets][setWays];

        // create cache structure with default values in every block

        for (int i = 0; i < sets; i++)
        {
            int LRUSetter = 0;
            for(int j = 0; j < setWays; j++)
            {
                cache[i][j] = new Block();
                // assign first "way" to 0 LRU, 2nd to 1, and so on. So there is a natural order to fill them
                cache[i][j].LRUCounter=LRUSetter;
                LRUSetter++;
            }
        }

        // create variables to hold the hits and misses of the cache simulation
        int hits = 0;
        int misses = 0;
        boolean didHit = false;


        // for every given address
        for (int i = 0; i < addresses.length; i++){

            //extract the values held in address at i

            // get blockIndex
            mask = ~(0xffffffff << b);
            blockOffset = (addresses[i]) & mask;

            // get setIndex
            mask = (0xffffffff >>> t);
            secondMask = (0xffffffff << b);
            setIndex = addresses[i] & mask;
            setIndex = setIndex & secondMask;
            setIndex = setIndex >>> b;

            // get tag
            mask = ~(0xffffffff >>> t);
            tag = addresses[i] & mask;
            tag = tag >>> (s+b);

            didHit = false;
            int hitSetIndex = 0;
            int hitWayIndex = 0;


            // check if it's a hit or miss (iterate through number of ways,
            // regardless if its direct or full associative cache)
            for(int way = 0; way < setWays; way++)
            {
                if(cache[setIndex][way].valid && cache[setIndex][way].tag == tag)
                {
                    didHit = true;
                    hitSetIndex = setIndex;
                    hitWayIndex = way;
                }
            }

            if(!didHit)
            {
                // it's a miss, pull in the new address (update tag value in cache)
                // default "way" to fill
                int toFill = 0;
                boolean foundToFill = false;
                // check if there is an empty spot
                for(int x = 0; x < setWays; x++){
                    if(cache[setIndex][x].valid == false){
                        toFill = x;
                        foundToFill = true;
                        break;
                    }
                }

                // if all spots are full decide which needs to be kicked
                if(!foundToFill)
                {
                    for(int x = 0; x < setWays; x++)
                    {
                        if(cache[setIndex][x].LRUCounter > toFill)
                        {
                            toFill = x;
                        }
                    }
                }

                // pull in new address, kick out least recently used section
                cache[setIndex][toFill].tag = tag;
                cache[setIndex][toFill].valid = true;
                cache[setIndex][toFill].LRUCounter = 0;

                // increment all other LRU counters
                for(int j = 0; j < cache[setIndex].length; j++){
                    if(j != toFill && !foundToFill){
                        cache[setIndex][j].LRUCounter++;
                    }
                }

                // increment misses
                misses++;
            }
            else
            {
                // update LRU
                cache[hitSetIndex][hitWayIndex].LRUCounter = 0;
                for(int g = 0; g < setWays; g++)
                {
                    if(g != hitWayIndex)
                    {
                        cache[hitSetIndex][g].LRUCounter++;
                    }
                }


                // increment hits
                hits++;
            }
            //System.out.println("Number: "+ addresses[i] +" Address: "+ Integer.toBinaryString(addresses[i]) + " "+didHit);
        }



        System.out.println("Accessing the addresses gives the following results:");
        System.out.println("Total number of hits:   " + hits);
        System.out.println("Total number of misses: " + misses);

        // Done -- end of run method.
    }
}

    // This is a great place for additional helper methods.  Add any you like.




// block class to represent each block in the cache
class Block
{
    public boolean valid;
    public int tag;
    public int LRUCounter;

    public Block(boolean _valid, int _tag, int _LRU)
    {
        valid = _valid;
        tag = _tag;
        LRUCounter = _LRU;
    }

    public Block()
    {
        valid = false;
        tag = 0;
        LRUCounter = 1;
    }

}
