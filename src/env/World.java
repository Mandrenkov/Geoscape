package env;

import java.util.ArrayList;
import java.util.Arrays;

import core.Top;
import geo.Line;
import geo.Vertex;
import util.Pair;

/**
 * @author  Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
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

        // Add a set of axes and a grid to the World for debugging purposes.
        if (Top.DEBUG) {
            // Draw the X, Y, and Z axes.
            float length = 1.1f;
            Line xAxis = new Line(Vertex.ORIGIN, new Vertex(length, 0, 0, new Colour(1, 0, 0)));
            Line yAxis = new Line(Vertex.ORIGIN, new Vertex(0, length, 0, new Colour(0, 1, 0)));
            Line zAxis = new Line(Vertex.ORIGIN, new Vertex(0, 0, length, new Colour(0, 0, 1)));
            this.drawables.addAll(Arrays.asList(xAxis, yAxis, zAxis));

            // The overhang of the grid in each direction of the World.
            float overhang = 2;
            // The number of rows and columns in the grid.
            int size = 17;
            // The colour of the grid.
            Colour gridColour = new Colour(0.3f, 0.3f, 0.3f); 

            // Calculate the minimum, maximum, and step of the grid dimensions.
            Pair<Float, Float> gridX = new Pair<>(minX - overhang, maxX + overhang);
            Pair<Float, Float> gridY = new Pair<>(minY - overhang, maxY + overhang);
            float stepX = (gridX.getSecond() - gridX.getFirst())/size;
            float stepY = (gridY.getSecond() - gridY.getFirst())/size;

            // Draw the grid.
            for (float x = gridX.getFirst(); x <= gridX.getSecond(); x += stepX) {
                for (float y = gridY.getFirst(); y <= gridY.getSecond(); y += stepY) {
                    Line h = new Line(new Vertex(x, gridY.getFirst(), 0, gridColour), new Vertex(x, gridY.getSecond(), 0, gridColour));
                    Line v = new Line(new Vertex(gridX.getFirst(), y, 0, gridColour), new Vertex(gridX.getSecond(), y, 0, gridColour));
                    this.drawables.addAll(Arrays.asList(h, v));
                }
            }
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
     * The list of Light sources in this World.
     */
    private ArrayList<Light> lights;
}