package bio;

import java.util.ArrayList;
import java.util.List;

import core.Logger;
import env.Colour;
import env.Grid;
import geo.Triangle;
import geo.Vector;
import util.Algebra;

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

    ALPINE("Alpine", new Colour(0, 0, 0), 15.0f) {
        @Override
        public void texturize(Grid grid, int row, int col, float scalar) {
            BioVertex biotex = grid.getVertex(row, col);

            // Derive the set of neighbouring BioVertexes.
            int row1 = Math.max(0,                  row - 1);
            int row2 = Math.min(grid.getRows() - 1, row + 1);
            int col1 = Math.max(0,                  col - 1);
            int col2 = Math.min(grid.getRows() - 1, col + 1);

            List<BioVertex> locals = new ArrayList<>();
            for (int r = row1; r <= row2; ++r) {
                for (int c = col1; c <= col2; ++c) {
                    BioVertex curtex = grid.getVertex(r, c);
                    if (curtex != biotex) {
                        locals.add(curtex);
                    }
                }
            }

            // There should always be at least 2 neighbouring BioVertexes.
            int neighbours = locals.size();
            if (neighbours < 2) {
                Logger.error("Failed to texturize 'Alpine' BioVertex at (%d, %d): BioVertex only has %d neighbours.", row, col, neighbours);
                return;
            }

            // Determine the normal of the BioVertex by creating a Triangle out
            // of the first two neighbouring BioVertexes.
            Triangle triangle = new Triangle(biotex, locals.get(0), locals.get(1));
            Vector normal = triangle.getNormal();

            // Compute the angle between the normal of the BioVertex and the
            // vertical Vector.
            Vector vertical = new Vector(0, 0, 1);
            float angle = normal.angle(vertical);

            // Map the angle to a smooth curve.
            float right = (float) Math.PI/2;
            angle = Algebra.curve(angle/right)*right;

            float max = 1f;
            float min = 0.1f;

            // Calculate the coefficients of the quadratic function below that
            // satisfy the minimum and maximum RGB values specified above:
            //
            //                    f(x) = A*x*x + 0*x + C
            float A = -(max - min)/(right*right);
            float C = max;

            // Derive the luminance of the alpine mountain BioVertex using the
            // quadratic cofficients.
            float luminance = A*angle*angle + C;

            // Combine the luminance with each hue of the BioVertex.
            float r = Algebra.average(biotex.getColour().getRed(),   luminance, scalar);
            float g = Algebra.average(biotex.getColour().getGreen(), luminance, scalar);
            float b = Algebra.average(biotex.getColour().getBlue(),  luminance, scalar);

            // Apply the merged Colour to the BioVertex.
            Colour colour = new Colour(r, g, b);
            biotex.setColour(colour);

            biotex.shift(0.003f*scalar);
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
    MOUNTAIN("Mountain", new Colour(0.45f, 0.3f, 0), 10.0f) {
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
     * Applies the texture representing this Biome to the BioVertex located at
     * the given row and column of the specified Grid.  The extent of the texturing
     * is controlled by the provided scalar which should fall within the range
     * [0, 1].
     *
     * @param grid   The Grid containing the BioVertex.
     * @param row    The row of the BioVertex.
     * @param col    The column of the BioVertex.
     * @param scalar The magnitude of the texturing.
     */
    public void texturize(Grid grid, int row, int col, float scalar) {
        BioVertex biotex = grid.getVertex(row, col);
        this.texturize(biotex, scalar);
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