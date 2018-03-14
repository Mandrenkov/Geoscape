package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Quad</b> class represents a quadrilateral.</p>
 */
public class Quad extends Polygon {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Quad with the given set of Vertices.
     *
     * @param vertices The vertices comprising this Quad.
     */
    public Quad(Vertex... vertices) {
        super(vertices, GL_QUADS);
        if (vertices.length != 4) {
            throw new IllegalArgumentException("Quads must have 4 vertices.");
        }
    }
}