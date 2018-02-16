package env;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.util.ArrayList;

import geo.Vertex;
import geo.TerrainPoint;
import geo.TerrainTriangle;
import util.BiomeMap;
import util.Noise;
import util.Render;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Grid</b> class.</p>
 */
public class Grid implements Drawable {

	/**
	 * Number of rows in this Grid.
	 */
	private final int ROWS;
	/**
	 * Number of columns in this Grid.
	 */
	private final int COLS;
	/**
	 * Number of Perlin rows in this Grid.
	 */
	private final int PERLIN_ROWS;
	/**
	 * Number of Perlin columns in this Grid.
	 */
	private final int PERLIN_COLS;
	/**
	 * Boundary coordinates of this Grid: [minimum X, minimum Y, maximum X, maximum Y] 
	 */
	private final float[] BOUNDS = new float[4];

	/**
	 * BiomeMap that represents the mapping of coordinates in this Grid to primary Biomes.
	 */
	private BiomeMap biomeMap;
	/**
	 * 2D-Array of TerrainPoints that constitute this Grid.
	 */
	private TerrainPoint[][]  points;
	/**
	 * Array of TerrainTriangles that constitute this Grid.
	 */
	private ArrayList<TerrainTriangle> terrainTriangles;

	/**
	 * Constructs a Grid object with the given row and column counts, in addition to the coordinate bounds.
	 * 
	 * @param rows Number of rows in this Grid.
	 * @param cols Number of columns in this Grid.
	 * @param perlinRows Number of Perlin rows in this Grid.
	 * @param perlinCols Number of Perlin columns in this Grid.
	 * @param minX Minimum X-coordinate in this Grid.
	 * @param minY Minimum Y-coordinate in this Grid.
	 * @param maxX Maximum X-coordinate in this Grid. 
	 * @param maxY Maximum Y-coordinate in this Grid.
	 */
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

	/**
	 * Adds the given TerrainTriangle to this Grid.
	 * 
	 * @param The TerrainTriangle to add to this Grid.
	 */
	public void addTriangle(TerrainTriangle t) {
		terrainTriangles.add(t);
	}

	/**
	 * Initializes the Points and Triangles that constitute this Grid.
	 */
	public void buildPoints() {
		this.initPoints();
		this.initTriangles();

		Noise.generateNoise(this);
	}

	/**
	 * Draws this Grid.
	 */
	public void draw() {
		// Polygon setup
		glPolygonMode(GL_FRONT, GL_FILL);
		glCullFace(GL_BACK);

		// Render triangles
		glBegin(GL_TRIANGLES);
			terrainTriangles.forEach(t -> Render.drawTriangle(t, t.getColour()));
		glEnd();
	}

	/**
	 * Returns the BiomeMap associated with this Grid.
	 * 
	 * @return This Grid's new BiomeMap.
	 */
	public BiomeMap getBiomeMap() {
		return biomeMap;
	}

	/**
	 * Returns the boundaries of this Grid.
	 * 
	 * @return The boundaries of this Grid.
	 */
	public float[] getBounds() {
		return BOUNDS;
	}

	/**
	 * Returns the number of columns in this Grid.
	 * 
	 * @return The number of columns in this Grid.
	 */
	public int getCols() {
		return COLS;
	}

	/**
	 * Returns the number of Perlin columns in this Grid.
	 * 
	 * @return The number of Perlin columns in this Grid.
	 */
	public int getPerlinCols() {
		return PERLIN_COLS;
	}

	/**
	 * Returns the number of Perlin rows in this Grid.
	 * 
	 * @return The number of Perlin rows in this Grid.
	 */
	public int getPerlinRows() {
		return PERLIN_ROWS;
	}

	/**
	 * Returns the TerrainPoint in this Grid located at the given coordinate.
	 * 
	 * @param row The X-component of the coordinate.
	 * @param col The Y-component of the coordinate.
	 * @return The TerrainPoint in this Grid.
	 */
	public TerrainPoint getPoint(int row, int col) {
		return points[row][col];
	}

	/**
	 * Returns the Points in this Grid.
	 * 
	 * @return The Points in this Grid.
	 */
	public Vertex[][] getPoints() {
		return points;
	}

	/**
	 * Returns the number of rows in this Grid.
	 * 
	 * @return The number of rows in this Grid.
	 */
	public int getRows() {
		return ROWS;
	}

	/**
	 * Returns the TerrainTriangles in this Grid.
	 * 
	 * @return The TerrainTriangles in this Grid.
	 */
	public ArrayList<TerrainTriangle> getTriangles() {
		return terrainTriangles;
	}

	/**
	 * Determines whether the given row and column fit within the bounds of this Grid.
	 * 
	 * @param row The row to be tested.
	 * @param col The column to be tested.
	 * @return True if the row and column reside in the Grid, otherwise false.
	 */
	public boolean inBounds(int row, int col) {
		return 0 <= row && row < ROWS && 0 <= col && col < COLS;
	}

	/**
	 * Sets the Point located at the given location to the given Point.
	 * 
	 * @param point The new Point at the specified location.
	 * @param row The row component of the location.
	 * @param col The column component of the location.
	 */
	public void setPoint(TerrainPoint point, int row, int col) {
		points[row][col] = point;
	}

	/**
	 * Returns a String representation of this Grid.
	 */
	public String toString() {
		return String.format("Grid from (%.2f, %.2f) to (%.2f, %.2f)", BOUNDS[0], BOUNDS[1], BOUNDS[2], BOUNDS[3]);
	}

	/***** Private Methods *****/

	/**
	 * Creates the Points in this Grid.
	 */
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

	/**
	 * Creates the Triangles in this Grid.
	 */
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