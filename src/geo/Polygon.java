package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;

import env.Colour;
import env.Drawable;

/**
 * @author Mikhail Andrenkov
 * @since February 15, 2018
 * @version 1.1
 *
 * <pThe <b>Polygon</b> class represents a geometric polygon.</p>
 */
public abstract class Polygon implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
	 * Draws this Polygon.
	 */
    public abstract void draw();

    /**
     * Returns the Colour of this Polygon.
     * 
     * @return The Colour of this Polygon.
     */
    public Colour getColour() {
        return colour;
    }
    
    /**
     * Returns the list of Vertices comprising this Polygon.
     * 
     * @return The Vertices comprising this Polygon.
     */
    public Vertex[] getVertices() {
        return vertices;
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
     * @param vertices The new Vertices of this Polygon.
     */
    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    /**
	 * Returns a String representation of this Polygon.
     * 
     * @return A String representing this Polygon.
	 */
	public String toString() {
        ArrayList<Vertex> vertices = new ArrayList<>(Arrays.asList(this.vertices));
		return String.format("%s: %s", this.getClass().getName(), vertices.toString());
	}

    // Protected members
    // -------------------------------------------------------------------------

    /**
     * List of Vertices comprising this Polygon.
     */
    protected Vertex[] vertices;

    /**
     * The colour of this Polygon.
     */
    protected Colour colour;

    /**
	 * Constructs an empty Polygon.
	 */
	protected Polygon() {
        this.vertices = new Vertex[]{};
    }

    /**
	 * Constructs a Polygon with the given Vertices.
     * 
     * @param vertices The Vertices comprising this Polygon.
	 */
	protected Polygon(Vertex... vertices) {
        this.vertices = vertices;
    }

    /**
	 * Constructs a Polygon with the given Colour and Vertices.
     * 
     * @param colour   The Colour of this Polygon.
     * @param vertices The Vertices comprising this Polygon.
	 */
	protected Polygon(Colour colour, Vertex... vertices) {
        this(vertices);
        this.colour = colour;
    }

    /**
	 * Adds the given Vertex to the GL representation of this Polygon.
	 *
	 * @param vertex The vertex to be added.
	 */
	protected void addVertex(Vertex vertex) {
		glVertex3f(vertex.getX(), vertex.getY(), vertex.getZ());
	}
}