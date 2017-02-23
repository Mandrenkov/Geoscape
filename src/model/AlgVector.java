package model;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>AlgVector</b> class.</p>
 */ 
public class AlgVector {

	private float[] components;
	private int dimensions;

	public AlgVector(float ... components) {
		this.components = components;
		this.dimensions = components.length;
	}

	public float angle(AlgVector vector) {
		if (dimensions != vector.dimensions) {
			throw new IllegalArgumentException("Vectors must have the same dimensionality");
		}

		return (float) Math.acos(this.dot(vector)/(this.magnitude()*vector.magnitude()));
	}

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

	public float get(int component) {
		return components[component];
	}

	public float[] getAll() {
		return components;
	}

	public float magnitude() {
		float magnitude = 0;

		for (float component : components) {
			magnitude += component*component;
		}

		return (float) Math.sqrt(magnitude);
	}

	public float set(int component, int value) {
		return components[component] = value;
	}

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