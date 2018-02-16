package env;

import geo.*;
import util.*;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>LightSource</b> class.</p>
 */
public class LightSource implements Drawable {

	/**
	 * Radius of all LightSource objects.
	 */
	private final float RADIUS = 0.10f;
	/**
	 * Origin of this LightSource.
	 */
	private Vertex position;
	/**
	 * Spherical representation of this LightSource.
	 */
	private Sphere sphere;

	/**
	 * Constructs a LightSource object using the specified position.
	 *
	 * @param position Position of this LightSource.
	 */
	public LightSource(Vertex position) {
		this.position = position;
		this.sphere = new Sphere(position, RADIUS);
	}

	/**
	 * Draws this LightSource to the screen.
	 */
	public void draw() {
		Render.drawSphere(sphere, new float[]{1f, 1f, 1f});
	}

	/**
	 * Returns the position of this LightSource.
	 *
	 * @return The position of this LightSource.
	 */
	public Vertex getPosition() {
		return position;
	}

	/**
	 * Returns the Sphere associated with this LightSource.
	 *
	 * @return The Sphere associated with this LightSource.
	 */
	public Sphere getSphere() {
		return sphere;
	}

	/**
	 * Returns a String representation of this LightSource.
	 */
	public String toString() {
		return String.format("%s: (%.2f, %.2f, %.2f)", this.getClass().getName(), position.getX(), position.getY(), position.getZ());
	}
}