package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Wetland</b> class.</p>
 */ 
public class Wetland extends Terrain {

	private static final float COLOUR_VARIANCE = 0.05f;
	private static final String NAME = "Wetland";

	public Wetland(Grid grid) {
		super(
			grid,
			NAME,
			6,
			6,
			-0.05f,
			0.2f,
			new float[] {0.05f, 0.10f, 0.0f}
		);
	}

	public void updateColours() {
		for (Triangle t : GRID.getTriangles()) {
			float[] baseColour = t.getBaseColour();
			float[] colour = new float[baseColour.length];

			System.arraycopy(baseColour, 0, colour, 0, baseColour.length);

			for (int c = 0 ; c < colour.length ; c++) {
				colour[c] -= (float) (Math.random()*COLOUR_VARIANCE);
			}

			t.setColour(colour);
		}
	}

}