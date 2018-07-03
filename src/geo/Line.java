package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * The Line class represents a line.
 */
public class Line extends Polygon {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Line with the given start and end Vertexes.
     *
     * @param vertexes The start and end Vertexes of this Line.
     */
    public Line(Vertex... vertexes) {
        super(vertexes, GL_LINES);
        if (vertexes.length != 2) {
            throw new IllegalArgumentException("Lines must have exactly 2 vertexes.");
        }
    }

    /**
     * Draws this Line.
     */
    public void draw() {
        // Lines do not have faces and should not be affected by lighting. 
        glDisable(GL_LIGHTING);
            super.draw();
        glEnable(GL_LIGHTING);
    }

    /**
     * Returns a normalized Vector that is perpendicular to this Line.
     *
     * @return The normal Vector.
     */
    public Vector getNormal() {
        Vector v1 = new Vector(this.vertexes[0]);
        Vector v2 = new Vector(this.vertexes[1]);

        Vector normal = new Vector(v1, v2);
        normal.normalize();
        return normal;
    }
}