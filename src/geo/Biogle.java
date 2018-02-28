package geo;

import java.util.ArrayList;
import env.Colour;
import env.Light;

/**
 * @author Mikhail Andrenkov
 * @since February 27, 2018
 * @version 1.1
 *
 * <p>The <b>Biogle</b> class represents a Biome Triangle.</p>
 */
public class Biogle extends Triangle {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Biogle using the given Biotices.
	 * 
	 * @param biotices The Biotices comprising this Biogle.
	 */
	public Biogle(Biotex... biotices) {
		super((Vertex[]) biotices);
		this.biotices = biotices;
		this.colour = Biotex.averageColour(biotices);
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
	 * Illuminates this Biogle with respect to the given Lights.
	 * 
	 * @param lights The Lights illuminating this Biogle.
	 */
	public void illuminate(ArrayList<Light> lights) {
		this.colour = Biotex.averageColour(this.biotices);

		// Scale the colour using the elevation of the Biogle.
		// The 10x multiplier is completely arbitrary.
		float scaleZ = 10*Vertex.averageZ(vertices);

		// Cache the middle Vertex and normal of this Biogle.
		Vertex middle = getMiddle();
		Vector normal = getNormal();

		// Scale the Colour using Lambert's cosine law.
		float scaleLight = 0f;
		for (Light light : lights) {
			Vector los = new Vector(middle, light.getPosition());
			float angle = normal.angle(los);

			// An angle approaching PI/2 achieves a maximum brightness factor.
			// An angle approaching k*PI achieves a minimum brightness factor.
			float cosine = (float) (Math.sin(angle))/2f;
			scaleLight += cosine*cosine;
		}

		float scalar = scaleZ*scaleLight;
		this.colour.scale(scalar);
	}

	/**
	 * Returns a String representation of this Biogle.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("%s with Colour %s", super.toString(), colour);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The Biotices comprising this Biogle.
	 */
	private Biotex[] biotices;

	/**
	 * The colour of this Biogle.
	 */
	private Colour colour;
}