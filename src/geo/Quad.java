package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.Arrays;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2018
 * @version 1.1
 *
 * <p>The <b>Quad</b> class represents a quadrilateral.</p>
 */
public class Quad extends Shape {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Quad with the given set of Vertices.
	 * 
	 * @param vertices The vertices comprising this Quad.
	 */
	public Quad(Vertex... vertices) {
		super(vertices);
		if (vertices.length != 4) {
			throw new IllegalArgumentException("Quads must have 4 vertices.");
		}
	}

	/**
	 * Draws this Quad.
	 */
	public void draw() {
		colour.glColor();

        glBegin(GL_QUADS);
		for (Vertex vertex : vertices) {
			addVertex(vertex);
		}
		glEnd();
	}
}