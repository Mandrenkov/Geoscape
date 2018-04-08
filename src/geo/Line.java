package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Line</b> class represents a line.</p>
 */
public class Line extends Polygon {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Line with the given Colour and start and end Vertexes.
     *
     * @param colour The Colour of this Line.
     * @param start  The start Vertex of this Line.
     * @param end    The end Vertex of this Line.
     */
    public Line(Vertex... vertexes) {
        super(vertexes, GL_LINES);
        if (vertexes.length != 2) {
            throw new IllegalArgumentException("Lines must have exactly 2 vertexes.");
        }
    }

    /**
     * Returns a Vector that is perpendicular to this Line.
     *
     * @return The normal Vector.
     */
    public Vector getNormal() {
        Vector v1 = new Vector(this.vertexes[0]);
        Vector v2 = new Vector(this.vertexes[1]);
        return new Vector(v1, v2);
    }
}