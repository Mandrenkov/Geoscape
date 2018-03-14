package bio;

import java.util.Arrays;

import env.Colour;
import geo.Vertex;
import util.Pair;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Biotex</b> class represents a Biome Vertex.</p>
 */
public class Biotex extends Vertex {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns the average Colour of the given Biotexes.
     *
     * @param biotexes The Biotexes to average.
     *
     * @return The average Colour.
     */
    public static Colour averageColour(Biotex... biotexes) {
        Colour[] colourArray = Arrays.stream(biotexes)
                                     .map(biotex -> biotex.getColour())
                                     .toArray(Colour[]::new);
        return Colour.average(colourArray);
    }

    /**
     * Constructs a Biotex representing the given 3D coordinate.
     *
     * @param x The X-coordinate of this Biotex.
     * @param y The Y-coordinate of this Biotex.
     * @param z The Z-coordinate of this Biotex.
     */
    public Biotex(Biome biome, float x, float y, float z) {
        super(x, y, z);
        this.biome = biome;

        this.colour = new Colour(biome.getColour());
        this.biomix = new Biomix();
        this.biomix.add(biome, 1f);
    }

    /**
     * Returns the primary Biome associated with this Biotex.
     *
     * @return The Biome.
     */
    public Biome getBiome() {
        return this.biome;
    }

    /**
     * Returns the Biomix associated with this Biotex.
     *
     * @return The Biomix.
     */
    public Biomix getBiomix() {
        return this.biomix;
    }

    /**
     * Returns the Colour of this Biotex.
     *
     * @return The Colour.
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Sets the Biomix of this Biotex to the given Biomix.
     *
     * @param biomix The new Biomix of this Biotex.
     */
    public void setBiomix(Biomix biomix) {
        this.biomix = biomix;
    }

    /**
     * Sets the Colour of this Biotex to the given Colour.
     *
     * @param colour The new Colour of this Biotex.
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * Applies the textures of the Biomes that influence this Biotex.
     */
    public void texturize() {
        for (Pair<Biome, Float> biomePair : this.biomix) {
            Biome biome = biomePair.getFirst();
            float scalar = (float) biomePair.getSecond();
            biome.texturize(this, scalar);
        }
    }

    /**
     * Shifts the elevation of this Biotex according to its position relative to
     * the wave pattern specified by the given parameters.
     *
     * @param frequency The frequency of the reference wave.
     * @param amplitude The amplitude of the reference wave.
     * @param density   The density of the wave pattern.
     * @param height    The magnitude of the shift in the elevation of this Biotex.
     */
    public void wave(float frequency, float amplitude, float density, float height) {
        // Compute the amplitude of the reference wave along the Y-axis.
        double refWave = amplitude*Math.cos(this.getY()*frequency);
        // Determine the distance along the X-axis from this Biotex to the reference wave.
        double deltaX = density*Math.abs(this.getX() - refWave);
        // Calculate the difference as a periodic function of the difference along the X-axis.
        float deltaZ = (float) Math.cos(deltaX)*height;
        this.z += deltaZ;
    }

    /**
     * Returns a String representation of this Biotex.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("Biotex (%.2f, %.2f, %.2f) with %s", this.x, this.y, this.z, this.colour);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The original Biome of this Biotex.
     */
    private Biome biome;

    /**
     * The Biome influences on this Biotex.
     */
    private Biomix biomix;

    /**
     * The colour of this Biotex.
     */
    private Colour colour;
}