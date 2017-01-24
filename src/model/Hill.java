package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Hill</b> class.</p>
 */ 
public class Hill extends Terrain {

	private static final float COLOUR_VARIANCE = 0.005f;
	private static final String NAME = "Hill";

	public Hill(Grid grid) {
		super(
			grid,
			NAME,
			3,
			6,
			0.1f,
			0.6f,
			new float[] {0.3f, 0.6f, 0.0f}
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

			for (int c = 0 ; c < colour.length ; c++) {
				colour[c] += (float) (Math.random()*COLOUR_VARIANCE*2) - COLOUR_VARIANCE;
				colour[c] = Math.min(1, colour[c]*(averageZ + 1)/2*1.6f);
				colour[c] = (float) Math.pow(colour[c], 3);
			}

			t.setColour(colour);
		}
	}

}