package env;

import geo.Polygon;
import geo.Sphere;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since May 1, 2018
 * @version 1.1
 *
 * <p>The <b>Backdrop</b> class represents a World backdrop.</p>
 */
public class Backdrop implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Backdrop with respect to the given origin and radius.
     *
     * @param origin The origin of the Backdrop.
     * @param radius The radius of the Backdrop.
     */
    public Backdrop(Vertex origin, float radius) {
        this.sphere = new Sphere(origin, radius, true, 4);

        // Set a random dark hue for each face of the Sphere. 
        for (Polygon face : this.sphere.getPolygons()) {
            face.setColour(Colour.random(Colour.Option.DARK));
        }
    }

    /**
     * Draws this Backdrop.
     */
    public void draw() {
        this.sphere.draw();
    }

	/**
     * Returns the number of Polygons in this Backdrop.
     *
     * @return The number of Polygons.
     */
    public int polygons() {
        return this.sphere.polygons();
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The Sphere representing this Backdrop.
     */
    private Sphere sphere;
}