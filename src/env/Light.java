package env;

import static org.lwjgl.opengl.GL11.*;

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

        // The shared OpenGL light index variable must be accessed exclusively.
        synchronized (Light.class) {
            this.glIndex = Light.nextGLindex++;
            if (this.glIndex > GL_LIGHT7) {
                throw new IllegalStateException("OpenGL does not support more than 8 light sources.");
            }
        } 

        float[] diffusion = {0.8f, 0.8f, 0.8f, 1.0f};
        glLightfv(this.glIndex, GL_DIFFUSE, diffusion);
        glLightfv(this.glIndex, GL_POSITION, location.toArray());
        glEnable(this.glIndex);     

        Logger.debug("Created Light %d at (%.2f, %.2f, %.2f).", this.glIndex - GL_LIGHT0, location.getX(), location.getY(), location.getZ());
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
     * Sets the position of the OpenGL light associated with this Light to the
     * location of this Light.
     */
    public void glPosition() {
        glLightfv(this.glIndex, GL_POSITION, location.toArray());
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
     * The next OpenGL light index.
     */
    private static volatile int nextGLindex = GL_LIGHT0; 

    /**
     * The location of this Light.
     */
    private Vertex location;

    /**
     * The Sphere representing this Light.
     */
    private Sphere sphere;

    /**
     * The OpenGL light index of this Light. 
     */
    private int glIndex;
}