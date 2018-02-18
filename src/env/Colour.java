package env;

import static org.lwjgl.opengl.GL11.*;

import java.util.Arrays;
import java.util.function.Function;
import core.Logger;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2018
 * @version 1.1
 *
 * <p>The <i>Colour</i> class represents an RGBA colour.</p>
 */
public class Colour {

	// Public members
	// -------------------------------------------------------------------------

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
	 * Returns the average Colour represented by the given Colours.
	 * 
	 * @param clrs The Colours to average.
	 * 
	 * @return The average Colour.
	 */
	public static Colour average(Colour... clrs) {
		if (clrs.length == 0) {
			Logger.error("Cannot determine average of 0 colours.");
			return new Colour();
		}

		// Returns the average value of the given Colour component accessor function.
		Function<Function<Colour, Float>, Float> average = (Function<Colour, Float> supplier) -> {
			return (float) Arrays.stream(clrs).mapToDouble(clr -> supplier.apply(clr)).sum()/clrs.length;
		};

		return new Colour(average.apply(Colour::getRed),
						  average.apply(Colour::getGreen),
						  average.apply(Colour::getBlue),
						  average.apply(Colour::getAlpha));
	}

	/**
	 * Constructs an opaque black Colour object.
	 */
	public Colour() {
		this(0f, 0f, 0f, 1f);
	}

	/**
	 * Constructs an opaque Colour object with the given RGB components.
	 * 
	 * @param red   The red RGBA component.
	 * @param green The green RGBA component.
	 * @param blue  The blue RGBA component.
	 */
	public Colour(float red, float green, float blue) {
		this(red, green, blue, 1f);
	}

	/**
	 * Constructs a Colour object with the given RGBA components.
	 * 
	 * @param red   The red RGBA component.
	 * @param green The green RGBA component.
	 * @param blue  The blue RGBA component.
	 * @param alpha The alpha RGBA component.
	 */
	public Colour(float red, float green, float blue, float alpha) {
		this.red   = red;
		this.green = green;
		this.blue  = blue;
		this.alpha = alpha;
	}

	/**
	 * Returns the red component of this Colour.
	 * 
	 * @return The red component.
	 */
	public float getRed() {
		return red;
	}

	/**
	 * Returns the green component of this Colour.
	 * 
	 * @return The green component.
	 */
	public float getGreen() {
		return green;
	}

	/**
	 * Returns the blue component of this Colour.
	 * 
	 * @return The blue component.
	 */
	public float getBlue() {
		return blue;
	}

	/**
	 * Returns the alpha component of this Colour.
	 * 
	 * @return The alpha component.
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * Sets the GL colour to this Colour.
	 */
	public void glColor() {
		byte r = (byte) (getRed() - 128);
		byte g = (byte) (getGreen() - 128);
		byte b = (byte) (getBlue() - 128);
		glColor3b(r, g, b);
	}

	/**
	 * Scales the RGB components of this Colour by the given scaling factor.
	 */
	public void scale(float scale) {
		red   *= scale;
		green *= scale;
		blue  *= scale;
	}

	/**
	 * Returns a String representation of this Colour.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("(%d, %d, %d, %d)", getRed(), getGreen(), getBlue(), getAlpha());
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The red RGBA component of this Colour.
	 */
	private float red;

	/**
	 * The green RGBA component of this Colour.
	 */
	private float green;

	/**
	 * The blue RGBA component of this Colour.
	 */
	private float blue;

	/**
	 * The alpha RGBA component of this Colour.
	 */
	private float alpha;
}