package env;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import geo.Polygon;
import geo.Sphere;
import geo.Vertex;
import util.Algebra;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
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
        this.sphere = new Sphere(origin, radius, true, 5);

        // Set a random dark hue for each face of the Sphere. 
        for (Polygon face : this.sphere.getPolygons()) {
            Colour colour = Colour.random(Colour.Option.DARK);
            face.setColour(colour);
        }

        // The number of stars in this Backdrop.
        int size = 500;
        this.stars = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            // Select a horizontal cross-section on the Sphere.
            //        ___ 
            // -->  /     \
            //     /       \
            //     \       /
            //      \ ___ /
            float z = Algebra.random(radius);

            // Select a Vertex from the horizontal cross-section on the Sphere.
            //        ___ 
            //      /     \
            //     /   .   \
            //     \    \  /
            //      \ ___X
            double angle = Math.random()*2*Math.PI;
            float r = (float) Math.sqrt(radius*radius - z*z) - 0.1f;
            float x = (float) Math.cos(angle)*r;
            float y = (float) Math.sin(angle)*r;

            // The Colour of a star ranges between yellow and white.
            Colour colour = new Colour(1, 1, 0.5f);
            colour.shift(0.5f);

            // Add the star to this Backdrop.
            Vertex star = new Vertex(x, y, z);
            star.setColour(colour);
            this.stars.add(star);
        }
    }

    /**
     * Draws this Backdrop.
     */
    public void draw() {
        glBegin(GL_POINTS);
        for (Vertex vertex : this.stars) {
            glMaterialfv(GL_FRONT, GL_EMISSION, vertex.getColour().toArray());
                vertex.glVertex();
            glMaterialfv(GL_FRONT, GL_EMISSION, Colour.GL_BLACK);
            
        }
        glEnd();
        

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

    /**
     * The list of stars in this Backdrop.
     */
    private ArrayList<Vertex> stars;
}