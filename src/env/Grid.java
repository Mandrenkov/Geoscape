package env;

import java.util.ArrayList;

import geo.Point;
import geo.TerrainPoint;
import geo.TerrainTriangle;
import util.BiomeMap;
import util.Noise;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Grid</b> class.</p>
 */
public class Grid {

	private final int ROWS, COLS;
	private final int PERLIN_ROWS, PERLIN_COLS;
	private final float[] BOUNDS = new float[4];

	private BiomeMap biomeMap;
	private TerrainPoint[][]  points;
	private ArrayList<TerrainTriangle> terrainTriangles;

	public Grid(int rows, int cols, int perlinRows, int perlinCols, float minX, float minY, float maxX, float maxY) {
		this.ROWS = rows; //(int) (World.ROWS*(maxY - minY)/World.RANGE_X);
		this.COLS = cols; //(int) (World.COLS*(maxX - minX)/World.RANGE_Y);

		this.PERLIN_ROWS = perlinRows;
		this.PERLIN_COLS = perlinCols;

		BOUNDS[0] = minX;
		BOUNDS[1] = minY;
		BOUNDS[2] = maxX;
		BOUNDS[3] = maxY;

		biomeMap = new BiomeMap(ROWS, COLS);
		points = new TerrainPoint[ROWS][COLS];
		terrainTriangles = new ArrayList<>();
	}

	public void addTriangle(TerrainTriangle t) {
		terrainTriangles.add(t);
	}

	public void buildPoints() {
		this.initPoints();
		this.initTriangles();

		Noise.generateNoise(this);
	}

	public BiomeMap getBiomeMap() {
		return biomeMap;
	}

	public float[] getBounds() {
		return BOUNDS;
	}

	public int getCols() {
		return COLS;
	}

	public int getPerlinCols() {
		return PERLIN_COLS;
	}

	public int getPerlinRows() {
		return PERLIN_ROWS;
	}

	public TerrainPoint getPoint(int row, int col) {
		return points[row][col];
	}

	public Point[][] getPoints() {
		return points;
	}

	public int getRows() {
		return ROWS;
	}

	public ArrayList<TerrainTriangle> getTriangles() {
		return terrainTriangles;
	}

	public boolean inBounds(int row, int col) {
		return 0 <= row && row < ROWS && 0 <= col && col < COLS;
	}

	public void setPoint(TerrainPoint point, int row, int col) {
		points[row][col] = point;
	}

	public String toString() {
		return String.format("Grid from (%.2f, %.2f) to (%.2f, %.2f)", BOUNDS[0], BOUNDS[1], BOUNDS[2], BOUNDS[3]);
	}

	private void initPoints() {
		for (int row = 0 ; row < ROWS; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				float x = BOUNDS[0] + col*(BOUNDS[2] - BOUNDS[0])/(COLS - 1);
				float y = BOUNDS[1] + row*(BOUNDS[3] - BOUNDS[1])/(ROWS - 1);
				float z = 0.1f;

				Biome biome = biomeMap.getBiome(row, col);

				this.setPoint(new TerrainPoint(biome, x, y, z), row, col);
			}
		}
	}

	private void initTriangles() {
		TerrainPoint[] points = new TerrainPoint[COLS*2];

		for (int row = 0 ; row < ROWS - 1 ; row ++) {
			int pointIndex = 0;

			for (int col = 0 ; col < COLS ; col++) {
				points[pointIndex++] = this.getPoint(row    , col);
				points[pointIndex++] = this.getPoint(row + 1, col);
			}

			boolean flip = false;

			for (int p = 0 ; p < pointIndex - 2 ; p ++) {
				if (flip = !flip) this.addTriangle(new TerrainTriangle(points[p    ], points[p + 1], points[p + 2]));
				else              this.addTriangle(new TerrainTriangle(points[p + 2], points[p + 1], points[p    ]));
			}
		}
	}
}