package util;

import java.util.Random;

import core.Build;

/**
 * The RNG class represents a Random Number Generator (RNG).
 */
public class RNG {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns a random number in the range [0, 1].
     * 
     * @return The random number.
     */
    public static float random() {
        return RNG.random(0, 1f);
    }

    /**
     * Returns a random number in the range [-magnitude, magnitude).
     * 
     * @param magnitude The maximum magnitude of the returned number.
     * 
     * @return The random number.
     */
    public static float random(float magnitude) {
        return RNG.random(-magnitude, magnitude);
    }

    /**
     * @see This function is an integer version of RNG#random(float).
     */
    public static int random(int magnitude) {
        return RNG.random(-magnitude, magnitude);
    }

    /**
     * Returns a random number in the range [low, high).
     * 
     * @param low  The lower bound of the random number range.
     * @param high The upper bound of the random number range.
     * 
     * @return The random number.
     */
    public static float random(float low, float high) {
        float range = high - low;
        return low + range*random.nextFloat();
    }

    /**
     * @see This function is an integer version of RNG#random(float, float).
     */
    public static int random(int low, int high) {
        int range = high - low;
        return low + random.nextInt(range + 1);
    }

    /**
     * Returns the seed that was used to initialize this random number generator.
     * 
     * @return The seed.
     */
    public static long getSeed() {
        return RNG.SEED;
    }


    // Private members
    // -------------------------------------------------------------------------    

    /**
     * The seed that is used to initialize the random number generator.
     */
    private static final long SEED = (long) (123456789*Math.random());

    /**
     * The Random instance that is seeded and used to perform the random number
     * calculations.
     */
    private static Random random = new Random(Build.getMajorVersion() ^ Build.getMinorVersion() ^ RNG.SEED);
}