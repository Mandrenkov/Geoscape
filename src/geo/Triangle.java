package geo;

import static org.lwjgl.opengl.GL11.*;

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
		super(vertices);
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
	 * Returns a normalized GeoVector that is perpendicular to the face of this Triangle.
	 * 
	 * @return The normalized GeoVector.
	 */
	public GeoVector getNormal() {
		GeoVector vectorA = new GeoVector(vertices[0]).to(new GeoVector(vertices[1]));
		GeoVector vectorB = new GeoVector(vertices[0]).to(new GeoVector(vertices[2]));
		return vectorA.cross(vectorB);
	}
}