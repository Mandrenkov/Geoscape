package util;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.util.StringJoiner;

import geo.TerrainPoint;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <i>Colour</i> class is responsible for storing and manipulating application colours.</p>
 */
public class Colour {

	/**
	 * Colour of the backdrop.
	 */
	public static final float[] BACKDROP = new float[] {0.05f, 0.05f, 0.05f, 1.0f};
	/**
	 * Colour of the platform.
	 */
	public static final float[] PLATFORM = new float[] {0.1f, 0.1f, 0.1f, 1.0f};

	/**
	 * Base colour of the "Desert" Biome.
	 */
	public static final float[] TERRAIN_DESERT = new float[] {0.6f, 0.3f, 0.0f, 1.0f};
	/**
	 * Base colour of the "Hills" Biome.
	 */
	public static final float[] TERRAIN_HILLS = new float[] {0.2f, 0.4f, 0.0f, 1.0f};
	/**
	 * Base colour of the "Mountains" Biome.
	 */
	public static final float[] TERRAIN_MOUNTAINS = new float[] {0.15f, 0.1f, 0.0f, 1.0f};
	/**
	 * Base colour of the "Plains" Biome.
	 */
	public static final float[] TERRAIN_PLAINS = new float[] {0.8f, 1.0f, 0.0f, 1.0f};
	/**
	 * Base colour of the "Tunda" Biome.
	 */
	public static final float[] TERRAIN_TUNDRA = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

	/**
	 * Returns the average colour represented by the given TerrainPoints.
	 * 
	 * @param points The TerrainPoints whose colours are to be averaged.
	 * @return The average TerrainPoint colour.
	 */
	public static float[] averageColour(TerrainPoint ... points) {
		float[] avgColour = new float[4];

		for (TerrainPoint point : points) {
			for (int c = 0 ; c < 4 ; c++) {
				avgColour[c] += point.getColour()[c]/points.length;
			}
		}
		return avgColour;
	}

	/**
	 * Returns a String representation of the given colour.
	 * 
	 * @param colour The colour to be represented as a String.
	 * @return The String representation of the colour.
	 */
	public static String colourString(float[] colour) {
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
		for (float c : colour)
			joiner.add(String.format("%.2f", c));
		return joiner.toString();
	}

	/**
	 * Sets the GL colour to the given colour.
	 * 
	 * @param colour The new GL colour.
	 */
	public static void setColour(float[] colour) {
		if (colour.length == 3 || colour.length == 4) {
			glColor3f(colour[0], colour[1], colour[2]);
		} else {
			System.err.println("Error: Called setColour() with an array of length " + colour.length + ".");
		}
	}

	/**
	 * Returns the given colour scaled by the specified factor.
	 * 
	 * @param scalar The multiplicative amount to scale the colour.
	 * @param colour The colour to be scaled.
	 * @return The scaled colour.
	 */
	public static float[] scaleColour(float scalar, float[] colour) {
		float[] scaledColour = new float[colour.length];

		for (int c = 0 ; c < colour.length ; c++)
			scaledColour[c] = scalar*colour[c];
		return scaledColour;
	}
}