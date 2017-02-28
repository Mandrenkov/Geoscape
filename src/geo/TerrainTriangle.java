package geo;

import java.util.ArrayList;

import env.LightSource;
import util.Colour;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>TerrainTriangle</b> class.</p>
 */
public class TerrainTriangle extends Triangle {

	private float[] colour;
	private final float[] BASE_COLOUR;

	public TerrainTriangle(TerrainPoint ... points) {
		super(points);

		this.BASE_COLOUR = Colour.triangleColour(points);
		
		this.colour = new float[this.BASE_COLOUR.length];
		System.arraycopy(this.BASE_COLOUR, 0, this.colour, 0, this.colour.length);
	}

	public float[] getBaseColour() {
		return BASE_COLOUR;
	}

	public float[] getColour() {
		return colour;
	}

	public GeoVector getNormal() {
		GeoVector vectorA, vectorB;

		vectorA = new GeoVector(POINTS[0]).to(new GeoVector(POINTS[1]));
		vectorB = new GeoVector(POINTS[0]).to(new GeoVector(POINTS[2]));

		return vectorA.cross(vectorB);
	}

	public void setColour(float[] colour) {
		this.colour = colour;
	}

	public void updateColours(ArrayList<LightSource> lights) {
		System.arraycopy(this.BASE_COLOUR, 0, this.colour, 0, this.colour.length);

		float averageZ = 0;
		for (Point p : POINTS) averageZ += p.getZ();
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
	
	private float calculateBrightness(float angle) {
		float cosineFactor = (float) (-Math.cos(angle) + 1)/2f;
		 return (float) Math.pow(cosineFactor, 2);
	}
}