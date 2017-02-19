package model;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2017
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
		return String.format("Light: (%.2f, %.2f, %.2f)", position.getX(), position.getY(), position.getZ());
	}
}