package env;

import core.Logger;
import geo.Sphere;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Light</b> class represents a light source.</p>
 */
public class Light implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Light at the specified location.
     *
     * @param location The location of this Light.
     */
    public Light(Vertex location) {
        this.location = location;
        this.sphere = new Sphere(location, 0.1f);
        Logger.debug("Creating Light at (%.2f, %.2f, %.2f).", location.getX(), location.getY(), location.getZ());
    }

    /**
     * Draws this Light.
     */
    public void draw() {
        this.sphere.draw();
    }

    /**
     * Returns the number of Polygons in this Light.
     *
     * @return The number of Polygons
     */
    public int polygons() {
        return this.sphere.polygons();
    }

    /**
     * Returns the location of this Light.
     *
     * @return The location.
     */
    public Vertex getPosition() {
        return this.location;
    }

    /**
     * Returns a String representation of this Light.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("Light %s", this.location.toString());
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The location of this Light.
     */
    private Vertex location;

    /**
     * The Sphere representing this Light.
     */
    private Sphere sphere;
}