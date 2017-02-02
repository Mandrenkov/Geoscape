package model;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>The <i>Colour</i> class is responsible for storing and manipulating application colours.</p>
 */ 
public class Colour {
	
	public static final float[] BACKDROP = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	public static final float[] PLATFORM = new float[] {0.1f, 0.1f, 0.1f, 1.0f};
	
	public static void clearColour(float[] colour) {
		glClearColor(colour[0], colour[1], colour[2], colour[3]);
	}
	
	public static void setColour(float[] colour) {
		if (colour.length == 3 || colour.length == 4) {
			glColor3f(colour[0], colour[1], colour[2]);
		} else {
			System.err.println("Error: Called setColour() with an array of length " + colour.length + ".");
		}
	}

}
