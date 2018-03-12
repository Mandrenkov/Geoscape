package env;

import geo.Prism;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2018
 * @version 1.1
 *
 * <p>The <b>Platform</b> class represents the platform beneath the landscape.</p>
 */
public class Platform implements Drawable {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Platform with the given geometric constraints.
	 *
	 * @param minX The minimum X constraint.
	 * @param minY The minimum Y constraint.
	 * @param minZ The minimum Z constraint.
	 * @param maxX The maximum X constraint.
	 * @param maxY The maximum Y constraint.
	 * @param maxZ The maximum Z constraint.
	 */
	public Platform(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;

		Vertex low = new Vertex(minX, minY, minZ);
		Vertex high = new Vertex(maxX, maxY, maxZ);
		Colour clr = new Colour(0.1f, 0.1f, 0.1f, 1.0f);
		this.prism = new Prism(clr, low, high);
	}

	/**
	 * Draws this Platform.
	 */
	public void draw() {
		prism.draw();
	}

	/**
     * Returns the number of Polygons in this Polygon.
     * 
     * @return The number of Polygons
     */
    public int polygons() {
        return prism.polygons();
    }

	/**
	 * Returns the String representation of this Platform.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f)", this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The minimum X-coordinate of this Platform.
	 */
	private float minX;

	/**
	 * The minimum Y-coordinate of this Platform.
	 */
	private float minY;

	/**
	 * The minimum Z-coordinate of this Platform.
	 */
	private float minZ;

	/**
	 * The minimum X-coordinate of this Platform.
	 */
	private float maxX;

	/**
	 * The minimum Y-coordinate of this Platform.
	 */
	private float maxY;

	/**
	 * The minimum Z-coordinate of this Platform.
	 */
	private float maxZ;

	/**
	 * The Prism representing the Platform.
	 */
	private Prism prism;
}