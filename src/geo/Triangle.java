package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Triangle</b> class represents a geometric triangle.</p>
 */
public class Triangle extends Polygon {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Triangle object with the given set of Vertexes.
     *
     * @param vertexes The vertexes comprising this Triangle.
     */
    public Triangle(Vertex... vertexes) {
        super(vertexes, GL_TRIANGLES);
        if (vertexes.length != 3) {
            throw new IllegalArgumentException("Triangles must have exactly 3 vertexes.");
        }
    }

    /**
     * Returns the Vertex located in the middle of this Triangle.
     *
     * @return The middle Vertex.
     */
    public Vertex getMiddle() {
        return Vertex.average(this.vertexes);
    }

    /**
     * Returns a normalized GeoVector that is perpendicular to the face of this Triangle.
     *
     * @return The normalized GeoVector.
     */
    public Vector getNormal() {
        Vector v1 = new Vector(this.vertexes[0]).to(new Vector(this.vertexes[2]));
        Vector v2 = new Vector(this.vertexes[0]).to(new Vector(this.vertexes[1]));
        return new Vector(v1, v2);
    }
}