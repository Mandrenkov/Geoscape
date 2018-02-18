package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;

import env.Colour;
import env.Drawable;

/**
 * @author Mikhail Andrenkov
 * @since February 17, 2018
 * @version 1.1
 *
 * <pThe <b>Composite</b> class represents a composite shape.</p>
 */
public abstract class Composite implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
	 * Draws this Composite.
	 */
    public abstract void draw();

    /**
	 * Returns a String representation of this Composite.
     * 
     * @return A String representing this Composite.
	 */
	public String toString() {
		return String.format("%s: %s", this.getClass().getName(), shapes.toString());
	}

    // Protected members
    // -------------------------------------------------------------------------

    /**
     * List of Shapes comprising this Composite.
     */
    protected Shape[] shapes;

    /**
	 * Constructs an empty Shape object.
	 */
	protected Composite() {
        this.shapes = new Shape[]{};
    }
    
    /**
	 * Sets the Shapes composing this Composite to the given array of Shapes.
	 *
	 * @param shapes The new Shapes representing this Composite.
	 */
	protected void setShapes(Shape[] shapes) {
        this.shapes = shapes;
	}
}