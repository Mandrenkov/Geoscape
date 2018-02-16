package geo;

import env.Colour;
import env.Drawable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Mikhail Andrenkov
 * @since February 15, 2018
 * @version 1.1
 *
 * <pThe <b>Shape</b> class represents a geometric shape.</p>
 */
public abstract class Shape implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
	 * Draws this Shape.
	 */
	public abstract void draw();

    /**
	 * Returns a String representation of this Shape.
     * 
     * @return A String representing this Shape.
	 */
	public String toString() {
        ArrayList<Vertex> vertices = new ArrayList<>(Arrays.asList(this.vertices));
		return String.format("%s: %s", this.getClass().getName(), vertices.toString());
	}

    // Protected members
    // -------------------------------------------------------------------------

    /**
     * List of Vertices comprising this Shape.
     */
    protected Vertex[] vertices;

    /**
     * The colour of this Shape.
     */
    protected Colour colour;

    /**
	 * Constructs a Shape object with the given vertices.
     * 
     * @param vertices The vertices comprising this Shape.
	 */
	protected Shape(Vertex... vertices) {
        this.vertices = vertices;
    }

    /**
	 * Constructs a Shape object with the given vertices and colour.
     * 
     * @param colour   The colour of this Shape.
     * @param vertices The vertices comprising this Shape.
	 */
	protected Shape(Colour colour, Vertex... vertices) {
        this(vertices);
        this.colour = colour;
    }
}