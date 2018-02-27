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
public class Biotex extends Vertex {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Biotex representing the given 3D coordinate.
	 * 
	 * @param x The X-coordinate of this Biotex.
	 * @param y The Y-coordinate of this Biotex.
	 * @param z The Z-coordinate of this Biotex.
	 */
	public Biotex(float x, float y, float z) {
		super(x, y, z);
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
	 * Sets the Colour of this Biotex to the given Colour.
	 * 
	 * @param colour The new Colour of this Biotex.
	 */
	public void setColour(Colour colour) {
		this.colour = colour;
	}

	/**
	 * Returns a String representation of this Biotex.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("%s with Colour %s", super.toString(), colour);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The Biome influences on this Biotex.
	 */
	private Biomix biomix;

	/**
	 * The colour of this Biotex.
	 */
	private Colour colour;
}