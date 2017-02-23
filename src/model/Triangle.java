package model;

import java.util.ArrayList;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Triangle</b> class.</p>
 */
public class Triangle {

	private float[] colour;
	private final float[] BASE_COLOUR;
	private final TerrainPoint[] POINTS;

	public Triangle(TerrainPoint ... points) {
		if (points.length != 3) throw new IllegalArgumentException("Triangle must have 3 points.");

		this.BASE_COLOUR = Colour.triangleColour(points);
		this.POINTS = points;

		this.colour = new float[4];

		System.arraycopy(this.BASE_COLOUR, 0, this.colour, 0, this.BASE_COLOUR.length);
	}

	public float[] getBaseColour() {
		return BASE_COLOUR;
	}

	public float[] getColour() {
		return colour;
	}

	public AlgVector3D getNormal() {
		AlgVector3D vectorA, vectorB;

		vectorA = new AlgVector3D(POINTS[0]).to(new AlgVector3D(POINTS[1]));
		vectorB = new AlgVector3D(POINTS[0]).to(new AlgVector3D(POINTS[2]));

		return vectorA.cross(vectorB);
	}

	public Point[] getPoints() {
		return POINTS;
	}

	public void setColour(float[] colour) {
		this.colour = colour;
	}

	public void updateColours(ArrayList<LightSource> lights) {
		this.colour = Colour.triangleColour(POINTS);

		float averageZ = 0;
		for (Point p : POINTS) averageZ += p.getZ();
		averageZ /= 3f;

		for (int c = 0 ; c < colour.length ; c++) {
			colour[c] *= averageZ*10;
		}

		// Brightness
		float brightScale = 0;
		for (LightSource light : lights) {
			AlgVector3D lightVector = new AlgVector3D(light.getPosition());
			float angle = getNormal().angle(lightVector);
			
			brightScale += calculateBrightness(angle);
			brightScale = Math.min(1f, brightScale);
		}

		this.setColour(Colour.scaleColour(brightScale, colour));
	}

	public String toString() {
		return String.format("%s <--> %s <--> %s - R%.2f G%.2f B%.2f",
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