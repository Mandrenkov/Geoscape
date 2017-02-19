package model;

public class AlgVector3D extends AlgVector {

	public AlgVector3D(float x, float y, float z) {
		super(new float[]{x, y, z});
	}

	public AlgVector3D(Point p) {
		super(new float[]{p.getX(), p.getY(), p.getZ()});
	}

	public AlgVector3D cross(AlgVector3D vector) {
		float x, y, z;

		x = this.getY()*vector.getZ() - this.getZ()*vector.getY();
		y = this.getZ()*vector.getX() - this.getX()*vector.getZ();
		z = this.getX()*vector.getY() - this.getY()*vector.getX();

		return new AlgVector3D(x, y, z);
	}

	public AlgVector3D to(AlgVector3D vector) {
		float[] components = super.to(vector).getAll();
		return new AlgVector3D(components[0], components[1], components[2]);
	}

	public float getX() {
		return super.get(0);
	}

	public float getY() {
		return super.get(1);
	}

	public float getZ() {
		return super.get(2);
	}

	public void setX(int x) {
		super.set(0, x);
	}

	public void setY(int y) {
		super.set(1, y);
	}

	public void setZ(int z) {
		super.set(2, z);
	}
}