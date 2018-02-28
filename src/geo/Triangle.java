package geo;

import static org.lwjgl.opengl.GL11.*;

import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since February 17, 2018
 * @version 1.1
 *
 * <p>The <b>Triangle</b> class represents a geometric triangle.</p>
 */
public class Triangle extends Shape {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Triangle object with the given set of Vertices.
	 * 
	 * @param vertices The vertices comprising this Triangle.
	 */
	public Triangle(Vertex... vertices) {
		this(new Colour(), vertices);
	}
	
	/**
	 * Constructs a Triangle object with the given Vertices and Colour.
	 * 
	 * @param colour   The colour of this Triangle.
	 * @param vertices The vertices comprising this Triangle.
	 */
	public Triangle(Colour colour, Vertex... vertices) {
		super(colour, vertices);
		if (vertices.length != 3) {
			throw new IllegalArgumentException("Triangles must have 3 vertices.");
		}
	}

	/**
	 * Draws this Triangle.
	 */
	public void draw() {
		colour.glColor();

		glBegin(GL_TRIANGLES);
		for (Vertex vertex : vertices) {
			addVertex(vertex);
		}
		glEnd();
	}

	/**
	 * Returns the Vertex located in the middle of this Triangle.
	 * 
	 * @return The middle Vertex.
	 */
	public Vertex getMiddle() {
		return Vertex.average(vertices);
	}

	/**
	 * Returns a normalized GeoVector that is perpendicular to the face of this Triangle.
	 * 
	 * @return The normalized GeoVector.
	 */
	public Vector getNormal() {
		Vector v1 = new Vector(vertices[0]).to(new Vector(vertices[1]));
		Vector v2 = new Vector(vertices[0]).to(new Vector(vertices[2]));
		return new Vector(v1, v2);
	}
}