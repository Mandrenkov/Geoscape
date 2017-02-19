package model;

public class AlgVector2D extends AlgVector {

	public AlgVector2D(float x, float y) {
		super(new float[]{x, y});
	}

	public float getX() {
		return super.get(0);
	}

	public float getY() {
		return super.get(1);
	}

	public void setX(int x) {
		super.set(0, x);
	}

	public void setY(int y) {
		super.set(1, y);
	}
}