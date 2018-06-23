package env;

import core.Logger;
import geo.Prism;
import geo.Vertex;
import util.RNG;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>Platform</b> class represents the platform beneath the landscape.</p>
 */
public class Platform implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Platform with the given geometric constraints.
     *
     * @param minX The minimum X constraint.
     * @param minY The minimum Y constraint.
     * @param minZ The minimum Z constraint.
     * @param maxX The maximum X constraint.
     * @param maxY The maximum Y constraint.
     * @param maxZ The maximum Z constraint.
     * @param rows The number of stalactite rows in this Platform.
     * @param cols The number of stalactite columns in this Platform.
     */
    public Platform(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int rows, int cols) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.rows = rows;
        this.cols = cols;

        // Compute the width of the stalactite Prisms along the X and Y dimensions.
        float dx = (maxX - minX)/cols;
        float dy = (maxY - minY)/rows;

        // The range of stalactite Prism heights.
        float range = (maxZ - minZ)*0.60f;

        this.prisms = new Prism[rows][cols];
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                // The lower X, Y, and Z corner of the stalactite Prism.
                float lowX = minX + col*dx;
                float lowY = minY + row*dy;
                float lowZ = RNG.random(minZ, minZ + range);
                Vertex low = new Vertex(lowX, lowY, lowZ);

                // The upper X, Y, and Z corner of the stalactite Prism.
                float highX = lowX + dx;
                float highY = lowY + dy;
                Vertex high = new Vertex(highX, highY, maxZ);

                Colour clr = Colour.random(Colour.Option.LIGHT);

                this.prisms[row][col] = new Prism(clr, low, high);
            }
        }
        Logger.debug("Created Platform from (%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f) with (%d x %d) stalactites.", minX, minY, minZ, maxX, maxY, maxZ, rows, cols);
    }

    /**
     * Draws this Platform.
     */
    public void draw() {
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                this.prisms[row][col].draw();
            }
        }
    }

    /**
     * Returns the number of Polygons in this Polygon.
     *
     * @return The number of Polygons
     */
    public int polygons() {
        int polygons = 0;
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                polygons += this.prisms[row][col].polygons();
            }
        }
        return polygons;
    }

    /**
     * Returns the String representation of this Platform.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f)", this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The minimum X-coordinate of this Platform.
     */
    private float minX;

    /**
     * The minimum Y-coordinate of this Platform.
     */
    private float minY;

    /**
     * The minimum Z-coordinate of this Platform.
     */
    private float minZ;

    /**
     * The minimum X-coordinate of this Platform.
     */
    private float maxX;

    /**
     * The minimum Y-coordinate of this Platform.
     */
    private float maxY;

    /**
     * The minimum Z-coordinate of this Platform.
     */
    private float maxZ;

    /**
     * The number of stalactite rows in this Platform.
     */
    private int rows;

    /**
     * The number of stalactite columns in this Platform.
     */
    private int cols;

    /**
     * The stalactite Prisms that compose this Platform.
     */
    private Prism[][] prisms;
}