package util;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;

import geo.TerrainPoint;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>Colour</i> class is responsible for storing and manipulating application colours.</p>
 */
public class Colour {

	public static final float[] BACKDROP = new float[] {0.05f, 0.0f, 0.0f, 1.0f};
	public static final float[] PLATFORM = new float[] {0.1f, 0.1f, 0.1f, 1.0f};

	public static final float[] TERRAIN_HILLS     = new float[] {0.2f, 0.4f, 0.0f, 1.0f}; // {0.3f, 0.6f, 0.0f, 1.0f};
	public static final float[] TERRAIN_MOUNTAINS = new float[] {0.2f, 0.1f, 0.0f, 1.0f};
	public static final float[] TERRAIN_PLAINS    = new float[] {0.8f, 1.0f, 0.0f, 1.0f};
	public static final float[] TERRAIN_TUNDRA    = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

	public static float[] averageColour(TerrainPoint ... points) {
		float[] avgColour = new float[4];

		for (TerrainPoint point : points) {
			for (int c = 0 ; c < 4 ; c++) {
				avgColour[c] += point.getColour()[c];
			}
		}

		for (int c = 0 ; c < 4 ; c++) {
			avgColour[c] /= points.length;
		}

		return avgColour;
	}

	public static void clearColour(float[] colour) {
		glClearColor(colour[0], colour[1], colour[2], colour[3]);
	}

	public static String colourString(float[] colour) {
		StringBuilder clrString = new StringBuilder("[");
		for (int c = 0 ; c < colour.length - 1 ; c++) {
			clrString.append(String.format("%.2f, ", colour[c]));
		}
		clrString.append(String.format("%.2f]", colour[colour.length - 1]));

		return clrString.toString();
	}

	public static void setColour(float[] colour) {
		if (colour.length == 3 || colour.length == 4) {
			glColor3f(colour[0], colour[1], colour[2]);
		} else {
			System.err.println("Error: Called setColour() with an array of length " + colour.length + ".");
		}
	}

	public static float[] scaleColour(float scalar, float[] colour) {
		float[] scaledColour = new float[colour.length];

		for (int c = 0 ; c < colour.length ; c++) {
			scaledColour[c] = scalar*colour[c];
		}

		return scaledColour;
	}

	public static float[] triangleColour(TerrainPoint[] points) {
		return averageColour(points);
	}

}