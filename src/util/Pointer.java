package util;

import java.nio.IntBuffer;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <b>Pointer</b> class is an implementation of an integer pointer.</p>
 */
public class Pointer {

	/**
	 * Buffer to hold
	 */
	private IntBuffer buffer;

	public Pointer() {
		this.buffer = IntBuffer.allocate(1);
	}

	public int get() {
		return (int) buffer.get(0);
	}

	public IntBuffer getBuffer() {
		return buffer;
	}
	
	public void set(int newValue) {
		buffer.put(newValue, 0);
	}

	@Override
	public String toString() {
		return String.valueOf(buffer.get(0));
	}

}