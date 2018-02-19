package geo;

import static org.lwjgl.opengl.GL11.*;

import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since February 17, 2018
 * @version 1.1
 *
 * <p>The <b>Line</b> class represents a line.</p>
 */
public class Line extends Shape {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Line with the given Colour and start and end Vertices.
	 * 
     * @param colour The Colour of this Line.
	 * @param start  The start Vertex of this Line.
     * @param end    The end Vertex of this Line.
	 */
	public Line(Colour colour, Vertex start, Vertex end) {
        super(colour, start, end);
	}

	/**
	 * Draws this Line.
	 */
	public void draw() {
		colour.glColor();

		glBegin(GL_LINE);
		for (Vertex vertex : vertices) {
			addVertex(vertex);
		}
		glEnd();
	}
}