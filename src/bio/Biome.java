package bio;

import core.Logger;
import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Biome</b> enumerations represent various natural biomes.  In the
 * context of this application, a Biome is a set of common characteristics that
 * is shared by a group of BioVertexes.</p>
 */
public enum Biome {
    
    // Enumerations
    // -------------------------------------------------------------------------

    DESERT("Desert", new Colour(0.600f, 0.300f, 0.000f), 0.05f) {
        public void texturize(BioVertex biotex, float scalar) {
            biotex.wave(20f, 0.02f, 200f, 0.001f*scalar);
        }
    },
    HILL("Hill", new Colour(0.2f, 0.4f, 0.0f), 2.0f) {
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.0005f*scalar);
            //biotex.getColour().shift(0.01f*scalar);
        }
    },
    MOUNTAIN("Mountain", new Colour(0.150f, 0.100f, 0.000f), 15.0f) {
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.005f*scalar);
            //biotex.getColour().scale(1f + biotex.getZ()*5);
        }
    },
    PLAINS("Plains", new Colour(0.8f, 1.0f, 0.0f), 0.02f) {
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.0001f*scalar);
            biotex.getColour().shift(0.01f*scalar);
        }
    },
    TUNDRA("Tundra", new Colour(1, 1, 1), 0.10f) {
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.0001f*scalar);
        }
    };


    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns the colour of this Biome.
     *
     * @return The colour.
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Returns the name of this Biome.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the elevation scale of this Biome.
     *
     * @return The elevation scale.
     */
    public float getScale() {
        return this.scale;
    }

    /**
     * @brief Applies the texture representing this Biome to the given BioVertex.
     *        The extent of the texturing is controlled by the given scalar which
     *        should fall within the range [0, 1].
     *
     * @param biotex The BioVertex to texturize.
     * @param scalar The magnitude of the texturing.
     */
    public void texturize(BioVertex biotex, float scalar) {
        Logger.warn("Biome \"%s\" does not implement Biome::texturize().", this.name);
    }

    /**
     * Returns a String representation of this Biome.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("%s [Colour: %s, Scale: %.2f]", this.name, this.colour.toString(), this.scale);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The colour of this Biome.
     */
    private Colour colour;

    /**
     * The name of this Biome.
     */
    private String name;

    /**
     * The elevation scale of this Biome.
     */
    private float scale;

    /**
     * Constructs a Biome object with the specified parameters.
     *
     * @param name   The name of this Biome.
     * @param colour The base colour of this Biome.
     * @param scale  The elevation scaling factor of this Biome.
     */
    private Biome(String name, Colour colour, float scale) {
        this.name = name;
        this.colour = colour;
        this.scale = scale;
    }
}