package model;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>LightSource</b> class.</p>
 */ 
public class LightSource {
	private Point position;

	public LightSource(Point position) {
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}

	public String toString() {
		return String.format("%s: (%.2f, %.2f, %.2f)", this.getClass().getName(), position.getX(), position.getY(), position.getZ());
	}
}