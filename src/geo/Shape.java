package geo;

import env.Drawable;

/**
 * @author Mikhail Andrenkov
 * @since February 17, 2018
 * @version 1.1
 *
 * <pThe <b>Shape</b> class represents a collection of Polygons.</p>
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
		return String.format("%s: %s", this.getClass().getName(), polygons.toString());
	}

    // Protected members
    // -------------------------------------------------------------------------

    /**
     * List of Shapes comprising this Shape.
     */
    protected Polygon[] polygons;

    /**
	 * Constructs an empty Shape object.
	 */
	protected Shape() {
        this.polygons = new Polygon[]{};
    }
}