package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>Quad</b> class represents a quadrilateral.</p>
 */
public class Quad extends Polygon {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Quad with the given set of Vertexes.
     *
     * @param vertexes The vertexes comprising this Quad.
     */
    public Quad(Vertex... vertexes) {
        super(vertexes, GL_QUADS);
        if (vertexes.length != 4) {
            throw new IllegalArgumentException("Quads must have exactly 4 vertexes.");
        }
    }

    /**
     * Returns a normalized Vector that is perpendicular to the face of this Quad.
     *
     * @return The normal Vector.
     */
    public Vector getNormal() {
        Vector v1 = new Vector(this.vertexes[0]).to(new Vector(this.vertexes[2]));
        Vector v2 = new Vector(this.vertexes[0]).to(new Vector(this.vertexes[1]));
        
        Vector normal = new Vector(v1, v2);
        normal.normalize();
        return normal;
    }
}