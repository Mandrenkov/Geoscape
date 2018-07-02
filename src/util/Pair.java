package util;

/**
 * The Pair class represents a pair of values of types F and S.
 */
public class Pair <F, S> {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Pair with the given values.
     * 
     * @param first  The first value in the Pair.
     * @param second The second value in the Pair.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first value of this Pair.
     *
     * @return The first value.
     */
    public F getFirst() {
        return this.first;
    }

    /**
     * Returns the second value of this Pair.
     *
     * @return The second value.
     */
    public S getSecond() {
        return this.second;
    }

    /**
     * Sets the values of this Pair.
     * 
     * @param first  The new value of |first|.
     * @param second The new value of |second|.
     */
    public void set(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Sets the first value of this Pair.
     *
     * @param first The new value of |first|.
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * Sets the second value of this Pair.
     *
     * @param second The new value of |second|.
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * Returns a String representation of this Pair.
     *
     * @return The String representation.
     */
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The first value of the Pair.
     */
    private F first;

    /**
     * The second value of the Pair.
     */
    private S second;
}