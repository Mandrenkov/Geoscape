package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Plateau</b> class.</p>
 */ 
public class Plateau extends Terrain {

	private static final float COLOUR_VARIANCE = 0.005f;
	private static final String NAME = "Plateau";

	public Plateau(Grid grid) {
		super(
			grid,
			NAME,
			1,
			1,
			(float) (Math.random()*0.1),
			0.3f,
			new float[] {0.1f, 0.2f, 0.1f}
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

			float scaleFactor = (float) Math.pow(Math.min(1, colour[0]*(averageZ + 1)/2*9f), 3)/colour[0];

			for (int c = 0 ; c < colour.length ; c++) {
				colour[c] += (float) (Math.random()*COLOUR_VARIANCE*2) - COLOUR_VARIANCE;
				colour[c] *= scaleFactor;
			}

			t.setColour(colour);
		}
	}

}