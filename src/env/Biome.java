package env;

import core.Logger;
import env.Colour;
import geo.Biotex;

/**
 * @author Mikhail Andrenkov
 * @since March 4, 2018
 * @version 1.1
 *
 * <p>The <b>Biome</b> class represents a natural biome.  In the context of this
 * application, a Biome is a set of common characteristics shared by a group of
 * Biotices.</p>
 */
public class Biome {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * The Biome instance modelling a desert landform.
	 */
	public static final Biome DESERT = new Biome("Desert", new Colour(0.600f, 0.300f, 0.000f), 0.050f) {
		public void texturize(Biotex biotex, float scalar) {
			biotex.wave(10f, 0.05f, 300f, 0.001f*scalar);
		}
	};

	/**
	 * The Biome instance modelling a hill landform.
	 */
	public static final Biome HILL = new Biome("Hill", new Colour(0.200f, 0.400f, 0.000f), 0.500f) {
		public void texturize(Biotex biotex, float scalar) {
			biotex.shift(0.001f*scalar);
		}
	};

	/**
	 * The Biome instance modelling a mountain landform.
	 */
	public static final Biome MOUNTAIN = new Biome("Mountain", new Colour(0.150f, 0.100f, 0.000f), 1.500f) {
		public void texturize(Biotex biotex, float scalar) {
			biotex.shift(0.01f*scalar);
		}
	};
	/**
	 * The Biome instance modelling a plains landform.
	 */
	public static final Biome PLAIN = new Biome("Plains", new Colour(0.800f, 1.000f, 0.000f), 0.020f) {
		public void texturize(Biotex biotex, float scalar) {
			biotex.shift(0.0001f*scalar);
			biotex.getColour().shift(0.125f*scalar);
		}
	};
	
	/**
	 * The Biome instance modelling a tundra landform.
	 */
	public static final Biome TUNDRA = new Biome("Tundra", new Colour(1.000f, 1.000f, 1.000f), 0.100f) {
		public void texturize(Biotex biotex, float scalar) {
			biotex.shift(0.0001f*scalar);
			biotex.getColour().shift(0.1f*scalar);
		}
	};

	/**
	 * Returns the colour of this Biome.
	 *
	 * @return The colour.
	 */
	public Colour getColour() {
		return this.colour;
	}

	/**
	 * Returns the name of this Biome.
	 *
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the elevation scale of this Biome.
	 *
	 * @return The elevation scale.
	 */
	public float getScale() {
		return this.scale;
	}

	/**
	 * @brief Applies the texture representing this Biome to the given Biotex.
	 *        The extent of the texturing is controlled by the given scalar which
	 *        should fall within the range [0, 1].
	 * 
	 * @param biotex The Biotex to texturize.
	 * @param scalar The magnitude of the texturing.
	 */
	public void texturize(Biotex biotex, float scalar) {
		Logger.warn("Biome \"%s\" does not implement Biome::texturize().", this.name);
	}

	/**
	 * Returns a String representation of this Biome.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("%s [Colour: %s, Scale: %.2f]", this.name, this.colour.toString(), this.scale);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The colour of this Biome.
	 */
	private Colour colour;

	/**
	 * The name of this Biome.
	 */
	private String name;

	/**
	 * The elevation scale of this Biome.
	 */
	private float scale;

	/**
	 * Constructs a Biome object with the specified parameters.
	 *
	 * @param name   The name of this Biome.
	 * @param colour The base colour of this Biome.
	 * @param scale  The elevation scaling factor of this Biome.
	 */
	private Biome(String name, Colour colour, float scale) {
		this.name = name;
		this.colour = colour;
		this.scale = scale;
	}	
}