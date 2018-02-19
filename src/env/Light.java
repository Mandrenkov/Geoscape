package env;

import geo.Sphere;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2018
 * @version 1.1
 *
 * <p>The <b>Light</b> class represents a light source.</p>
 */
public class Light implements Drawable {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Light at the specified position.
	 *
	 * @param position The position of this Light.
	 */
	public Light(Vertex position) {
		this.position = position;
		this.sphere = new Sphere(position, RADIUS);
	}

	/**
	 * Draws this Light.
	 */
	public void draw() {
		this.sphere.draw();
	}

	/**
	 * Returns the position of this Light.
	 *
	 * @return The position.
	 */
	public Vertex getPosition() {
		return position;
	}

	/**
	 * Returns a String representation of this Light.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return position.toString();
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The radius of the Spheres that represent Lights.
	 */
	private final float RADIUS = 0.10f;

	/**
	 * The position of this Light.
	 */
	private Vertex position;

	/**
	 * The Sphere representing this Light.
	 */
	private Sphere sphere;
}