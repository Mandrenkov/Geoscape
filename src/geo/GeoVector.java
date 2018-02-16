package geo;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <i>GeoVector</i> class represents a geometric vector.</p>
 */
public class GeoVector {

	/**
	 * Dimension components.
	 */
	private float[] components;
	/**
	 * Number of dimensions.
	 */
	private int dimensions;

	/**
	 * Constructs a GeoVector with the specified component values.
	 *
	 * @param components Components of the GeoVector.
	 */
	public GeoVector(float ... components) {
		this.components = components;
		this.dimensions = components.length;
	}

	/**
	 * Constructs a GeoVector that starts at the origin and ends at the specified Point.
	 *
	 * @param p Point that denotes the end coordinates of the GeoVector.
	 */
	public GeoVector(Vertex p) {
		this.components = new float[]{p.getX(), p.getY(), p.getZ()};
		this.dimensions = 3;
	}

	/**
	 * Returns the angle between this GeoVector and the specified GeoVector.
	 *
	 * @param vector GeoVector that forms the desired angle.
	 *
	 * @return Angle between the GeoVectors.
	 */
	public float angle(GeoVector vector) {
		if (dimensions != vector.dimensions) {
			throw new IllegalArgumentException("Vectors must have the same dimensionality");
		}

		return (float) Math.acos(this.dot(vector)/(this.magnitude()*vector.magnitude()));
	}

	/**
	 * Returns the cross product between this GeoVector and the specified GeoVector.
	 *
	 * @param vector Second operand of the cross product operation.
	 *
	 * @return The cross product.
	 */
	public GeoVector cross(GeoVector vector) {
		if (dimensions != 3 || vector.dimensions != 3) {
			throw new IllegalArgumentException("Vectors must have a dimensionality of 3");
		}

		float x, y, z;

		x = this.getY()*vector.getZ() - this.getZ()*vector.getY();
		y = this.getZ()*vector.getX() - this.getX()*vector.getZ();
		z = this.getX()*vector.getY() - this.getY()*vector.getX();

		return new GeoVector(x, y, z);
	}

	/**
	 * Returns the dot product between this GeoVector and the specified GeoVector.
	 *
	 * @param vector Second operand of the dot product operation.
	 *
	 * @return The dot product.
	 */
	public float dot(GeoVector vector) {
		if (dimensions != vector.dimensions) {
			throw new IllegalArgumentException("Vectors must have the same dimensionality");
		}

		float product = 0;
		for (int i = 0 ; i < this.dimensions ; ++i) {
			product += this.components[i]*vector.components[i];
		}

		return product;
	}

	/**
	 * Returns the value of the X component of this GeoVector.
	 *
	 * @return The value of the X component of this GeoVector.
	 */
	public float getX() {
		if (dimensions < 1) throw new IllegalArgumentException("Vector does not have a X component");
		return components[0];
	}

	/**
	 * Returns the value of the Y component of this GeoVector.
	 *
	 * @return The value of the Y component of this GeoVector.
	 */
	public float getY() {
		if (dimensions < 2) throw new IllegalArgumentException("Vector does not have a Y component");
		return components[1];
	}

	/**
	 * Returns the value of the Z component of this GeoVector.
	 *
	 * @return The value of the Z component of this GeoVector.
	 */
	public float getZ() {
		if (dimensions < 3) throw new IllegalArgumentException("Vector does not have a Z component");
		return components[2];
	}

	public void normalize() {
		float magnitude = this.magnitude();

		for (int i = 0 ; i < this.dimensions ; ++i) {
			components[i] /= magnitude;
		}
	}

	/**
	 * Sets the magnitude of this GeoVector along the X dimension to the specified value.
	 *
	 * @param x The desired magnitude along the X dimension.
	 */
	public void setX(int x) {
		if (dimensions < 1) throw new IllegalArgumentException("Vector does not have a X component");
		components[0] = x;
	}

	/**
	 * Sets the magnitude of this GeoVector along the Y dimension to the specified value.
	 *
	 * @param x The desired magnitude along the Y dimension.
	 */
	public void setY(int y) {
		if (dimensions < 2) throw new IllegalArgumentException("Vector does not have a Y component");
		components[1] = y;
	}

	/**
	 * Sets the magnitude of this GeoVector along the Y dimension to the specified value.
	 *
	 * @param x The desired magnitude along the Y dimension.
	 */
	public void setZ(int z) {
		if (dimensions < 3) throw new IllegalArgumentException("Vector does not have a Z component");
		components[2] = z;
	}

	/**
	 * Returns the GeoVector that can be added to this GeoVector to produce the specified AlgVector.
	 *
	 * @param vector Target GeoVector.
	 *
	 * @return The AlgVector that connects this GeoVector to the given AlgVector.
	 */
	public GeoVector to(GeoVector vector) {
		if (dimensions != vector.dimensions) {
			throw new IllegalArgumentException("Vectors must have the same dimensionality");
		}

		float[] components = new float[dimensions];

		for (int i = 0 ; i < this.dimensions ; ++i) {
			components[i] = vector.components[i] - this.components[i];
		}

		return new GeoVector(components);
	}

	/**
	 * Returns the value of the component along the specified dimension.
	 *
	 * @param dimension The dimension index of the desired component.
	 *
	 * @return The value of the component along the specified dimension.
	 */
	public float get(int dimension) {
		return components[dimension];
	}

	/**
	 * Returns all the components of this GeoVector.
	 *
	 * @return The components of this GeoVector.
	 */
	public float[] getAll() {
		return components;
	}

	/**
	 * Returns the algebraic magnitude of this GeoVector.
	 *
	 * @return The magnitude of this GeoVector.
	 */
	public float magnitude() {
		float magnitude = 0;

		for (float component : components) {
			magnitude += component*component;
		}

		return (float) Math.sqrt(magnitude);
	}

	/**
	 * Sets the component of the specified dimension to the given value.
	 *
	 * @param dimension Dimension of the component.
	 * @param value Desired value of the component.
	 */
	public void set(int dimension, int value) {
		components[dimension] = value;
	}

	/**
	 * Returns a String representation of this GeoVector.
	 */
	public String toString() {
		StringBuilder vecString = new StringBuilder(String.format("%dD Vector", dimensions));
		vecString.append(": [");
		for (int i = 0 ; i < dimensions - 1 ; ++i) {
			vecString.append(String.format("%.4f ", components[i]));
		}
		vecString.append(String.format("%.4f]", components[dimensions - 1]));

		return vecString.toString();
	}
}