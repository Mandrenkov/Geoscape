package env;

import geo.TerrainPoint;
import util.Colour;

/**
 * @author Mikhail Andrenkov
 * @since April 10, 2017
 * @version 1.0
 *
 * <p>The <b>Biome</b> class represents a landscape region with points that share common characteristics.</p>
 */
public class Biome {
	/**
	 * Base colour of this Biome.
	 */
	private final float[] COLOUR;
	/**
	 * Elevation scaling factor for this Biome.
	 */
	private final float Z_SCALE;
	/**
	 * Name of this Biome.
	 */
	private final String NAME;

	/**
	 * Constructs a Biome object with the specified name, colour, and scaling factor.
	 * 
	 * @param name Name of this Biome
	 * @param colour Colour of this Biome
	 * @param zScale Elevation scaling factor of this Biome
	 */
	public Biome(String name, float[] colour, float zScale) {
		this.NAME = name;
		this.COLOUR = colour;
		this.Z_SCALE = zScale;
	}

	/**
	 * Returns the colour of this Biome.
	 * 
	 * @return The colour of this Biome.
	 */
	public float[] getColour() {
		return COLOUR;
	}

	/**
	 * Returns the name of this Biome.
	 * 
	 * @return The name of this Biome.
	 */
	public String getName() {
		return this.NAME;
	}

	/**
	 * Returns the elevation scaling of this Biome.
	 * 
	 * @return The elevation scaling of this Biome.
	 */	
	public float getScale() {
		return Z_SCALE;
	}

	/**
	 * Applies a texturing to the given point with respect to the provided degree of dominance.
	 * 
	 * @param point Point to be textured.
	 * @param dominance Intensity of the texturizing process.  Typically within the range of [0, 1].
	 */
	public void texturize(TerrainPoint point, float dominance) {
		System.out.println("Warning: texturize() not implemented.");
	}

	/**
	 * Returns a String representation of this Biome.
	 */
	public String toString() {
		return String.format("Biome \"%s\": %s %.2f", NAME, Colour.colourString(COLOUR), Z_SCALE);
	}
}