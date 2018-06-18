package bio;

import static org.lwjgl.opengl.GL11.*;

import java.util.function.Function;
import java.util.stream.Stream;

import env.Colour;
import geo.Triangle;
import geo.Vertex;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
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

        // Returns the highlight of the given BioVertex.
        Function<BioVertex, Colour> getHighlight = biotex -> biotex.getBiome().getHighlight();

        // The highlight of the BioTriangle is the average highlights of its
        // constituent BioVertexes.
        this.highlight = Colour.average(Stream.of(biotexes)
                                              .map(getHighlight)
                                              .toArray(Colour[]::new));
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

    /**
     * Returns the specular highlight of this BioTriangle.
     *
     * @return The highlight.
     */
    public Colour getHighlight() {
        return this.highlight;
    }

    /**
     * Sets the Colour of this BioTriangle to the given colour.
     *
     * @param colour The new Colour of the BioTriangle.
     */
    public void setColour(Colour colour) {
        for (BioVertex biotex : this.biotexes) {
            biotex.setColour(colour);
        }
    }

    /**
     * Updates the colour of this BioTriangle to reflect the current Colours of
     * its BioVertexes.
     */
    public void updateColour() {
        this.colour = BioVertex.averageColour(this.biotexes);
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

    /**
     * The specular highlight of this BioTriangle.
     */
    private Colour highlight;
}