package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Tundra</b> class.</p>
 */ 
public class Tundra extends Terrain {

	private static final float COLOUR_VARIANCE = 0.005f;
	private static final String NAME = "Tundra";

	public Tundra(Grid grid) {
		super(
			grid,
			NAME,
			6,
			10,
			0.1f,
			0.05f,
			new float[] {0.95f, 0.95f, 0.95f}
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

			float monoColour = (float) (Math.random()*COLOUR_VARIANCE*2) - COLOUR_VARIANCE;
			//float monoColour = 0;

			for (int c = 0 ; c < colour.length ; c++) {
				colour[c] += monoColour;
				colour[c] = Math.min(1, colour[c]*(averageZ + 1)/2*1.9f);
				colour[c] = (float) Math.pow(colour[c], 3);
			}

			t.setColour(colour);
		}
	}

}