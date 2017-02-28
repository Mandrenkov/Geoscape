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
	/**
	 * Number of Point rows in the World.
	 */
	public static final int ROWS = 150;
	/**
	 * Number of Point columns in the World.
	 */
	public static final int COLS = ROWS;

	/**
	 * Number of Perlin rows in the World.
	 */
	public static final int PERLIN_ROWS = 20;
	/**
	 * Number of Perlin columns in the World.
	 */
	public static final int PERLIN_COLS = PERLIN_ROWS;
	/**
	 * Padding along the X dimension of the World.
	 */
	public static final float PADDING_X = 0.2f;
	/**
	 * Padding along the Y dimension of the World.
	 */
	public static final float PADDING_Y = 0.2f;
	/**
	 * Minimum X coordinate of the World.
	 */
	public static final float MIN_X = -1 + PADDING_X;
	/**
	 * Minimum Y coordinate of the World.
	 */
	public static final float MIN_Y = -1 + PADDING_Y;
	/**
	 * Maximum X coordinate of the World.
	 */
	public static final float MAX_X =  1 - PADDING_X;
	/**
	 * Maximum Y coordinate of the World.
	 */
	public static final float MAX_Y =  1 - PADDING_Y;
	/**
	 * Range of X coordinates in the World.
	 */
	public static final float RANGE_X = MAX_X - MIN_X;
	/**
	 * Range of Y coordinates in the World.
	 */
	public static final float RANGE_Y = MAX_Y - MIN_Y;
	/**
	 * Minimum Z coordinate of the World platform.
	 */
	public static final float PLATFORM_MIN_Z = -0.30f;
	/**
	 * Maximum Z coordinate of the World platform.
	 */
	public static final float PLATFORM_MAX_Z = 0.00f;

	
	/**
	 * List of Grid constituting the landscape of the World.
	 */
	private ArrayList<Grid> landscape;
	/**
	 * List of World light sources.
	 */
	private ArrayList<LightSource> lights;

	public World() {
		landscape = new ArrayList<>();
		lights = new ArrayList<>();

		Grid land = new Grid(ROWS, COLS, PERLIN_ROWS, PERLIN_COLS, MIN_X, MIN_Y, MAX_X, MAX_Y);
		BiomeMap landMap = land.getBiomeMap();

		landMap.setSymbols(0, 0, landMap.getRows(), landMap.getCols(), 'H');
		landMap.setSymbolsWave(landMap.getRows()/2, 0, landMap.getRows(), landMap.getCols()/3, 'P');
		landMap.setSymbolsWave(0, 0, landMap.getRows()/2, landMap.getCols()/4, 'T');
		landMap.setSymbolsWave(0, landMap.getCols()/2, landMap.getRows()/2, landMap.getCols(), 'M');
		landscape.add(land);
		
		lights.add(new LightSource(new Point(-1f, -1f, 1f)));
		//lights.add(new LightSource(new Point(-1f, 0f, 1f)));
		//lights.add(new LightSource(new Point(0f, 0f, 1f)));

		for (Grid grid : landscape) {
			grid.buildPoints();

			for (TerrainTriangle t : grid.getTriangles()) {
				t.updateColours(lights);
			}
		}
	}

	public ArrayList<Grid> getLandscape() {
		return landscape;
	}

	public ArrayList<LightSource> getLights() {
		return lights;
	}
}