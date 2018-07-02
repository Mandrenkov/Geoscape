package geo;

import env.Drawable;

/**
 * The Shape class represents a collection of Polygons.
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
     * Returns the Polygons that comprise this Shape.
     *
     * @return The Polygons.
     */
    public Polygon[] getPolygons() {
        return this.polygons;
    }

    /**
     * Returns the number of Polygons in this Shape.
     *
     * @return The number of Polygons
     */
    public int polygons() {
        return this.polygons.length;
    }

    /**
     * Returns a String representation of this Shape.
     *
     * @return A String representing this Shape.
     */
    public String toString() {
        return String.format("%s: %s", this.getClass().getName(), this.polygons.toString());
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