package geo;

import env.Drawable;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
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
    public void draw() {
        for (Polygon polygon : this.polygons) {
            polygon.draw();
        }
    }

    /**
     * Returns the number of Polygons in this Shape.
     *
     * @return The number of Polygons
     */
    public int polygons() {
        return polygons.length;
    }

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