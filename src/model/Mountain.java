package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Mountain</b> class.</p>
 */ 
public class Mountain extends Terrain {

	private static final float COLOUR_VARIANCE = 0.01f;
	private static final String NAME = "Mountain";

	public Mountain(Grid grid) {
		super(
			grid,
			NAME,
			2,
			3,
			0.2f,
			3.00f,
			new float[] {0.2f, 0.1f, 0.0f}
		);
	}

	public void updateColours() {
		for (Triangle t : GRID.getTriangles()) {
			float[] baseColour = t.getBaseColour();
			float[] colour = new float[baseColour.length];

			System.arraycopy(baseColour, 0, colour, 0, baseColour.length);

			float averageZ = 0;
			for (Point p : t.getPoints()) averageZ += p.getZ();
			averageZ /= 3f;

			float scaleFactor = (float) Math.pow(Math.min(1, colour[0]*(averageZ + 1)/2*4.5f), 3)/colour[0];

			for (int c = 0 ; c < colour.length ; c++) {
				colour[c] += (float) (Math.random()*COLOUR_VARIANCE*2) - COLOUR_VARIANCE;
				colour[c] *= scaleFactor;

				if (averageZ > 0.4f) {
					colour[c] += (averageZ - 0.4f)/0.6f;
				}
			}



			/*
			if (averageZ > 0.6f) {
				float var = 1.0f - (float) (Math.random()*0.1f);

				for (int c = 0 ; c < colour.length ; c++) {
					colour[c] = var - COLOUR_VARIANCE/2f;
				}
			}*/

			t.setColour(colour);
		}
	}

}