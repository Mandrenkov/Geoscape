package geo;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Point</b> class.</p>
 */
public class Point {

	private float x, y, z;

	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point average(Point point) {
		return new Point((this.x + point.x)/2, (this.y + point.y)/2, (this.z + point.z)/2);
	}

	public float distance(Point point) {
		return (float) Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public String toString() {
		return String.format("Point: (%.2f, %.2f, %.2f)", x, y, z);
	}
}