package model;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>AlgVector2D</i> class represents a 2D algebraic vector.</p>
 */ 
public class AlgVector2D extends AlgVector {

	/**
	 * Constructs an AlgVector2D with the specified X and Y components.
	 * 
	 * @param x Value of AlgVector2D along the X dimension.
	 * @param y Value of AlgVector2D along the Y dimension.
	 */
	public AlgVector2D(float x, float y) {
		super(new float[]{x, y});
	}

	/**
	 * Returns the value of the X component of this AlgVector2D.
	 * 
	 * @return The value of the X component of this AlgVector2D.
	 */
	public float getX() {
		return super.get(0);
	}

	/**
	 * Returns the value of the Y component of this AlgVector2D.
	 * 
	 * @return The value of the Y component of this AlgVector2D.
	 */
	public float getY() {
		return super.get(1);
	}

	/**
	 * Sets the magnitude of this AlgVector2D along the X dimension to the specified value.
	 * 
	 * @param x The desired magnitude along the X dimension.
	 */
	public void setX(int x) {
		super.set(0, x);
	}

	/**
	 * Sets the magnitude of this AlgVector2D along the Y dimension to the specified value.
	 * 
	 * @param x The desired magnitude along the Y dimension.
	 */
	public void setY(int y) {
		super.set(1, y);
	}
}