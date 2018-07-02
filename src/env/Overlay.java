package env;

import static org.lwjgl.opengl.GL11.*;

import geo.Quad;
import geo.Vertex;

/**
 * The Overlay class represents a 2D screen overlay.
 */
public class Overlay implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a new Overlay with the given Colour.
     * 
     * @param colour The Colour of this Overlay.
     */
    public Overlay(Colour colour) {
        this.colour = colour;
    }

    /**
     * Draws this Overlay.
     */
    public void draw() {
        // Save and reset the states of the OpenGL modelview and projecton matrices.
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();

        // Apply a simple orthogonal projection to the OpenGL projection matrix.
        glOrtho(0, 1, 1, 0, -1, 1);

        // Switch the current matrix to the OpenGL modelview matrix.
        glMatrixMode(GL_MODELVIEW);

        // Draw a Quad that fills the entire screen with the colour of this Overlay.
        Quad quad = new Quad(
            new Vertex(0, 0, 0, this.colour),
            new Vertex(0, 1, 0, this.colour),
            new Vertex(1, 1, 0, this.colour),
            new Vertex(1, 0, 0, this.colour)
        );
        quad.draw();

        // Restore the states of the OpenGL projection and modelview matrices.
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();

        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
    }

    /**
     * Returns the number of Polygons in this Overlay.
     * 
     * @return The number of Polygons in this Overlay.
     */
    public int polygons() {
        // The only polygon is the Quad that stretches across the screen.
        return 1;
    }

    // Private members
    // -------------------------------------------------------------------------

    private Colour colour;
}