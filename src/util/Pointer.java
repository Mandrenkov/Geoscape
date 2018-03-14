package util;

import java.nio.IntBuffer;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Pointer</b> class is an implementation of an integer pointer.</p>
 */
public class Pointer {

    /**
     * Buffer to hold the integer value.
     */
    private IntBuffer buffer;

    /**
     * Constructs a Pointer object and initializes the buffer.
     */
    public Pointer() {
        this.buffer = IntBuffer.allocate(1);
    }

    /**
     * Returns the value at this Pointer.
     *
     * @return The value at this Pointer.
     */
    public int get() {
        return (int) buffer.get(0);
    }

    /**
     * Returns the IntBuffer associated with this Pointer.
     *
     * @return The IntBuffer associated with this Pointer.
     */
    public IntBuffer getBuffer() {
        return buffer;
    }

    /**
     * Sets the value at this Pointer to the given value.
     *
     * @param newValue The new value at this Pointer.
     */
    public void set(int newValue) {
        buffer.put(newValue, 0);
    }

    /**
     * Returns a String representation of this Pointer.
     */
    public String toString() {
        return String.valueOf(buffer.get(0));
    }

}