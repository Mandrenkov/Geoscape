package geo;

/**
 * @author Mikhail Andrenkov
 * @since April 10, 2017
 * @version 1.0
 *
 * <pThe <b>Point</b> class represents a 3D point.</p>
 */
public class Point {
	/**
	 * The dimensional components of this Point relative to the origin.
	 */
	private float x, y, z;

	/**
	 * Constructs a Point object with the given 3D coordinate.
	 * 
	 * @param x X-coordinate of this Point.
	 * @param y Y-coordinate of this Point.
	 * @param z Z-coordinate of this Point.
	 */
	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns a new Point denoting the midpoint between this Point and the given Point.
	 * 
	 * @param point The second Point.
	 * @return The midpoint of the two Points.
	 */
	public Point average(Point point) {
		return new Point((this.x + point.x)/2, (this.y + point.y)/2, (this.z + point.z)/2);
	}

	/**
	 * Changes the elevation (Z) of this Point by a random value within the given range.
	 * 
	 * @param range The maximum magnitude of the elevation change.
	 */
	public void bump(float range) {
		z += (float) (range*(2*Math.random()- 1));
	}

	/**
	 * Returns the distance between this Point and the given Point.
	 * 
	 * @param point The second Point.
	 * @return The distance between the points.
	 */
	public float distance(Point point) {
		return (float) Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
	}

	/**
	 * Returns the X-coordinate of this Point.
	 * 
	 * @return The X-coordinate of this Point.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the Y-coordinate of this Point.
	 * 
	 * @return The Y-coordinate of this Point.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the Z-coordinate of this Point.
	 * 
	 * @return The Z-coordinate of this Point.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the X-coordinate of this Point to the specified value.
	 * 
	 * @param x The new X-coordinate
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the Y-coordinate of this Point to the specified value.
	 * 
	 * @param y The new Y-coordinate
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Sets the Z-coordinate of this Point to the specified value.
	 * 
	 * @param z The new Z-coordinate
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns a String representation of this Point.
	 */
	public String toString() {
		return String.format("Point: (%.2f, %.2f, %.2f)", x, y, z);
	}
}