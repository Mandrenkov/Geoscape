package bio;

import java.util.Arrays;
import env.Colour;
import geo.Vertex;
import util.Pair;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>BioVertex</b> class represents a Biome Vertex.</p>
 */
public class BioVertex extends Vertex {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns the average Colour of the given BioVertexes.
     *
     * @param BioVertexes The BioVertexes to average.
     *
     * @return The average Colour.
     */
    public static Colour averageColour(BioVertex... BioVertexes) {
        Colour[] colourArray = Arrays.stream(BioVertexes)
                                     .map(BioVertex -> BioVertex.getColour())
                                     .toArray(Colour[]::new);
        return Colour.average(colourArray);
    }

    /**
     * Constructs a BioVertex representing the given 3D coordinate.
     *
     * @param x The X-coordinate of this BioVertex.
     * @param y The Y-coordinate of this BioVertex.
     * @param z The Z-coordinate of this BioVertex.
     */
    public BioVertex(Biome biome, float x, float y, float z) {
        super(x, y, z);
        this.biome = biome;

        this.offset = new Vertex(0, 0, 0);

        this.colour = new Colour(biome.getColour());
        this.biomix = new Biomix();
        this.biomix.add(biome, 1f);
    }

    /**
     * Constructs a BioVertex that is a copy of the given BioVertex.
     *
     * @param biotex The BioVertex to copy.
     */
    public BioVertex(BioVertex biotex) {
        this(biotex.biome, biotex.x, biotex.y, biotex.z);
    }

    /**
     * Returns the primary Biome associated with this BioVertex.
     *
     * @return The Biome.
     */
    public Biome getBiome() {
        return this.biome;
    }

    /**
     * Returns the Biomix associated with this BioVertex.
     *
     * @return The Biomix.
     */
    public Biomix getBiomix() {
        return this.biomix;
    }

    /**
     * Returns the Colour of this BioVertex.
     *
     * @return The Colour.
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Adds this BioVertex to the GL buffer.  The position of this BioVertex
     * will be offset prior to the call to glVertex() and then subsequently
     * restored to its original position.
     */
    public void glVertex() {
        this.translate(this.offset.getX(), this.offset.getY(), this.offset.getZ());
        super.glVertex();
        this.translate(-this.offset.getX(), -this.offset.getY(), -this.offset.getZ());
    }

    /**
     * Sets the Biomix of this BioVertex to the given Biomix.
     *
     * @param biomix The new Biomix of this BioVertex.
     */
    public void setBiomix(Biomix biomix) {
        this.biomix = biomix;
    }

    /**
     * Sets the Colour of this BioVertex to the given Colour.
     *
     * @param colour The new Colour of this BioVertex.
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * Sets the X-coordinate, Y-coordinate, and Z-coordinate offsets of this
     * BioVertex.
     * 
     * @param x The offset of the X-coordinate.
     * @param y The offset of the Y-coordinate.
     * @param z The offset of the Z-coordinate.
     */
    public void setOffset(float x, float y, float z) {
        this.offset = new Vertex(x, y, z);
    }

    /**
     * Applies the textures of the Biomes that influence this BioVertex.
     */
    public void texturize() {
        for (Pair<Biome, Float> biomePair : this.biomix) {
            Biome biome = biomePair.getFirst();
            float scalar = (float) biomePair.getSecond();
            biome.texturize(this, scalar);
        }
    }

    /**
     * Shifts the elevation of this BioVertex according to its position relative to
     * the wave pattern specified by the given parameters.
     *
     * @param frequency The frequency of the reference wave.
     * @param amplitude The amplitude of the reference wave.
     * @param density   The density of the wave pattern.
     * @param height    The magnitude of the shift in the elevation of this BioVertex.
     */
    public void wave(float frequency, float amplitude, float density, float height) {
        // Compute the amplitude of the reference wave along the Y-axis.
        double refWave = amplitude*Math.cos(this.getY()*frequency);
        // Determine the distance along the X-axis from this BioVertex to the reference wave.
        double deltaX = density*Math.abs(this.getX() - refWave);
        // Calculate the difference as a periodic function of the difference along the X-axis.
        float deltaZ = (float) Math.cos(deltaX)*height;
        this.z += deltaZ;
    }

    /**
     * Returns a String representation of this BioVertex.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("BioVertex (%.2f, %.2f, %.2f) with %s", this.x, this.y, this.z, this.colour);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The original Biome of this BioVertex.
     */
    private Biome biome;

    /**
     * The Biome influences on this BioVertex.
     */
    private Biomix biomix;

    /**
     * The rendering offset of this BioVertex.
     */
    private Vertex offset;
}