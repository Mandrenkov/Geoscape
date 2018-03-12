package env;

import static org.lwjgl.opengl.GL11.*;

import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since February 19, 2018
 * @version 1.1
 *
 * <p>The <i>Camera</i> class manipulates the view perspective.</p>
 */
public class Camera {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Axis represents an axis in Cartesian space.
	 */
	public enum Axis {
		X,  // X-axis
		Y,  // Y-axis
		Z   // Z-axis
	};

	/**
	 * Returns the singleton Camera instance.
	 * 
	 * @return The singleton instance.
	 */
	public static Camera getInstance() {
		if (singleton == null) {
			singleton = new Camera();
		}
		return singleton;
	}

	/**
	 * Rotates the Camera about the given Axis by the specified value in degrees.
	 *
	 * @param axis     The Axis of rotation.
	 * @param rotation The degree of counter-clockwise rotation about the Axis.
	 */
	/*public void rotate(Axis axis, int rotation) {
		int x = axis == Axis.X ? 1 : 0;
		int y = axis == Axis.Y ? 1 : 0;
		int z = axis == Axis.Z ? 1 : 0;
		glRotatef(rotation, x, y, z);
	}*/

	/**
	 * Rotates the Camera about the given Axis by the specified value in degrees.
	 *
	 * @param rotation The degree of CCW rotation about the Axis.
	 * @param x        The X-component of the line.
	 * @param y        The Y-component of the line.
	 * @param z        The Z-component of the line.
	 */
	public void rotate(float rotation, float x, float y, float z) {
		glRotatef(rotation, x, y, z);
	}

	/**
	 * Translates this Camera by the given coordinate values.
	 * 
	 * @param dx The amount to translate the X-coordinate of this Camera.
	 * @param dy The amount to translate the Y-coordinate of this Camera.
	 * @param dz The amount to translate the Z-coordinate of this Camera.
	 */
	public void translate(float dx, float dy, float dz) {
		position.translate(dx, dy, dz);
		glTranslatef(-dx, -dy, -dz);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The reference to the Camera singleton.
	 */
	private static Camera singleton = null;

	/**
	 * The position of this Camera.
	 */
	private Vertex position;

	/**
	 * Constructs a new Camera that is oriented in a starting position. 
	 */
	private Camera() {
		this.position = new Vertex(Vertex.ORIGIN);

		this.translate(0, 0, 1.5f);
		//this.rotate(-45, 1, 0, 0);
		this.rotate(-65, 1, 0, 0);
		this.rotate(-135, 0, 0, 1);
	}
}