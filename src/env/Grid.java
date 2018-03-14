package env;

import java.util.ArrayList;

import bio.Biotex;
import bio.Biogle;
import bio.Biomap;
import bio.Biome;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Grid</b> class represents the surface of a landscape.</p>
 */
public class Grid implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Grid with the given name, rows, columns, coordinate bounds,
     * and Biomap.
     *
     * @param name   The name of this Grid.
     * @param rows   The number of rows in this Grid.
     * @param cols   The number of columns in this Grid.
     * @param minX   The minimum X-coordinate of this Grid.
     * @param minY   The minimum Y-coordinate of this Grid.
     * @param maxX   The maximum X-coordinate of this Grid.
     * @param maxY   The maximum Y-coordinate of this Grid.
     * @param biomap The Biomap representing the Biomes imposed on this Grid.
     */
    public Grid(String name, int rows, int cols, float minX, float minY, float maxX, float maxY, Biomap biomap) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.biotexes = new Biotex[this.rows][this.cols];
        this.biogles = new ArrayList<>();

        // Initialize the Biotexes in this Grid.
        for (int row = 0 ; row < this.rows; ++row) {
            for (int col = 0 ; col < this.cols ; ++col) {
                float x = this.minX + col*(this.maxX - this.minX)/(this.cols - 1);
                float y = this.minY + row*(this.maxY - this.minY)/(this.rows - 1);
                float z = 0.1f;
                Biome biome = biomap.getBiome(row, col);

                Biotex biotex = new Biotex(biome, x, y, z);
                this.biotexes[row][col] = biotex;
            }
        }

        // Initialize the Biogles in this Grid.
        for (int row = 0; row < this.rows - 1; ++row) {
            for (int p = 0; p < 2*this.cols - 2; ++p) {
                int col = p/2;
                boolean forward = p % 2 == 0;

                Biotex[] biogle = new Biotex[3];
                biogle[0] = forward ? this.biotexes[row][col]     : this.biotexes[row + 1][col + 1];
                biogle[1] = forward ? this.biotexes[row + 1][col] : this.biotexes[row][col + 1];
                biogle[2] = forward ? this.biotexes[row][col + 1] : this.biotexes[row + 1][col];
                this.biogles.add(new Biogle(biogle));
            }
        }

        Noiseform noiseform = new Noiseform(this, 10, 10);
        noiseform.apply();
    }

    /**
     * Draws this Grid.
     */
    public void draw() {
        for (Biogle biogle : this.biogles) {
            biogle.draw();
        }
    }

    /**
     * Returns the number of Polygons in this Polygon.
     *
     * @return The number of Polygons
     */
    public int polygons() {
        return this.biogles.size();
    }

    /**
     * Returns the number of columns in this Grid.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return this.cols;
    }

    /**
     * Returns the number of rows in this Grid.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Returns the Biotex in this Grid located at the given position.
     *
     * @param row The row of this Grid containing the desired Biotex.
     * @param col The column of this Grid containing the desired Biotex.
     *
     * @return The Biotex located at the given position.
     */
    public Biotex getBiotex(int row, int col) {
        return this.biotexes[row][col];
    }

    /**
     * Returns the Biogles comprising this Grid.
     *
     * @return The Biogles.
     */
    public ArrayList<Biogle> getBiogles() {
        return this.biogles;
    }

    /**
     * Returns the minimum X-coordinate of this Grid.
     *
     * @return The minimum X-coordinate.
     */
    public float getMinX() {
        return this.minX;
    }

    /**
     * Returns the minimum Y-coordinate of this Grid.
     *
     * @return The minimum Y-coordinate.
     */
    public float getMinY() {
        return this.minY;
    }

    /**
     * Returns the maximum X-coordinate of this Grid.
     *
     * @return The maximum X-coordinate.
     */
    public float getMaxX() {
        return this.maxX;
    }

    /**
     * Returns the maximum Y-coordinate of this Grid.
     *
     * @return The maximum Y-coordinate.
     */
    public float getMaxY() {
        return this.maxY;
    }

    /**
     * Returns the width of a cell in this Grid.
     *
     * @return The width.
     */
    public float getCellWidth() {
        return this.getWidth()/this.cols;
    }

    /**
     * Returns the height of a cell in this Grid.
     *
     * @return The height.
     */
    public float getCellHeight() {
        return this.getHeight()/this.rows;
    }

    /**
     * Returns the width of this Grid.
     *
     * @return The width.
     */
    public float getWidth() {
        return this.maxX - this.minX;
    }

    /**
     * Returns the height of this Grid.
     *
     * @return The height.
     */
    public float getHeight() {
        return this.maxY - this.minY;
    }

    /**
     * Returns a String representation of this Grid.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("%s [(%.2f, %.2f) to (%.2f, %.2f)]", this.name, this.minX, this.minY, this.maxX, this.maxY);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The name of this Grid.
     */
    private String name;

    /**
     * The number of rows in this Grid.
     */
    private int rows;

    /**
     * The number of columns in this Grid.
     */
    private int cols;

    /**
     * The minimum X-coordinate of this Grid.
     */
    private float minX;

    /**
     * The minimum Y-coordinate of this Grid.
     */
    private float minY;

    /**
     * The maximum X-coordinate of this Grid.
     */
    private float maxX;

    /**
     * The maximum Y-coordinate of this Grid.
     */
    private float maxY;

    /**
     * The matrix of Biotexes that comprise the Biogles of this Grid.
     */
    private Biotex[][] biotexes;

    /**
     * The Biogles that comprise this Grid.
     */
    private ArrayList<Biogle> biogles;
}