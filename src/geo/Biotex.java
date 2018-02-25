package geo;

import env.Biomix;
import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since February 25, 2018
 * @version 1.1
 *
 * <p>The <b>Biotex</b> class represents a Biome Vertex.</p>
 */
public class Biotex {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a TerrainPoint object at the given 3D coordinate.
	 * 
	 * @param x X-coordinate of this TerrainPoint.
	 * @param y Y-coordinate of this TerrainPoint.
	 * @param z Z-coordinate of this TerrainPoint.
	 */
	public Biotex(float x, float y, float z) {
		this.vertex = new Vertex(x, y, z);
		this.biomix = new Biomix();
	}

	/**
	 * Returns the Biomix associated with this Biotex.
	 * 
	 * @return The Biomix.
	 */
	public Biomix getBiomix() {
		return this.biomix;
	}

	/**
	 * Returns the Colour of this Biotex.
	 * 
	 * @return The Colour.
	 */
	public Colour getColour() {
		return this.colour;
	}

	/**
	 * Sets the Colour of this Biotex to the given Option.
	 * 
	 * @param colour The new Colour of this Biotex.
	 */
	public void setColour(Colour colour) {
		this.colour = colour;
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The Vertex representing the location of this Biotex.
	 */
	private Vertex vertex;

	/**
	 * The Biome influences on this Biotex.
	 */
	private Biomix biomix;

	/**
	 * The colour of this Biotex.
	 */
	private Colour colour;

	/**
	 * Returns a String representation of this TerrainPoint.
	 */
	public String toString() {
		return String.format("%s with Colour %s", vertex, colour);
	}
}