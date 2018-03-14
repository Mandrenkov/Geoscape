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
     * Constructs a Line with the given Colour and start and end Vertices.
     *
     * @param colour The Colour of this Line.
     * @param start  The start Vertex of this Line.
     * @param end    The end Vertex of this Line.
     */
    public Line(Vertex start, Vertex end) {
        super(new Vertex[]{start, end}, GL_LINES);
    }
}