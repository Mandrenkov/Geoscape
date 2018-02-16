package geo;

import java.util.ArrayList;

import env.LightSource;
import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>TerrainTriangle</b> class.</p>
 */
public class TerrainTriangle extends Triangle {

	/**
	 * List of Points that constitute the TerrainTriangle.
	 */
	private final TerrainPoint[] POINTS;
	/**
	 * Colour of this TerrainTriangle.
	 */
	private float[] colour;
	/**
	 * Colour of the base Biome of this TerrainTriangle.
	 */
	private final float[] BASE_COLOUR;

	public TerrainTriangle(TerrainPoint ... points) {
		super(points);

		this.POINTS = points;
		this.BASE_COLOUR = Colour.averageColour(points);

		this.colour = new float[this.BASE_COLOUR.length];
		System.arraycopy(this.BASE_COLOUR, 0, this.colour, 0, this.colour.length);
	}
	
	
	/*** Public Methods ***/
	

	/**
	 * Returns the base colour of this TerrainTriangle.
	 * 
	 * @return The base colour of this TerrainTriangle.
	 */
	public float[] getBaseColour() {
		return BASE_COLOUR;
	}
	
	/**
	 * Returns the colour of this TerrainTriangle.
	 * 
	 * @return The colour of this TerrainTriangle.
	 */
	public float[] getColour() {
		return colour;
	}

	/**
	 * Sets the colour of this TerrainTriangle to the given colour.
	 * 
	 * @param colour The new colour of this TerainTriangle.
	 */
	public void setColour(float[] colour) {
		this.colour = colour;
	}

	/**
	 * Updates the colour of this TerrainTriangle with respect to the given LightSources.
	 * 
	 * @param lights The list of LightSources that may affect this TerrainTriangle.
	 */
	public void updateColours(ArrayList<LightSource> lights) {
		colour = Colour.averageColour(POINTS);

		float averageZ = 0;
		for (Vertex p : POINTS) averageZ += p.getZ();
		averageZ /= 3f;

		for (int c = 0 ; c < colour.length ; c++) {
			colour[c] *= averageZ*10;
		}

		// Brightness
		float brightScale = 0;
		for (LightSource light : lights) {
			GeoVector lightVector = new GeoVector(light.getPosition());
			float angle = getNormal().angle(lightVector);

			brightScale += calculateBrightness(angle);
			brightScale = Math.min(1f, brightScale);
		}

		this.setColour(Colour.scaleColour(brightScale, colour));
	}

	/**
	 * Returns a String representation of this TerrainTriangle.
	 */
	public String toString() {
		return String.format("%s: %s <--> %s <--> %s - R%.2f G%.2f B%.2f",
			this.getClass().getName(),
			POINTS[0],
			POINTS[1],
			POINTS[2],
			colour[0],
			colour[1],
			colour[2]
		);
	}
	
	
	/*** Private Methods ***/

	
	/**
	 * Returns the brightness scaling factor associated with the given angle.
	 * An angle approaching pi/2 radians achieves a maximum brightness factor.
	 * An angle approaching a multiple of pi radians achieves a minimum brightness factor.
	 * 
	 * @param angle The angle of the LightSource makes with the Triangle face.
	 * @return The brightness scaling factor.
	 */
	private float calculateBrightness(float angle) {
		float cosineFactor = (float) (-Math.cos(angle) + 1)/2f;
		 return (float) Math.pow(cosineFactor, 2);
	}
}