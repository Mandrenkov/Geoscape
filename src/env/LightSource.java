package env;

import geo.*;
import util.*;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>LightSource</b> class.</p>
 */ 
public class LightSource implements Drawable {
	private Point position;
	private Sphere sphere;

	public LightSource(Point position) {
		this.position = position;
		
		this.sphere = new Sphere(position, 0.05f);
	}
	
	public void draw() {
		Render.drawSphere(sphere, new float[]{1f, 1f, 1f});
	}

	public Point getPosition() {
		return position;
	}
	
	public Sphere getSphere() {
		return sphere;
	}

	public String toString() {
		return String.format("%s: (%.2f, %.2f, %.2f)", this.getClass().getName(), position.getX(), position.getY(), position.getZ());
	}
}