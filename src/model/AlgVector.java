package model;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>AlgVector</i> class represents a generic algebraic vector.</p>
 */ 
public class AlgVector {

	/**
	 * Dimension components.
	 */
	private float[] components;
	/**
	 * Number of dimensions.
	 */
	private int dimensions;

	/**
	 * Constructs an AlgVector with the specified component values.
	 * 
	 * @param components Components of the AlgVector.
	 */
	public AlgVector(float ... components) {
		this.components = components;
		this.dimensions = components.length;
	}

	/**
	 * Returns the angle between this AlgVector and the specified AlgVector.
	 * 
	 * @param vector AlgVector that forms the desired angle.
	 * 
	 * @return Angle between the AlgVectors.
	 */
	public float angle(AlgVector vector) {
		if (dimensions != vector.dimensions) {
			throw new IllegalArgumentException("Vectors must have the same dimensionality");
		}

		return (float) Math.acos(this.dot(vector)/(this.magnitude()*vector.magnitude()));
	}

	/**
	 * Returns the dot product between this AlgVector and the specified AlgVector.
	 * 
	 * @param vector Second operand of the dot product operation. 
	 * 
	 * @return The dot product.
	 */
	public float dot(AlgVector vector) {
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
	 * Returns the AlgVector that can be added to this AlgVector to produce the specified AlgVector.
	 * 
	 * @param vector Target AlgVector.
	 * 
	 * @return The AlgVector that connects this AlgVector to the given AlgVector.
	 */
	public AlgVector to(AlgVector vector) {
		if (dimensions != vector.dimensions) {
			throw new IllegalArgumentException("Vectors must have the same dimensionality");
		}

		float[] components = new float[dimensions];

		for (int i = 0 ; i < this.dimensions ; ++i) {
			components[i] = vector.components[i] - this.components[i];
		}

		return new AlgVector(components);
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
	 * Returns all the components of this AlgVector.
	 * 
	 * @return The components of this AlgVector.
	 */
	public float[] getAll() {
		return components;
	}

	/**
	 * Returns the algebraic magnitude of this AlgVector.
	 * 
	 * @return The magnitude of this AlgVector.
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
	 * Returns a String representation of this AlgVector.
	 */
	public String toString() {
		StringBuilder vecString = new StringBuilder(this.getClass().getName());
		vecString.append(": (");
		for (int i = 0 ; i < dimensions - 1 ; ++i) {
			vecString.append(String.format("%.6f, ", components[i]));
		}
		vecString.append(String.format("%.6f)", components[dimensions - 1]));

		return vecString.toString();
	}
}