package util;

/**
 * @author Mikhail Andrenkov
 * @since February 25, 2018
 * @version 1.1
 *
 * <p>The <b>Pair</b> class represents a pair of values of types T1 and T2.</p>
 */
public class Pair <T1, T2> {

    // Public members
	// -------------------------------------------------------------------------

    /**
	 * Constructs a Pointer object and initializes the buffer.
	 */
	public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Returns the first value of this Pair.
     * 
     * @return The first value.
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * Returns the second value of this Pair.
     * 
     * @return The second value.
     */
    public T2 getSecond() {
        return second;
    }

    /**
     * Sets the first value of this Pair.
     * 
     * @param first The new value of |first|.
     */
    public void setFirst(T1 first) {
        this.first = first;
    }

    /**
     * Sets the second value of this Pair.
     * 
     * @param second The new value of |second|.
     */
    public void setSecond(T2 second) {
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
    private T1 first;

    /**
     * The second value of the Pair.
     */
    private T2 second;
}