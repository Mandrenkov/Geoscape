package util;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Pointer</b> class.</p>
 */
public class Pointer {

	public static final int NULL_VALUE = -1;

	private int [] value = new int [1];

	public Pointer() {
		this.value[0] = NULL_VALUE;
	}

	public int[] array() {
		return value;
	}

	public int get() {
		return value[0];
	}

	public void set(int newValue) {
		value[0] = newValue;
	}

	@Override
	public String toString() {
		return String.valueOf(value[0]);
	}

}