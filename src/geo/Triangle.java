package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
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
     * Returns a normalized Vector that is perpendicular to the face of this Triangle.
     *
     * @return The normalized Vector.
     */
    public Vector getNormal() {
        Vector v1 = new Vector(this.vertexes[0]).to(new Vector(this.vertexes[2]));
        Vector v2 = new Vector(this.vertexes[0]).to(new Vector(this.vertexes[1]));
        
        Vector normal = new Vector(v1, v2);
        normal.normalize();
        return normal;
    }

    /**
     * Reverses the order of the Vertexes that comprise this Triangle.
     */
    public void reverse() {
        Vertex vertex = this.vertexes[0];
        this.vertexes[0] = this.vertexes[2];
        this.vertexes[2] = vertex;
    }
}