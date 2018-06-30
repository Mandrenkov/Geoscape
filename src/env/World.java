package env;

import java.util.ArrayList;
import java.util.Arrays;

import bio.BioMap;
import bio.BioVertex;
import core.Top;
import geo.Line;
import geo.Vertex;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <i>World</i> class represents a virtual world.  Specifically, this class
 * holds the Drawable entities of such a world.</p>
 */
public class World implements Drawable {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a World with the given name and coordinate bounds.
     *
     * @param name   The name of this World.
     * @param minX   The minimum X-coordinate of this World.
     * @param minY   The minimum Y-coordinate of this World.
     * @param maxX   The maximum X-coordinate of this World.
     * @param maxY   The maximum Y-coordinate of this World.
     */
    public World(String name, float minX, float minY, float maxX, float maxY) {
        this.name = name;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.drawables = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.grids = new ArrayList<>();

        // Add a set of axes and a grid to the World for debugging purposes.
        if (Top.DEBUG) {
            this.addDebugAxes();
            this.addDebugGrid();
        }
    }

    /**
     * Adds the given Drawables to this World.
     *
     * @param drawables The Drawables to add.
     */
    public void add(Drawable... drawables) {
        this.drawables.addAll(Arrays.asList(drawables));
    }

    /**
     * Adds the given Grid to this World.
     *
     * @param grid The Grid to add.
     */
    public void addGrids(Grid... grids) {
        this.grids.addAll(Arrays.asList(grids));
        this.add(grids);
    }

    /**
     * Adds the given Light source to this World.
     *
     * @param light The Light to add.
     */
    public void addLights(Light... lights) {
        this.lights.addAll(Arrays.asList(lights));
        this.add(lights);
    }

    /**
     * Draws this World.
     */
    public void draw() {
        for (Drawable drawable : this.drawables) {
            drawable.draw();
        }
    }

    /**
     * Returns the number of Polygons in this World.
     *
     * @return The number of Polygons.
     */
    public int polygons() {
        return drawables.stream()
                        .mapToInt(drawable -> drawable.polygons())
                        .sum();
    }

    /**
     * Returns the list of Lights in this World.
     * 
     * @return The Lights.
     */
    public ArrayList<Light> getLights() {
        return this.lights;
    }

    /**
     * Returns the minimum X-coordinate of this World.
     *
     * @return The minimum X-coordinate.
     */
    public float getMinX() {
        return this.minX;
    }

    /**
     * Returns the minimum Y-coordinate of this World.
     *
     * @return The minimum Y-coordinate.
     */
    public float getMinY() {
        return this.minY;
    }

    /**
     * Returns the maximum X-coordinate of this World.
     *
     * @return The maximum X-coordinate.
     */
    public float getMaxX() {
        return this.maxX;
    }

    /**
     * Returns the maximum Y-coordinate of this World.
     *
     * @return The maximum Y-coordinate.
     */
    public float getMaxY() {
        return this.maxY;
    }

    /**
     * Returns a String representation of this World.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("World \"%s\" (%d polygons)", this.name, this.polygons());
    }

    /**
     * Updates all the Grids in this World using the given time.
     * 
     * @param time The uptime of the application (in seconds).
     */
    public void update(double time) {
        for (Grid grid : this.grids) {
            grid.update(time);
        }
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The name of this World.
     */
    private String name;

    /**
     * The minimum X-coordinate of this World.
     */
    private float minX;

    /**
     * The minimum Y-coordinate of this World.
     */
    private float minY;

    /**
     * The maximum X-coordinate of this World.
     */
    private float maxX;

    /**
     * The maximum Y-coordinate of this World.
     */
    private float maxY;

    /**
     * The list of Drawable entities in this World.
     */
    private ArrayList<Drawable> drawables;

    /**
     * The list of Grids in this World.
     */
    private ArrayList<Grid> grids;

    /**
     * The list of Light sources in this World.
     */
    private ArrayList<Light> lights;

    /**
     * Adds the X, Y, and Z debug axes to the Drawable list of this World.
     */
    private void addDebugAxes() {
        float length = 1.1f;
        Line xAxis = new Line(Vertex.ORIGIN, new Vertex(length, 0, 0, new Colour(1, 0, 0)));
        Line yAxis = new Line(Vertex.ORIGIN, new Vertex(0, length, 0, new Colour(0, 1, 0)));
        Line zAxis = new Line(Vertex.ORIGIN, new Vertex(0, 0, length, new Colour(0, 0, 1)));
        this.drawables.addAll(Arrays.asList(xAxis, yAxis, zAxis));
    }

    /**
     * Adds the debug grid to the Drawable list of this World.
     */
    private void addDebugGrid() {
        // The overhang of the grid in each direction of the World.
        float overhang = 1;
        // The number of rows and columns in the grid.
        int size = 20;

        BioMap biomap = new BioMap(size, size);
        Grid grid = new Grid("Debug",
                             size,
                             size,
                             0,
                             this.minX - overhang,
                             this.minY - overhang,
                             this.maxX + overhang,
                             this.maxY + overhang,
                             biomap);

        Colour colour = new Colour(0.3f);
        for (int row = 0; row < size; ++row){
            for (int col = 0; col < size; ++col) {
                BioVertex biotex = grid.getVertex(row, col);
                biotex.setColour(colour);

                Line h = new Line(grid.getVertex(row, 0), grid.getVertex(row, size - 1));
                Line v = new Line(grid.getVertex(0, col), grid.getVertex(size - 1, col));
                this.drawables.addAll(Arrays.asList(h, v));
            }
        }
    }
}