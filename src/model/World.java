package model;

import java.util.ArrayList;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>World</i> class represents the world encapsulating the landscape.</p>
 */
public class World {
	public static final int ROWS = 200;
	public static final int COLS = 200;

	public static final int PERLIN_ROWS = 8;
	public static final int PERLIN_COLS = 8;

	public static final float PADDING_X = 0.2f;
	public static final float PADDING_Y = 0.2f;
	public static final float MIN_X = -1 + PADDING_X;
	public static final float MIN_Y = -1 + PADDING_Y;
	public static final float MAX_X =  1 - PADDING_X;
	public static final float MAX_Y =  1 - PADDING_Y;
	public static final float RANGE_X = MAX_X - MIN_X;
	public static final float RANGE_Y = MAX_Y - MIN_Y;
	public static final float BOUNDARY_PADDING = 0.01f;

	public static final float BASE_MIN_Z = -0.30f;
	public static final float BASE_MAX_Z = 0.00f;

	private ArrayList<Grid> landscape;
	private ArrayList<LightSource> lights;

	public World() {
		landscape = new ArrayList<>();
		lights = new ArrayList<>();

		Grid land = new Grid(ROWS, COLS, PERLIN_ROWS, PERLIN_COLS, MIN_X, MIN_Y, MAX_X, MAX_Y);
		BiomeMap landMap = land.getBiomeMap();

		landMap.setSymbols(0, 0, landMap.getRows(), landMap.getCols(), 'H');
		landMap.setSymbolsWave(0, 0, landMap.getRows()/2, landMap.getCols()/3, 'T');
		landMap.setSymbolsWave(0, landMap.getCols()/2, landMap.getRows()/2, landMap.getCols(), 'M');
		landscape.add(land);
		
		lights.add(new LightSource(new Point(-1f, -1f, 0f)));
		//lights.add(new LightSource(new Point(-1f, 1f, 1f)));

		for (Grid grid : landscape) {
			grid.buildPoints();

			for (Triangle t : grid.getTriangles()) {
				t.updateColours(lights);
			}
		}
	}

	public ArrayList<Grid> getLandscape() {
		return this.landscape;
	}


}