package geo;

import java.util.HashMap;

import env.Biome;
import util.Colour;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The TerrainPoint class represents a Point in a Terrain context.</p>
 */
public class TerrainPoint extends Point {

	/**
	 * Geographic biome of this TerrainPoint.
	 */
	private Biome biome;
	/**
	 * Mapping between various Biomes and their influence on this TerrainPoint's Biome characteristics.
	 * Biomes that are omitted from this HashMap are assumed to have an influence of 0.
	 */
	private HashMap<Biome, Float> biomeMix;
	/**
	 * Colour of the geographic Biome of this TerrainPoint.
	 */
	private float[] colour;

	/**
	 * Constructs a TerrainPoint object at the given 3D coordinate.
	 * 
	 * @param x X-coordinate of this TerrainPoint.
	 * @param y Y-coordinate of this TerrainPoint.
	 * @param z Z-coordinate of this TerrainPoint.
	 */
	public TerrainPoint(float x, float y, float z) {
		super(x, y, z);
	}

	/**
	 * Constructs a TerrainPoint object at the given 3D coordinate with the
	 * specified geographic Biome.
	 * 
	 * @param biome Geographic Biome of this TerrainPoint.
	 * @param x X-coordinate of this TerrainPoint.
	 * @param y Y-coordinate of this TerrainPoint.
	 * @param z Z-coordinate of this TerrainPoint.
	 */
	public TerrainPoint(Biome biome, float x, float y, float z) {
		this(x, y, z);
		this.biome = biome;
		this.colour = biome.getColour();
		this.biomeMix = new HashMap<>();

		this.biomeMix.put(biome, 1f);
	}

	/**
	 * Returns the geographic Biome of this TerrainPoint.
	 * 
	 * @return The geographic Biome of this TerrainPoint.
	 */
	public Biome getBiome() {
		return biome;
	}
	
	/**
	 * Scales the colour of this TerrainPoint by a random value within the given range.
	 * The applied scaling will always decrease the luminosity of the TerrainPoint.
	 * 
	 * @param range The maximum possible scaling factor.  This value should fall within the range [0, 1]. 
	 */
	public void colourDip(float range) {
		float clrScale = 1f - (float) Math.random()*range;
		colour = Colour.scaleColour(clrScale, colour);
	}

	/**
	 * Returns the Biome influence map of this TerrainPoint.
	 * 
	 * @return The Biome influence map of this TerrainPoint.
	 */
	public HashMap<Biome, Float> getBiomeMix() {
		return biomeMix;
	}

	/**
	 * Returns the colour of this TerrainPoint.
	 * 
	 * @return The colour of this TerrainPoint.
	 */
	public float[] getColour() {
		return colour;
	}

	/**
	 * Sets the Biome of this TerrainPoint to the given Biome.
	 * 
	 * @param biome The new Biome of this TerrainPoint.
	 */
	public void setBiome(Biome biome) {
		this.biome = biome;
		this.colour = biome.getColour();
	}

	/**
	 * Sets the Biome influence map of this TerrainPoint to the given map.
	 * 
	 * @param biomeMix The new Biome influence map.
	 */
	public void setBiomeMix(HashMap<Biome, Float> biomeMix) {
		this.biomeMix = biomeMix;
	}

	/**
	 * Sets the colour of this TerrainPoint to the given colour.
	 * 
	 * @param colour The new colour of this TerrainPoint.
	 */
	public void setColour(float[] colour) {
		this.colour = colour;
	}

	/**
	 * Returns a String representation of this TerrainPoint.
	 */
	public String toString() {
		return String.format("TerrainPoint: \"%s\", (%.2f, %.2f, %.2f)", biome.getName(), super.getX(), super.getY(), super.getZ());
	}

}