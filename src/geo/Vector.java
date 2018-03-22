package geo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <i>Vector</i> class represents a geometric vector in R3 Cartesian space.</p>
 */
public class Vector {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Vector with the given X and Y components.
     *
     * @param x The X-component of this Vector.
     * @param y The Y-component of this Vector.
     */
    public Vector(float x, float y) {
        this(x, y, 0);
    }

    /**
     * Constructs a Vector with the given X, Y, and Z components.
     *
     * @param x The X-component of this Vector.
     * @param y The Y-component of this Vector.
     * @param z The Z-component of this Vector.
     */
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a Vector that stretches from the given start Vertex to the
     * provided end Vertex.
     *
     * @param start The start Vertex.
     * @param end   The end Vertex.
     */
    public Vector(Vertex start, Vertex end) {
        this.x = end.getX() - start.getX();
        this.y = end.getY() - start.getY();
        this.z = end.getZ() - start.getZ();
    }

    /**
     * Constructs a Vector with the components of the given Vertex.
     *
     * @param vertex The reference Vertex.
     */
    public Vector(Vertex vertex) {
        this(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    /**
     * Constructs a Vector representing the cross product between the two given
     * Vectors.
     *
     * @param v1 The first Vector.
     * @param v2 The second Vector.
     */
    public Vector(Vector v1, Vector v2) {
        this.x = v1.y*v2.z - v1.z*v2.y;
        this.y = v1.z*v2.x - v1.x*v2.z;
        this.z = v1.x*v2.y - v1.y*v2.x;
    }

    /**
     * Returns the angle between this Vector and the given Vector.
     *
     * @param vector The Vector which forms the desired angle.
     *
     * @return The angle (in radians) between the Vectors.
     */
    public float angle(Vector vector) {
        // Apply the dot product formula: dot(x, y) = |x|*|y|*cos(angle).
        return (float) Math.acos(this.dot(vector)/(this.magnitude()*vector.magnitude()));
    }

    /**
     * Returns the dot product between this Vector and the given Vector.
     *
     * @param vector The second operand of the dot product operation.
     *
     * @return The dot product between the two Vectors.
     */
    public float dot(Vector vector) {
        return this.x*vector.x + this.y*vector.y + this.z*vector.z;
    }

    /**
     * Returns the X-component of this Vector.
     *
     * @return The X-component.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Returns the Y-component of this Vector.
     *
     * @return The Y-component.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Returns the Z-component of this Vector.
     *
     * @return The Z-component.
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Adds this Vector to the GL buffer as a normal vector.
     */
    public void glNormal() {
        Vector normal = new Vector(this.x, this.y, this.z);
        normal.normalize();
        glNormal3f(normal.x, normal.y, normal.z);
    }

    /**
     * Returns the magnitude of this Vector.
     *
     * @return The magnitude.
     */
    public float magnitude() {
        return (float) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

    /**
     * Normalizes this Vector.
     */
    public void normalize() {
        float magnitude = this.magnitude();
        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;
    }

    /**
     * Sets the X-component of this Vector.
     *
     * @param x The new X-component of this Vector.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the Y-component of this Vector.
     *
     * @param y The new Y-component of this Vector.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the Z-component of this Vector.
     *
     * @param z The new Z-component of this Vector.
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Returns the Vector that can be added to this Vector to produce the given Vector.
     *
     * @param vector The target Vector.
     *
     * @return The Vector that connects this Vector to the given Vector.
     */
    public Vector to(Vector vector) {
        return new Vector(vector.x - this.x, vector.y - this.y, vector.z - this.z);
    }

    /**
     * Returns a String representation of this Vector.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", this.x, this.y, this.z);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The X-component of this Vector.
     */
    private float x;

    /**
     * The Y-component of this Vector.
     */
    private float y;

    /**
     * The Z-component of this Vector.
     */
    private float z;
}