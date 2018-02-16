package geo;

/**
 * @author Mikhail Andrenkov
 * @since February 15, 2018
 * @version 1.1
 *
 * <pThe <b>Vertex</b> class represents a 3D Vertex.</p>
 */
public class Vertex {
	/**
	 * The dimensional components of this Vertex relative to the origin.
	 */
	private float x, y, z;

	/**
	 * Constructs a Vertex object with the given 3D coordinate.
	 *
	 * @param x The X-coordinate of this Vertex.
	 * @param y The Y-coordinate of this Vertex.
	 * @param z The Z-coordinate of this Vertex.
	 */
	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns a new Vertex at the midpoint between this Vertex and the given Vertex.
	 *
	 * @param Vertex The second Vertex.
	 * 
	 * @return The midpoint Vertex.
	 */
	public Vertex average(Vertex Vertex) {
		return new Vertex((this.x + Vertex.x)/2, (this.y + Vertex.y)/2, (this.z + Vertex.z)/2);
	}

	/**
	 * Changes the elevation (Z) of this Vertex by a random value within the given range.
	 *
	 * @param range The maximum magnitude of the elevation change.
	 */
	public void bump(float range) {
		z += (float) (range*(2*Math.random()- 1));
	}

	/**
	 * Returns the distance between this Vertex and the given Vertex.
	 *
	 * @param Vertex The second Vertex.
	 * @return The distance between the Vertexs.
	 */
	public float distance(Vertex Vertex) {
		return (float) Math.sqrt(Math.pow(this.x - Vertex.x, 2) + Math.pow(this.y - Vertex.y, 2));
	}

	/**
	 * Returns the X-coordinate of this Vertex.
	 *
	 * @return The X-coordinate of this Vertex.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the Y-coordinate of this Vertex.
	 *
	 * @return The Y-coordinate of this Vertex.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the Z-coordinate of this Vertex.
	 *
	 * @return The Z-coordinate of this Vertex.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the X-coordinate of this Vertex to the specified value.
	 *
	 * @param x The new X-coordinate
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the Y-coordinate of this Vertex to the specified value.
	 *
	 * @param y The new Y-coordinate
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Sets the Z-coordinate of this Vertex to the specified value.
	 *
	 * @param z The new Z-coordinate
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns a String representation of this Vertex.
	 */
	public String toString() {
		return String.format("Vertex: (%.2f, %.2f, %.2f)", x, y, z);
	}
}