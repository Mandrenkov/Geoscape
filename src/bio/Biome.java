package bio;

import java.util.ArrayList;
import java.util.List;

import core.Logger;
import env.Colour;
import env.Grid;
import geo.Triangle;
import geo.Vector;
import util.Algebra;
import util.RNG;

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

    ALPINE("Alpine", new Colour(), new Colour(), 15.0f) {
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
    BARREN("Barren", new Colour(0.7f, 0.6f, 0.4f), new Colour(), 0.75f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.001f*scalar);

            // Determine whether this BioVertex should represent a bush.
            boolean threshold = scalar > 0.8f;
            boolean lucky = RNG.random() < 0.01;
            boolean bush = threshold && lucky;
            if (bush) {
                // Bushes are short.
                float minHeight = 0.006f;
                float maxHeight = 0.010f;
                biotex.raise(minHeight, maxHeight);

                // It is assumed that bushes have a dark green hue.
                float minR = 0.0f, maxR = 0.0f;
                float minG = 0.1f, maxG = 0.15f;
                float minB = 0.0f, maxB = 0.0f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            }
        }
    },
    DECIDUOUS("Deciduous", new Colour(0.10f, 0.25f, 0f), new Colour(), 1.0f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.001f*scalar);

            // Determine whether this BioVertex should represent a tree.
            boolean threshold = scalar > 0.5f;
            boolean lucky = RNG.random() < 0.08*scalar*scalar;
            boolean tree = threshold && lucky;
            if (tree) {
                // A range of tree heights suggest dissimilar trees.
                float minHeight = scalar*scalar*0.035f;
                float maxHeight = scalar*scalar*0.040f;
                biotex.raise(minHeight, maxHeight);

                // The leaves of a tree can vary from green to yellow to red.
                float minR = 0.0f, maxR = 0.5f;
                float minG = 0.0f, maxG = 0.7f;
                float minB = 0.0f, maxB = 0.0f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            }
        }
    },
    DESERT("Desert", new Colour(0.7f, 0.5f, 0.3f), new Colour(), 0.4f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.wave(15f, 0.035f, 20f, 0.015f*scalar);
            biotex.wave(20f, 0.025f, 330f, 0.0008f*scalar);
        }
    },
    GRASSLANDS("Grasslands", new Colour(0.35f, 0.45f, 0), new Colour(), 0.75f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.getColour().shift(0.01f*scalar);

            // Determine whether this BioVertex should represent tall grass.
            boolean threshold = scalar > 0.4f;
            boolean lucky = RNG.random() < 0.2*scalar*scalar;
            boolean tall = threshold && lucky;
            if (tall) {
                // Tall grass is actually quite short.
                float minHeight = scalar*scalar*0.01f;
                float maxHeight = scalar*scalar*0.012f;
                biotex.raise(minHeight, maxHeight);

                // The colour of the grass should be similar to the base Biome colour.
                float minR = 0.3f,  maxR = 0.3f;
                float minG = 0.4f,  maxG = 0.5f;
                float minB = 0.05f, maxB = 0.05f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            }
        }
    },
    MOUNTAIN("Mountain", new Colour(0.2f, 0.1f, 0), new Colour(), 10.0f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            float range = 0.012f*scalar*biotex.getZ();
            biotex.shift(range);
            biotex.getColour().shift(0.02f*scalar);
        }
    },
    PRAIRIE("Prairie", new Colour(0.55f, 0.50f, 0), new Colour(), 0.75f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.getColour().shift(0.01f*scalar);

            // Determine whether this BioVertex should represent wheat.
            boolean threshold = scalar > 0.4f;
            boolean lucky = RNG.random() < 0.35*scalar*scalar;
            boolean wheat = threshold && lucky;
            if (wheat) {
                // Wheat plants are effectively the same height.
                float minHeight = scalar*scalar*0.018f;
                float maxHeight = scalar*scalar*0.019f;
                biotex.raise(minHeight, maxHeight);

                // The colour of wheat is a little richer than the base Biome colour.
                float minR = 0.77f, maxR = 0.77f;
                float minG = 0.70f, maxG = 0.70f;
                float minB = 0.00f, maxB = 0.00f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            }
        }
    },
    TAIGA("Taiga", new Colour(0.15f, 0.2f, 0), new Colour(), 2f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            // Determine whether this BioVertex should represent a tree.
            boolean threshold = scalar > 0.6f;
            boolean lucky = RNG.random() < 0.12*Math.pow(scalar, 4);
            boolean above = biotex.getZ() > 0.03f;
            boolean tree = threshold && lucky && above;
            if (tree) {
                // Similar tree heights give the illusion of a canopy.
                float minHeight = 0.050f;
                float maxHeight = 0.055f;
                biotex.raise(minHeight, maxHeight);

                // Evergreen trees are virtually all hunter green.
                float minR = 0.30f, maxR = 0.35f;
                float minG = 0.35f, maxG = 0.50f;
                float minB = 0.0f,  maxB = 0.0f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            }
        }
    },
    TROPICAL("Tropical", new Colour(0.1f, 0.2f, 0), new Colour(), 0.4f) {
        @Override
        public void texturize(BioVertex biotex, float scalar) {
            biotex.shift(0.001f*scalar);

            // Determine whether this BioVertex should represent a tree, a puddle,
            // or neither.
            boolean threshold = scalar > 0.4f;
            boolean treeLuck = RNG.random() < 0.2*scalar*scalar;
            boolean tree = threshold && treeLuck;
            boolean puddleLuck = RNG.random() < 0.05*scalar*scalar;
            boolean puddle = threshold && puddleLuck;
            if (tree) {
                // Close tree heights eliminate sharp edges from the canopy.
                float minHeight = scalar*scalar*0.035f;
                float maxHeight = scalar*scalar*0.040f;
                biotex.raise(minHeight, maxHeight);

                // The trunks and branches of trees are assumed to be hidden by
                // their leaves.
                float minR = 0.0f, maxR = 0.0f;
                float minG = 0.0f, maxG = 0.5f;
                float minB = 0.0f, maxB = 0.0f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            } else if (puddle) {
                // Naturally, puddles are predominantly blue.
                float minR = 0.0f, maxR = 0.0f;
                float minG = 0.1f, maxG = 0.3f;
                float minB = 0.5f, maxB = 1.0f;
                Colour colour = Colour.random(minR, maxR, minG, maxG, minB, maxB);
                biotex.setColour(colour);
            }
        }
    },
    WATER("Water", new Colour(0, 0.5f, 1), new Colour(1f, 1f, 1f), 0.001f) {
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
    VOID("Void", new Colour(0.2f, 0.2f, 0.2f), new Colour(), 0) {
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
     * Returns the specular highlight of this Biome.
     *
     * @return The highlight.
     */
    public Colour getHighlight() {
        return this.highlight;
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
     * The specular highlight of this Biome.
     */
    private Colour highlight;

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
     * @param name      The name of this Biome.
     * @param colour    The base colour of this Biome.
     * @param highlight The specular highlight of this Biome.
     * @param scale     The elevation scaling factor of this Biome.
     */
    private Biome(String name, Colour colour, Colour highlight, float scale) {
        this.name = name;
        this.colour = colour;
        this.highlight = highlight;
        this.scale = scale;
    }
}