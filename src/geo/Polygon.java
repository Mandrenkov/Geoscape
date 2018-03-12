package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;

import core.Logger;
import env.Colour;
import env.Drawable;

/**
 * @author Mikhail Andrenkov
 * @since February 15, 2018
 * @version 1.1
 *
 * <pThe <b>Polygon</b> class represents a geometric polygon.</p>
 */
public class Polygon implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
	 * Draws this Polygon.
	 */
    public void draw() {
        glBegin(this.mode);	
		for (Vertex vertex : this.vertexes) {
            vertex.getColour().gl();
			vertex.gl();
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
        return this.colour;
    }
    
    /**
     * Returns the list of Vertices comprising this Polygon.
     * 
     * @return The Vertices comprising this Polygon.
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
        this.colour = colour;
    }

    /**
     * Sets the Vertices of this Polygon.
     * 
     * @param vertexes The new Vertices of this Polygon.
     */
    public void setVertices(Vertex[] vertexes) {
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
     * The colour of this Polygon.
     */
    protected Colour colour;

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
	 * Constructs a Polygon with the given Vertices.
     * 
     * @param vertexes The Vertices comprising this Polygon.
	 */
	protected Polygon(Vertex[] vertexes) {
        this(vertexes, GL_POLYGON);
    }

    /**
	 * Constructs a Polygon with the given OpenGL drawing mode and Vertices.
     * 
     * @param vertexes The Vertices comprising this Polygon.
     * @param mode     The OpenGL drawing mode.
	 */
	protected Polygon(Vertex[] vertexes, int mode) {
        this.vertexes = vertexes;
        this.mode = mode;
    }
}