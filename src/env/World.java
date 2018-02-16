package env;

import java.util.ArrayList;

import core.Top;
import geo.Vertex;
import geo.TerrainTriangle;
import util.BiomeMap;
import util.Render;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <i>World</i> class represents the world encapsulating the landscape.</p>
 */
public class World {
	/**
	 * Number of Vertex rows in the World.
	 */
	public static final int ROWS = Top.DEBUG ? 100 : 300;
	/**
	 * Number of Vertex columns in the World.
	 */
	public static final int COLS = ROWS;

	/**
	 * Number of Perlin rows in the World.
	 */
	public static final int PERLIN_ROWS = 10;
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


	private Platform platform;
	/**
	 * List of Grid constituting the landscape of the World.
	 */
	private ArrayList<Grid> landscape;
	/**
	 * List of World light sources.
	 */
	private ArrayList<LightSource> lights;
	/**
	 * List of Drawable entities to render.
	 */
	private ArrayList<Drawable> drawables;

	/**
	 * Constructs a World object that is parameterized by the global static members of this class.
	 */
	public World() {
		landscape = new ArrayList<>();
		lights = new ArrayList<>();
		drawables = new ArrayList<>();

		platform = new Platform(MIN_X, MAX_X, MIN_Y, MAX_Y, PLATFORM_MIN_Z, PLATFORM_MAX_Z);

		Grid land = new Grid(ROWS, COLS, PERLIN_ROWS, PERLIN_COLS, MIN_X, MIN_Y, MAX_X, MAX_Y);
		BiomeMap landMap = land.getBiomeMap();

		// Populate the World surface with various Biome regions
		landMap.setSymbols(0, 0, landMap.getRows(), landMap.getCols(), 'H');
		landMap.setSymbolsWave(landMap.getRows()*2/3, 0, landMap.getRows(), landMap.getCols()/3, 'P');
		landMap.setSymbolsWave(landMap.getRows()/2, landMap.getCols()*2/3, landMap.getRows(), landMap.getCols(), 'D');
		landMap.setSymbolsWave(0, 0, landMap.getRows()/3, landMap.getCols()/4, 'T');
		landMap.setSymbolsWave(0, landMap.getCols()/2, landMap.getRows()/2, landMap.getCols(), 'M');
		landscape.add(land);

		lights.add(new LightSource(new Vertex(-3f, -3f, 3f)));

		// Generate the Points of each World surface and adjust the terrain lighting
		for (Grid grid : landscape) {
			grid.buildPoints();

			for (TerrainTriangle t : grid.getTriangles()) {
				t.updateColours(lights);
			}
		}

		drawables.add(platform);
		drawables.addAll(landscape);
		drawables.addAll(lights);
	}

	/**
	 * Returns a list of Drawable entities in this World.
	 * 
	 * @return a list of Drawable entities in this World.
	 */
	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

	/**
	 * Returns a list of Grids that constitute this World.
	 * 
	 * @return A list of Grids that constitute this World.
	 */
	public ArrayList<Grid> getLandscape() {
		return landscape;
	}

	/**
	 * Returns a list of LightSources that are present in this World.
	 * 
	 * @return A list of LightSources that are present in this World.
	 */
	public ArrayList<LightSource> getLights() {
		return lights;
	}

	public void render() {
		if (Top.DEBUG) {
			Render.drawAxes();
		}

		for (Drawable d : drawables) {
			d.draw();
		}
	}
}