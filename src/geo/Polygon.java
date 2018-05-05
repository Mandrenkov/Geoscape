package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;

import env.Colour;
import env.Drawable;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <pThe <b>Polygon</b> class represents a geometric polygon.</p>
 */
public abstract class Polygon implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns a Vector that is perpendicular to the face of this Polygon.
     * 
     * @return The normal Vector.
     */
    public abstract Vector getNormal();

    /**
     * Draws this Polygon.
     */
    public void draw() {
        glBegin(this.mode);
        this.getNormal().glNormal();
        for (Vertex vertex : this.vertexes) {
            vertex.glColour();
            vertex.glVertex();
        }
        glEnd();
    }

    /**
     * Returns the number of Polygons in this Polygon.
     *
     * @return The number of Polygons
     */
    public int polygons() {
        return 1;
    }

    /**
     * Returns the Colour of this Polygon.
     *
     * @return The Colour of this Polygon.
     */
    public Colour getColour() {
        return this.vertexes[this.vertexes.length - 1].getColour();
    }

    /**
     * Returns the list of Vertexes comprising this Polygon.
     *
     * @return The Vertexes comprising this Polygon.
     */
    public Vertex[] getVertexes() {
        return this.vertexes;
    }

    /**
     * Sets the Colour of this Polygon.
     *
     * @param colour The new Colour of this Polygon.
     */
    public void setColour(Colour colour) {
        for (Vertex vertex : this.vertexes) {
            vertex.setColour(colour);
        }
    }

    /**
     * Sets the Vertexes of this Polygon.
     *
     * @param vertexes The new Vertexes of this Polygon.
     */
    public void setVertexes(Vertex[] vertexes) {
        this.vertexes = vertexes;
    }

    /**
     * Returns a String representation of this Polygon.
     *
     * @return A String representing this Polygon.
     */
    public String toString() {
        ArrayList<Vertex> vertexes = new ArrayList<>(Arrays.asList(this.vertexes));
        return String.format("%s: %s", this.getClass().getName(), vertexes.toString());
    }

    // Protected members
    // -------------------------------------------------------------------------

    /**
     * List of Vertexes comprising this Polygon.
     */
    protected Vertex[] vertexes;

    /**
     * The OpenGL drawing mode to be applied to this Polygon.
     */
    protected int mode;

    /**
     * Constructs an empty Polygon.
     */
    protected Polygon() {
        this.vertexes = new Vertex[]{};
    }

    /**
     * Constructs a Polygon with the given Vertexes.
     *
     * @param vertexes The Vertexes comprising this Polygon.
     */
    protected Polygon(Vertex[] vertexes) {
        this(vertexes, GL_POLYGON);
    }

    /**
     * Constructs a Polygon with the given OpenGL drawing mode and Vertexes.
     *
     * @param vertexes The Vertexes comprising this Polygon.
     * @param mode     The OpenGL drawing mode.
     */
    protected Polygon(Vertex[] vertexes, int mode) {
        this.vertexes = vertexes;
        this.mode = mode;
    }
}