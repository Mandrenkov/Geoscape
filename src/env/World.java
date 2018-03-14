package env;

import java.util.ArrayList;
import java.util.Arrays;

import core.Top;
import geo.Line;
import geo.Vertex;

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

        // Add Axes to the World for debugging purposes.
        if (Top.DEBUG) {
            float length = 1.1f;
            Line xAxis = new Line(Vertex.ORIGIN, new Vertex(new Colour(1, 0, 0), length, 0, 0));
            Line yAxis = new Line(Vertex.ORIGIN, new Vertex(new Colour(0, 1, 0), 0, length, 0));
            Line zAxis = new Line(Vertex.ORIGIN, new Vertex(new Colour(0, 0, 1), 0, 0, length));
            drawables.addAll(Arrays.asList(xAxis, yAxis, zAxis));
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
     * Draws this World.
     */
    public void draw() {
        for (Drawable drawable : drawables) {
            drawable.draw();
        }
    }

    /**
     * Returns the number of Polygons in this World.
     *
     * @return The number of Polygons.
     */
    public int polygons() {
        return drawables.stream().mapToInt(drawable -> drawable.polygons()).sum();
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
        return String.format("\"%s\" with %d polygons", this.name, this.polygons());
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
}