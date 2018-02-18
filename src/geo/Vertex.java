package geo;

/**
 * @author Mikhail Andrenkov
 * @since February 15, 2018
 * @version 1.1
 *
 * <pThe <b>Vertex</b> class represents a point in R3.</p>
 */
public class Vertex {
	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Vertex representing the given 3D coordinate.
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
	 * Constructs a Vertex at the midpoint of the given Vertices.
	 *
	 * @param v1 The first Vertex.
	 * @param v2 The second Vertex.
	 */
	public Vertex(Vertex v1, Vertex v2) {
		this.x = (v1.x + v2.x)/2;
		this.y = (v1.y + v2.y)/2;
		this.z = (v1.z + v2.z)/2;
	}

	/**
	 * Changes the elevation of this Vertex by a random value within the given range.
	 *
	 * @param magnitude The maximum magnitude of the change in elevation.
	 */
	public void bump(float magnitude) {
		float random = (float) (Math.random() - 0.5);
		this.z += magnitude*2*random;
	}

	/**
	 * Returns the distance between this Vertex and the given Vertex.
	 *
	 * @param vertex The other Vertex.
	 * 
	 * @return The distance between the Vertices.
	 */
	public float distance(Vertex vertex) {
		return (float) Math.sqrt(Math.pow(this.x - vertex.x, 2)
							   + Math.pow(this.y - vertex.y, 2)
							   + Math.pow(this.z - vertex.z, 2));
	}

	/**
	 * Returns the X-coordinate of this Vertex.
	 *
	 * @return The X-coordinate.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the Y-coordinate of this Vertex.
	 *
	 * @return The Y-coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the Z-coordinate of this Vertex.
	 *
	 * @return The Z-coordinate.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the X-coordinate of this Vertex to the specified value.
	 *
	 * @param x The new X-coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the Y-coordinate of this Vertex to the specified value.
	 *
	 * @param y The new Y-coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Sets the Z-coordinate of this Vertex to the specified value.
	 *
	 * @param z The new Z-coordinate.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Scales the coordinates of this Vertex by the given scaling factor.
	 * 
	 * @param scalar The scaling factor.
	 */
	public void scale(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
	}

	/**
	 * Translates this Vertex by the given coordinate values.
	 * 
	 * @param dx The amount to translate the X-coordinate of this Vertex.
	 * @param dy The amount to translate the Y-coordinate of this Vertex.
	 * @param dz The amount to translate the Z-coordinate of this Vertex.
	 */
	public void translate(float dx, float dy, float dz) {
		this.x += dx;
		this.y += dy;
		this.z += dz;
	}

	/**
	 * Returns a String representation of this Vertex.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("Vertex: (%.2f, %.2f, %.2f)", x, y, z);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The X-coordinate of this Vertex.
	 */
	private float x;
	
	/**
	 * The Y-coordinate of this Vertex.
	 */
	private float y;
	
	/**
	 * The Z-coordinate of this Vertex.
	 */
	private float z;
}