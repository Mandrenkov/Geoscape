package env;

import core.Logger;
import geo.TerrainPoint;
import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <b>Biome</b> class represents a landscape region with points that share common characteristics.</p>
 */
public class Biome {
	/**
	 * The "Desert" Biome.
	 */
	public static final Biome DESERT = new Biome("Desert", Colour.TERRAIN_DESERT, 0.05f) {
		public void texturize(TerrainPoint point, float dominance) {
			float z = point.getZ();

			float refFreq = 10f;		// Frequency of lines waves
			float refGain = 0.05f;		// Amplitude of line waves
			float scaleFreq = 300f;		// Density of line waves
			float scaleGain = 0.001f;	// Height of line waves

			float deltaZ = (float) (scaleGain*Math.cos(scaleFreq*Math.abs(point.getX() - refGain*Math.cos(point.getY()*refFreq))));
			deltaZ *= dominance;

			point.setZ(z + deltaZ);
		}
	};
	/**
	 * The "Hill" Biome.
	 */
	public static final Biome HILL = new Biome("Hills", Colour.TERRAIN_HILLS, 0.5f) {
		public void texturize(TerrainPoint point, float dominance) {
			point.bump(0.001f * dominance);
		}
	};
	/**
	 * The "Mountain" Biome.
	 */
	public static final Biome MOUNTAIN = new Biome("Mountains", Colour.TERRAIN_MOUNTAINS, 1.5f) {
		public void texturize(TerrainPoint point, float dominance) {
			point.bump(0.01f * dominance);
		}
	};
	/**
	 * The "Plain" Biome.
	 */
	public static final Biome PLAIN = new Biome("Plain", Colour.TERRAIN_PLAINS, 0.02f) {
		public void texturize(TerrainPoint point, float dominance) {
			point.bump(0.0001f * dominance);
			point.colourDip(dominance/8f);
		}
	};
	/**
	 * The "Tundra" Biome.
	 */
	public static final Biome TUNDRA = new Biome("Tundra", Colour.TERRAIN_TUNDRA, 0.10f) {
		public void texturize(TerrainPoint point, float dominance) {
			point.bump(0.0001f * dominance);
			point.colourDip(dominance/10f);
		}
	};
	
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
		Logger.warn("texturize() not implemented for Biome \"%s\".", NAME);
	}

	/**
	 * Returns a String representation of this Biome.
	 */
	public String toString() {
		return String.format("Biome \"%s\": %s %.2f", NAME, Colour.colourString(COLOUR), Z_SCALE);
	}
}