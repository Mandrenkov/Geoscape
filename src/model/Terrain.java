package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Terrain</b> class.</p>
 */ 
public abstract class Terrain {

	protected final Grid GRID;

	private final String BIOME;

	private final static char SYMBOL = '/';
	private final int PERLIN_ROWS;
	private final int PERLIN_COLS;
	private final float ELEVATION_BASE;
	private final float ELEVATION_SCALE;
	private final float[] BASE_COLOUR;

	public Terrain(Grid grid, String biome, int perlinRows, int perlinCols, float elevationBase, float elevationScale, float[] baseColour) {
		this.GRID = grid;

		this.BIOME = biome;
		this.PERLIN_ROWS     = perlinRows;
		this.PERLIN_COLS     = perlinCols;
		this.ELEVATION_BASE  = elevationBase;
		this.ELEVATION_SCALE = elevationScale;
		this.BASE_COLOUR     = baseColour;
	}

	public static char getSymbol() {
		return SYMBOL;
	};

	public float[] getBaseColour() {
		return BASE_COLOUR;
	}

	public Grid getGrid() {
		return GRID;
	}

	public int getPerlinCols() {
		return PERLIN_COLS;
	}

	public int getPerlinRows() {
		return PERLIN_ROWS;
	}

	public float getElevationBase() {
		return ELEVATION_BASE;
	}

	public float getElevationScale() {
		return ELEVATION_SCALE;
	}

	public boolean is(String type) {
		return this.BIOME.equals(type);
	}

	public void resetPoints() {
		for (int row = 0 ; row < GRID.ROWS ; row ++) {
			for (int col = 0 ; col < GRID.COLS ; col++) {
				Point p = GRID.getPoint(row, col);
				p.setZ(ELEVATION_BASE);
			}
		}
	}

	public String toString() {
		return BIOME + " Biome: " + GRID.toString();
	}

	public abstract void updateColours();
}