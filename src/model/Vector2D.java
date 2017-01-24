package model;

public class Vector2D {

	private float x, y;

	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float dot(Vector2D vector) {
		return x*vector.x + y*vector.y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}
}