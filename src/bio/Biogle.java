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
 * <p>The <b>Biogle</b> class represents a Biome Triangle.</p>
 */
public class Biogle extends Triangle {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Biogle using the given Biotexes.
     *
     * @param biotexes The Biotexes comprising this Biogle.
     */
    public Biogle(Biotex... biotexes) {
        super((Vertex[]) biotexes);
        this.biotexes = biotexes;
        this.colour = Biotex.averageColour(biotexes);
    }

    /**
     * Draws this Biogle.
     */
    public void draw() {
        this.colour.gl();

        glBegin(GL_TRIANGLES);
        for (Vertex vertex : this.vertexes) {
            vertex.gl();
        }
        glEnd();
    }

    /**
     * Returns the Colour of this Biogle.
     *
     * @return The Colour.
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Illuminates this Biogle with respect to the given Lights.
     *
     * @param lights The Lights illuminating this Biogle.
     */
    public void illuminate(Light... lights) {
        this.colour = Biotex.averageColour(this.biotexes);

        // Scale the colour using the elevation of the Biogle.
        // The 10x multiplier is completely arbitrary.
        float scaleZ = 10*Vertex.averageZ(this.vertexes);

        // Cache the middle Vertex and normal of this Biogle.
        Vertex middle = this.getMiddle();
        Vector normal = this.getNormal();

        // Scale the Colour using Lambert's cosine law.
        float scaleLight = 0;
        for (Light light : lights) {
            Vector los = new Vector(middle, light.getPosition());
            float angle = normal.angle(los);

            // Assume the Light source initially emits 3200 lumens.
            //int lumens = (int) (10000/Math.pow(los.magnitude(), 3));

            // An angle approaching k*PI achieves a maximum brightness factor.
            // An angle approaching k*PI/2 achieves a minimum brightness factor.
            float cosine = Math.max(0, (float) (Math.cos(angle) + 1f)/2f);
            scaleLight += cosine*cosine*cosine;
        }

        scaleLight *= scaleZ;

        this.colour.scale(scaleLight);
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The Biotexes comprising this Biogle.
     */
    private Biotex[] biotexes;

    /**
     * The colour of this Biogle.
     */
    private Colour colour;
}