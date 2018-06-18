package env;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import bio.BioVertex;
import bio.BioTriangle;
import bio.BioMap;
import bio.Biome;
import core.Logger;
import util.Pair;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>Grid</b> class represents the surface of a landscape.</p>
 */
public class Grid implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Grid with the given name, rows, columns, coordinate bounds,
     * and BioMap.
     *
     * @param name   The name of this Grid.
     * @param rows   The number of rows in this Grid.
     * @param cols   The number of columns in this Grid.
     * @param initZ  The initial elevation of all BioVertexes in this Grid.
     * @param minX   The minimum X-coordinate of this Grid.
     * @param minY   The minimum Y-coordinate of this Grid.
     * @param maxX   The maximum X-coordinate of this Grid.
     * @param maxY   The maximum Y-coordinate of this Grid.
     * @param biomap The BioMap representing the Biomes imposed on this Grid.
     */
    public Grid(String name, int rows, int cols, float initZ, float minX, float minY, float maxX, float maxY, BioMap biomap) {
        Logger.debug("Creating Grid \"%s\" with %d rows and %d columns from (%.2f, %.2f) to (%.2f, %.2f).", name, rows, cols, minX, minY, maxX, maxY);

        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.biotexes = new BioVertex[this.rows][this.cols];
        this.biogles = new ArrayList<>();
        this.indexes = new ArrayList<>(rows*cols);

        // Initialize the BioVertexes in this Grid.
        for (int row = 0 ; row < this.rows; ++row) {
            for (int col = 0 ; col < this.cols ; ++col) {
                this.indexes.add(new Pair<>(row, col));

                float x = this.minX + col*(this.maxX - this.minX)/(this.cols - 1);
                float y = this.minY + row*(this.maxY - this.minY)/(this.rows - 1);
                float z = initZ;
                Biome biome = biomap.getBiome(row, col);

                BioVertex biotex = new BioVertex(biome, x, y, z);
                this.biotexes[row][col] = biotex;
            }
        }

        // Initialize the BioTriangles in this Grid.
        for (int row = 0; row < this.rows - 1; ++row) {
            for (int p = 0; p < 2*this.cols - 2; ++p) {
                int col = p/2;
                boolean forward = p % 2 == 0;

                BioVertex[] biogle = new BioVertex[3];
                biogle[0] = forward ? this.biotexes[row    ][col    ] : this.biotexes[row + 1][col    ];
                biogle[1] = forward ? this.biotexes[row + 1][col    ] : this.biotexes[row + 1][col + 1];
                biogle[2] = forward ? this.biotexes[row    ][col + 1] : this.biotexes[row    ][col + 1];
                this.biogles.add(new BioTriangle(biogle));
            }
        }

        Noiseform noiseform = new Noiseform(this, 8, 8);
        noiseform.apply();
    }

    /**
     * Draws this Grid.
     */
    public void draw() {
        // By default, the BioTriangles that compose this Grid are pretty shiny.
        int shininess = 100;
        glMateriali(GL_FRONT, GL_SHININESS, shininess);

        for (int row = 0; row < this.rows - 1; ++row) {
            // GL_TRIANGLE_STRIP expects the Vertices to be ordered as follows
            // in the OpenGL buffer:
            //
            //  0 --- 2 --- 4 --- 6
            //  |   / |   / |   / |
            //  |  /  |  /  |  /  |
            //  | /   | /   | /   |
            //  1 --- 3 --- 4 --- 5
            glBegin(GL_TRIANGLE_STRIP);
            this.biotexes[row][0].glVertex();
            this.biotexes[row + 1][0].glVertex();
            for (int v = 2; v < 2*this.cols; ++v) {
                // Each row of the Grid contains 2 Biogles for every column in
                // the Grid (except for the last column).
                int t = row*2*(this.cols - 1) + (v - 2);
                BioTriangle biogle = this.biogles.get(t);

                // Apply the colour of the BioVertex and update the OpenGL normal.
                biogle.getColour().glColour();
                biogle.getNormal().glNormal();

                // Apply the specular highlights of the BioTriangle.
                float[] highlight = biogle.getHighlight().toArray();
                glMaterialfv(GL_FRONT, GL_SPECULAR, highlight);

                // The row and column equations for the current Vertex can be
                // derived by studying the ASCII depiction of the BioTriangles
                // above. 
                int r = row + (v % 2);
                int c = v/2;
                this.biotexes[r][c].glVertex();
            }
            glEnd();
        }

        // Restore the specular highlight settings.
        glMaterialfv(GL_FRONT, GL_SPECULAR, Colour.GL_BLACK);
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
     * Returns the BioVertex in this Grid located at the given position.
     *
     * @param row The row of this Grid containing the desired BioVertex.
     * @param col The column of this Grid containing the desired BioVertex.
     *
     * @return The BioVertex located at the given position.
     */
    public BioVertex getVertex(int row, int col) {
        return this.biotexes[row][col];
    }

    /**
     * Returns the BioTriangles comprising this Grid.
     *
     * @return The BioTriangles.
     */
    public ArrayList<BioTriangle> getTriangles() {
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

    public ArrayList<Pair<Integer, Integer>> getIndexes() {
        return this.indexes;
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
     * Updates all the BioVertexes in this Grid using the given time.
     * 
     * @param time   The uptime of the application (in seconds).
     */
    public void update(double time) {
        indexes.parallelStream().forEach(pair -> {
            int row = pair.getFirst();
            int col = pair.getSecond();

            BioVertex biotex = this.biotexes[row][col];
            Biome biome = biotex.getBiome();
            biome.update(biotex, time, 1f);
        });
    }

    /**
     * Returns a String representation of this Grid.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("Grid \"%s\" (%.2f, %.2f) to (%.2f, %.2f)", this.name, this.minX, this.minY, this.maxX, this.maxY);
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
     * The list of indexes in this Grid.
     */
    private ArrayList<Pair<Integer, Integer>> indexes;

    /**
     * The matrix of BioVertexes that comprise the BioTriangles of this Grid.
     */
    private BioVertex[][] biotexes;

    /**
     * The BioTriangles that comprise this Grid.
     */
    private ArrayList<BioTriangle> biogles;
}