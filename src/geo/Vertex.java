package geo;

import static org.lwjgl.opengl.GL11.*;

import env.Colour;
import util.RNG;

/**
 * The Vertex class represents a geometric point in R3 Cartesian space.
 */
public class Vertex {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * The Vertex located at the origin.
     */
    public static final Vertex ORIGIN = new Vertex(0, 0, 0);

    /**
     * Returns the average position of the given Vertexes.  This function
     * assumes that the given list of Vertexes is non-empty.
     *
     * @param vertexes The Vertexes to be averaged.
     *
     * @return The average position.
     */
    public static Vertex average(Vertex... vertexes) {
        Vertex average = new Vertex(0, 0, 0);
        for (Vertex vertex : vertexes) {
            average.translate(vertex.getX(), vertex.getY(), vertex.getZ());
        }
        average.scale(1f/vertexes.length);
        return average;
    }

    /**
     * Returns the average Z-coordinate of the given Vertexes.  This function
     * assumes that the given list of Vertexes is non-empty.
     *
     * @param vertexes The Vertexes to be averaged.
     *
     * @return The average Z-coordinate.
     */
    public static float averageZ(Vertex... vertexes) {
        float z = 0;
        for (Vertex vertex : vertexes) {
            z += vertex.getZ();
        }
        return z/vertexes.length;
    }

    /**
     * Constructs a Vertex representing the given 3D coordinate.
     *
     * @param x The X-coordinate of this Vertex.
     * @param y The Y-coordinate of this Vertex.
     * @param z The Z-coordinate of this Vertex.
     */
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.colour = new Colour();
    }

    /**
     * Constructs a Vertex with the given Colour representing the given 3D coordinate.
     *
     * @param x      The X-coordinate of this Vertex.
     * @param y      The Y-coordinate of this Vertex.
     * @param z      The Z-coordinate of this Vertex.
     * @param colour The colour of this Vertex.
     */
    public Vertex(float x, float y, float z, Colour colour) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.colour = colour;
    }

    /**
     * Constructs a new Vertex that is a clone of the given Vertex.
     *
     * @param vertex The Vertex to clone.
     */
    public Vertex(Vertex vertex) {
        this.x = vertex.x;
        this.y = vertex.y;
        this.z = vertex.z;
        this.colour = new Colour(vertex.colour);
    }

    /**
     * Constructs a Vertex at the midpoint of the given Vertexes.
     *
     * @param v1 The first Vertex.
     * @param v2 The second Vertex.
     */
    public Vertex(Vertex v1, Vertex v2) {
        this.x = (v1.x + v2.x)/2;
        this.y = (v1.y + v2.y)/2;
        this.z = (v1.z + v2.z)/2;
        this.colour = Colour.average(v1.colour, v2.colour);
    }

    /**
     * Returns true if this Vertex is close to the given Vertex.
     *
     * @param vertex The other Vertex.
     *
     * @return True if the Vertexes occupy the same spatial region.
     */
    public boolean close(Vertex vertex) {
        return this.distance(vertex) < 1E-6;
    }

    /**
     * Returns the distance between this Vertex and the given Vertex.
     *
     * @param vertex The other Vertex.
     *
     * @return The distance between the Vertexes.
     */
    public float distance(Vertex vertex) {
        return (float) Math.sqrt(Math.pow(this.x - vertex.x, 2)
                               + Math.pow(this.y - vertex.y, 2)
                               + Math.pow(this.z - vertex.z, 2));
    }

    /**
     * Returns the distance from this Vertex to the origin.
     *
     * @return The distance to the origin.
     */
    public float magnitude() {
        return distance(Vertex.ORIGIN);
    }

    /**
     * Returns the X-coordinate of this Vertex.
     *
     * @return The X-coordinate.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Returns the Y-coordinate of this Vertex.
     *
     * @return The Y-coordinate.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Returns the Z-coordinate of this Vertex.
     *
     * @return The Z-coordinate.
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Returns the Colour of this Vertex.
     *
     * @return The Colour.
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Sets the GL colour to the Colour of this Vertex.
     */
    public void glColour() {
        this.colour.glColour();
    }

    /**
     * Adds this Vertex to the GL buffer.
     */
    public void glVertex() {
        glVertex3f(this.x, this.y, this.z);
    }

    /**
     * Sets the X-coordinate of this Vertex to the specified value.
     *
     * @param x The new X-coordinate.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the Y-coordinate of this Vertex to the specified value.
     *
     * @param y The new Y-coordinate.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the Z-coordinate of this Vertex to the specified value.
     *
     * @param z The new Z-coordinate.
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Sets the Colour of this Vertex to the specified value.
     *
     * @param colour The new Colour.
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * Scales the coordinates of this Vertex by the given scaling factor.
     */
    public void normalize() {
        float magnitude = magnitude();
        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;
    }

    /**
     * Scales the coordinates of this Vertex by the given scaling factor.
     *
     * @param scalar The scaling factor.
     */
    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    /**
     * Changes the elevation of this Vertex by a random value within the given range.
     *
     * @param magnitude The maximum magnitude of the change in elevation.
     */
    public void shift(float magnitude) {
        this.z += RNG.random(magnitude);
    }

    /**
     * Translates this Vertex by the given coordinate values.
     *
     * @param dx The amount to translate the X-coordinate of this Vertex.
     * @param dy The amount to translate the Y-coordinate of this Vertex.
     * @param dz The amount to translate the Z-coordinate of this Vertex.
     */
    public void translate(float dx, float dy, float dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    /**
     * Returns a float array representation of this Vertex.  This array has
     * the following form: [x, y, z, 1].
     * 
     * @return The float array representation.
     */
    public float[] toArray() {
        return new float[]{this.x, this.y, this.z, 1};
    }

    /**
     * Returns a String representation of this Vertex.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("Vertex (%.2f, %.2f, %.2f)", this.x, this.y, this.z);
    }


    // Protected members
    // -------------------------------------------------------------------------

    /**
     * The X-coordinate of this Vertex.
     */
    protected float x;

    /**
     * The Y-coordinate of this Vertex.
     */
    protected float y;

    /**
     * The Z-coordinate of this Vertex.
     */
    protected float z;

    /**
     * The colour of this Vertex.
     */
    protected Colour colour;
}