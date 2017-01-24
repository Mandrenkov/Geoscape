package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Lake</b> class.</p>
 */ 
public class Lake extends Terrain {

	private static final float COLOUR_VARIANCE = 0.005f;
	private static final String NAME = "Lake";

	public Lake(Grid grid) {
		super(
			grid,
			NAME,
			1,
			1,
			(float) (Math.random()*0.2) -0.10f,
			0.005f,
			new float[] {0.0f, 0.3f, 0.4f}
		);
	}

	public void updateColours() {
		for (Triangle t : GRID.getTriangles()) {
			float[] baseColour = t.getBaseColour();
			float[] colour = new float[baseColour.length];

			System.arraycopy(baseColour, 0, colour, 0, baseColour.length);

			for (int c = 0 ; c < colour.length ; c++) {
				colour[c] += (float) (Math.random()*COLOUR_VARIANCE*2) - COLOUR_VARIANCE;
			}

			t.setColour(colour);
		}
	}

}