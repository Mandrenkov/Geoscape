package model;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>AlgVector2D</i> class represents a 3D algebraic vector.</p>
 */ 
public class AlgVector3D extends AlgVector {

	/**
	 * Constructs an AlgVector3D with the specified X, Y, and Z components.
	 * 
	 * @param x Value of AlgVector3D along the X dimension.
	 * @param y Value of AlgVector3D along the Y dimension.
	 * @param z Value of AlgVector3D along the Z dimension.
	 */
	public AlgVector3D(float x, float y, float z) {
		super(new float[]{x, y, z});
	}

	/**
	 * Constructs an AlgVector3D that starts at the origin and ends at the specified Point.
	 * 
	 * @param p Point that denotes the end coordinates of the AlgVector3D.
	 */
	public AlgVector3D(Point p) {
		super(new float[]{p.getX(), p.getY(), p.getZ()});
	}

	/**
	 * Returns the cross product between this AlgVector3D and the specified AlgVector3D.
	 * 
	 * @param vector Second operand of the cross product operation.
	 * 
	 * @return The cross product.
	 */
	public AlgVector3D cross(AlgVector3D vector) {
		float x, y, z;

		x = this.getY()*vector.getZ() - this.getZ()*vector.getY();
		y = this.getZ()*vector.getX() - this.getX()*vector.getZ();
		z = this.getX()*vector.getY() - this.getY()*vector.getX();

		return new AlgVector3D(x, y, z);
	}

	/**
	 * Returns the AlgVector3D that can be added to this AlgVector3D to produce the specified AlgVector3D.
	 * 
	 * @param vector Target AlgVector3D.
	 * 
	 * @return The AlgVector3D that connects this AlgVector3D to the given AlgVector3D.
	 */
	public AlgVector3D to(AlgVector3D vector) {
		float[] components = super.to(vector).getAll();
		return new AlgVector3D(components[0], components[1], components[2]);
	}

	/**
	 * Returns the value of the X component of this AlgVector3D.
	 * 
	 * @return The value of the X component of this AlgVector3D.
	 */
	public float getX() {
		return super.get(0);
	}

	/**
	 * Returns the value of the Y component of this AlgVector3D.
	 * 
	 * @return The value of the Y component of this AlgVector3D.
	 */
	public float getY() {
		return super.get(1);
	}

	/**
	 * Returns the value of the Z component of this AlgVector3D.
	 * 
	 * @return The value of the Z component of this AlgVector3D.
	 */
	public float getZ() {
		return super.get(2);
	}

	/**
	 * Sets the magnitude of this AlgVector3D along the X dimension to the specified value.
	 * 
	 * @param x The desired magnitude along the X dimension.
	 */
	public void setX(int x) {
		super.set(0, x);
	}

	/**
	 * Sets the magnitude of this AlgVector3D along the Y dimension to the specified value.
	 * 
	 * @param x The desired magnitude along the Y dimension.
	 */
	public void setY(int y) {
		super.set(1, y);
	}

	/**
	 * Sets the magnitude of this AlgVector3D along the Z dimension to the specified value.
	 * 
	 * @param x The desired magnitude along the Z dimension.
	 */
	public void setZ(int z) {
		super.set(2, z);
	}
}