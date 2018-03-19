package bio;

import static org.lwjgl.opengl.GL11.*;

import env.Colour;
import env.Light;
import geo.Triangle;
import geo.Vector;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>BioTriangle</b> class represents a Biome Triangle.</p>
 */
public class BioTriangle extends Triangle {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a BioTriangle using the given BioVertexes.
     *
     * @param biotexes The BioVertexes comprising this BioTriangle.
     */
    public BioTriangle(BioVertex... biotexes) {
        super((Vertex[]) biotexes);
        this.biotexes = biotexes;
        this.colour = BioVertex.averageColour(biotexes);
    }

    /**
     * Draws this BioTriangle.
     */
    public void draw() {
        this.colour.glColour();

        glBegin(GL_TRIANGLES);
        for (Vertex vertex : this.vertexes) {
            vertex.glVertex();
        }
        glEnd();
    }

    /**
     * Returns the Colour of this BioTriangle.
     *
     * @return The Colour.
     */
    public Colour getColour() {
        return this.colour;
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The BioVertexes comprising this BioTriangle.
     */
    private BioVertex[] biotexes;

    /**
     * The colour of this BioTriangle.
     */
    private Colour colour;
}