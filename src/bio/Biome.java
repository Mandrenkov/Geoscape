package bio;

import core.Logger;
import env.Colour;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>Biome</b> enumerations represent various natural biomes.  In the
 * context of this application, a Biome is a set of common characteristics that
 * is shared by a group of BioVertexes.</p>
 */
public enum Biome {
    
    // Enumerations
    // -------------------------------------------------------------------------

    ALPINE("Alpine", new Colour(0.6f, 0.6f, 0.6f), 15.0f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.005f*scalar);
            biotex.getColour().shift(0.1f*scalar);
        }
    },
    BARREN("Barren", new Colour(0.7f, 0.6f, 0.4f), 0.75f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.001f*scalar);
        }
    },
    DECIDUOUS("Deciduous", new Colour(0.4f, 0.5f, 0f), 1.0f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.001f*scalar);
        }
    },
    DESERT("Desert", new Colour(0.7f, 0.5f, 0.3f), 0.4f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.wave(20f, 0.02f, 200f, 0.002f*scalar);
        }
    },
    GRASSLANDS("Grasslands", new Colour(0.35f, 0.45f, 0), 0.75f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.getColour().shift(0.01f*scalar);
        }
    },
    MOUNTAIN("Mountain", new Colour(0.45f, 0.3f, 0), 15.0f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.005f*scalar);
            biotex.getColour().shift(0.05f*scalar);
        }
    },
    SHRUBLANDS("Shrublands", new Colour(0.3f, 0.4f, 0.25f), 2f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.getColour().shift(0.01f*scalar);
        }
    },
    TAIGA("Taiga", new Colour(0.8f, 1f, 1f), 2f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            // Do nothing.
        }
    },
    TROPICAL("Tropical", new Colour(0.0f, 0.4f, 0), 0.4f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.001f*scalar);
        }
    },
    WATER("Water", new Colour(0, 0.5f, 1), 0.001f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.getColour().shift(0.02f*scalar);
        }

        @Override
        public void update(BioVertex biotex, double time, float scalar) {
            // Apply an offset to the BioVertex to create the illusion of waves.
            // The vertical displacement can be described by the equation:
            //     dz = <Scalar>*<Height>*sin(<Speed>*<Time> + <Density>*<X>)
            float height = biotex.getZ()/4;
            float speed = 4;
            float density = 20;

            float dz = (float) Math.sin(speed*time + density*biotex.getX())*height*scalar;
            biotex.setOffset(0, 0, dz);
        }
    },
    VOID("Void", new Colour(0.2f, 0.2f, 0.2f), 0) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            // Tie the BioVertex elevation to 0.
            float z = biotex.getZ();
            z *= 1 - scalar;
            biotex.setZ(z);
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
     * Applies the texture representing this Biome to the given BioVertex.
     * The extent of the texturing is controlled by the given scalar which
     * should fall within the range [0, 1].
     *
     * @param biotex The BioVertex to texturize.
     * @param scalar The magnitude of the texturing.
     */
    public void texturize(BioVertex biotex, float scalar) {
        Logger.warn("Biome \"%s\" does not implement Biome::texturize().", this.name);
    }

    /**
     * Updates the given BioVertex according to the given time.  By default, no
     * changes are applied to the BioVertex.  The extent of the update is
     * controlled by the given scalar which should fall within the range [0, 1].
     * 
     * @param biotex The BioVertex to update.
     * @param time   The uptime of the application (in seconds).
     * @param scalar The magnitude of the update.
     */
    public void update(BioVertex biotex, double time, float scalar) {
        // Do nothing.
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