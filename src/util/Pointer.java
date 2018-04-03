package util;

import java.nio.IntBuffer;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Pointer</b> class is an implementation of a thread-safe integer
 * pointer.</p>
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
     * Constructs a Pointer object with the given value.
     * 
     * @param value The initial value of this Pointer.
     */
    public Pointer(int value) {
        this();
        this.buffer.put(0, value);
    }

    /**
     * Adds the given value to this Pointer.
     * 
     * @param value The value to add.
     */
    public void add(int value) {
        int oldValue = this.get();
        int newValue = oldValue + value;
        this.set(newValue);
    }

    /**
     * Returns the value at this Pointer.
     *
     * @return The value at this Pointer.
     */
    public int get() {
        return (int) this.buffer.get(0);
    }

    /**
     * Increments the value of this Pointer.
     */
    public void increment() {
        int value = this.get();
        ++value;
        this.set(value);
    }

    /**
     * Returns the IntBuffer associated with this Pointer.
     *
     * @return The IntBuffer associated with this Pointer.
     */
    public IntBuffer getBuffer() {
        return this.buffer;
    }

    /**
     * Sets the value at this Pointer to the given value.
     *
     * @param newValue The new value at this Pointer.
     */
    public void set(int newValue) {
        this.buffer.put(0, newValue);
    }

    /**
     * Returns a String representation of this Pointer.
     */
    public String toString() {
        return String.valueOf(this.buffer.get(0));
    }
}