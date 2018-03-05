package util;

import java.util.*;
import bio.Biotex;
import env.*;
import geo.*;
import geo.Vector;

/**
 * @author  Mikhail Andrenkov
 * @since   March 4, 2018
 * @version 1.1
 *
 * <p>The <b>Noiseform</b> class represents a noise transform that is applied
 * to Grid objects.  Specifically, this class uses Perlin noise to distort
 * a given Grid and then texturizes the Biotices of the given Grid to reflect
 * the influence of nearby Biomes.</p>
 */
public class Noiseform {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Noiseform with the given Grid and the specified number of 
	 * Perlin rows and columns.
	 * 
	 * @param grid The Grid to be associated with this Noiseform.
	 * @param rows The number of Perlin rows.
	 * @param cols The number of Perlin columns.
	 */
	public Noiseform(Grid grid, int rows, int cols) {
		this.grid = grid;
		this.rows = rows;
		this.cols = cols;

		this.gradients = new Vector[rows + 1][cols + 1];
		for (int row = 0; row <= rows; ++row) {
			for (int col = 0; col <= cols; ++col) {
				double angle = 2*Math.PI*Math.random();
				float x = (float) Math.cos(angle);
				float y = (float) Math.sin(angle);
				this.gradients[row][col] = new Vector(x, y);
			}
		}
	}

	public void apply() {
		this.disturb();
		this.alias();
		this.texture();
	}

	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The Grid associated with this Noiseform.
	 */
	private Grid grid;

	/**
	 * The number of Perlin rows to be used in this Noiseform.
	 */
	private int rows;

	/**
	 * The number of Perlin columns to be used in this Noiseform.
	 */
	private int cols;

	/**
	 * The map of Perlin gradients used to generate the Perlin noise.
	 */
	private Vector[][] gradients;


	/**
	 * Avoids division by zero when mapping Points to the Perlin grid.
	 */
	private static final float PERLIN_EPSILON = (float) 1E-5;
	/**
	 * Determines the radius of influence of a Point during scaling and Biome calculations.
	 */
	private static final float BIOME_CREEP = 0.05f;


	/**
	 * Returns the weighted average corresponding to the given parameters.
	 *
	 * @param x Value X
	 * @param y Value Y
	 * @param w Weight corresponding to Y; w should fall within the range [0, 1]
	 * @return The weighted average.
	 */
	private static float weightAvg(float x, float y, float w) {
		return x + w*(y - x);
	}

	/**
	 * Maps the given value to a smooth curve with range [0, 1].
	 *
	 * @param v Value to be mapped to the curve; v should shall within the range [0, 1]
	 * @return The curved value of v.
	 */
	private static float unitCurve(float v) {
		return (float) (1f - Math.cos(v*Math.PI))/2f;
	}

	/**
	 * Applies a Perlin noise transformation to the Grid associated with this Noiseform.
	 */
	private void disturb() {
		// Conversion ratios from Grid dimensions to Perlin grid dimensions
		float perlinXSize = grid.getPerlinCols()/(grid.getBounds()[2] - grid.getBounds()[0] + PERLIN_EPSILON);
		float perlinYSize = grid.getPerlinRows()/(grid.getBounds()[3] - grid.getBounds()[1] + PERLIN_EPSILON);

		for (int x = 0 ; x < grid.getCols() ; x++) {
			for (int y = 0 ; y < grid.getRows() ; y++) {
				TerrainPoint p = grid.getPoint(y, x);
				HashMap<TerrainPoint, Float> nearbyPoints = findNearbyPoints(grid, x, y, BIOME_CREEP);

				int xCell = (int) ((p.getX() - grid.getBounds()[0])*perlinXSize );
				int yCell = (int) ((p.getY() - grid.getBounds()[1])*perlinYSize);

				float zInit  = p.getZ();
				float zDelta = generatePerlinDelta(grid, gradients, x, y, perlinXSize, perlinYSize);
				float zScale = generatePerlinScale(nearbyPoints);

				// Tie points near grid boundaries to ground elevation
				if (xCell == 0 || xCell == grid.getPerlinCols() - 1 || yCell == 0 || yCell == grid.getPerlinRows() - 1) {
					float minX = Math.min(p.getX() - grid.getBounds()[0], grid.getBounds()[2] - p.getX());
					float minY = Math.min(p.getY() - grid.getBounds()[1], grid.getBounds()[3] - p.getY());
					float minScale = Math.min(minX*perlinXSize, minY*perlinYSize);

					zScale *= unitCurve(minScale);
					zInit *= unitCurve(minScale);
				}

				float[] colour = Colour.averageColour(nearbyPoints.keySet().toArray(new TerrainPoint[nearbyPoints.size()]));
				HashMap<Biome, Float> biomeMix = generateBiomeMix(nearbyPoints);

				p.setBiomeMix(biomeMix);
				p.setColour(colour);
				p.setZ(zInit + zDelta*zScale);
			}
		}
	}

	/**
	 * Maps Points near a center Point to a corresponding distance weight.
	 *
	 * @param grid Grid containing the Points.
	 * @param centerX X coordinate of the center Point.
	 * @param centerY Y coordinate of the center Point.
	 * @param tolerance Value denoting the Point distance tolerance (i.e., a Point with a distance that exceeds the tolerance is not included in the map).
	 * @return A mapping between nearby Points and their associated distance weights.
	 */
	private static HashMap<TerrainPoint, Float> findNearbyPoints(Grid grid, int centerX, int centerY, float tolerance) {
		tolerance *= Math.min(grid.getRows(), grid.getCols());
		int maxDim = (int) tolerance;

		// Nearby Point index bounds
		int startX = Math.max(centerX - maxDim, 0);
		int endX   = Math.min(centerX + maxDim, grid.getCols() - 1);
		int startY = Math.max(centerY - maxDim, 0);
		int endY   = Math.min(centerY + maxDim, grid.getRows() - 1);

		TerrainPoint centerP = grid.getPoint(centerY, centerX);
		HashMap<TerrainPoint, Float> points = new HashMap<>();

		// Iterate through each candidate Point and add it to the map if it is sufficiently close to the center Point
		for (int x = startX ; x <= endX ; x++) {
			for (int y = startY ; y <= endY ; y++) {
				if (x == centerX || y == centerY) continue;
				float distance = centerP.distance(grid.getPoint(y, x));

				if (distance <= tolerance) {
					float weight = (float) Math.pow(1f - unitCurve(distance/tolerance), 0.8);
					points.put(grid.getPoint(y, x), weight);
				}
			}
		}

		return points;
	}

	private static HashMap<Biome, Float> generateBiomeMix(HashMap<TerrainPoint, Float> nearbyPoints) {
		HashMap<Biome, Float> biomeMix = new HashMap<>();

		float weightSum = 0f;

		for (TerrainPoint point : nearbyPoints.keySet()) {
			Biome biome = point.getBiome();
			float weight = nearbyPoints.get(point);

			biomeMix.putIfAbsent(biome, 0f);
			biomeMix.put(biome, biomeMix.get(biome) + weight);

			weightSum += weight;
		}

		for (Biome biome : biomeMix.keySet()) {
			biomeMix.replace(biome, biomeMix.get(biome)/weightSum);
		}

		return biomeMix;
	}

	/**
	 * Returns the change in elevation of the given Point with respect to the Perlin gradient array.
	 *
	 * @param grid Grid containing the Point.
	 * @param gradients Array of normalized vectors.
	 * @param x X coordinate of the Point.
	 * @param y Y coordinate of the Point.
	 * @param perlinXSize Number of Perlin columns per unit distance along the Grid's X dimension.
	 * @param perlinYSize Number of Perlin rows per unit distance along the Grid's Y dimension.
	 * @return The change in elevation of the given Point.
	 */
	private static float generatePerlinDelta(Grid grid, GeoVector[][] gradients, int x, int y, float perlinXSize, float perlinYSize) {
		TerrainPoint p = grid.getPoint(y, x);

		// Transform Grid coordinate system to Perlin coordinate system
		float xExact = (p.getX() - grid.getBounds()[0])*perlinXSize;
		float yExact = (p.getY() - grid.getBounds()[1])*perlinYSize;

		int xCell = (int) xExact;
		int yCell = (int) yExact;

		float xDiff = xExact - xCell;
		float yDiff = yExact - yCell;

		float weightX = unitCurve(xDiff);
		float weightY = unitCurve(yDiff);

		// Compute the dot products from each corner of the corresponding Perlin cell to the Point
		float[][] dotProducts = new float[2][2];
		dotProducts[0][0] = gradients[xCell    ][yCell    ].dot(new GeoVector(xDiff    , yDiff    ));
		dotProducts[0][1] = gradients[xCell    ][yCell + 1].dot(new GeoVector(xDiff    , yDiff - 1));
		dotProducts[1][1] = gradients[xCell + 1][yCell + 1].dot(new GeoVector(xDiff - 1, yDiff - 1));
		dotProducts[1][0] = gradients[xCell + 1][yCell    ].dot(new GeoVector(xDiff - 1, yDiff    ));

		// Average each dot product along the X dimension
		float v1 = weightAvg(dotProducts[0][0], dotProducts[1][0], weightX);
		float v2 = weightAvg(dotProducts[0][1], dotProducts[1][1], weightX);

		// Average the weighted averages along the Y dimension
		return weightAvg(v1, v2, weightY);
	}

	/**
	 * Compute the elevation scaling factor from the given Points.
	 *
	 * @param nearbyPoints Mapping of Points to distance weights.
	 * @return The elevation scaling factor.
	 */
	private static float generatePerlinScale(HashMap<TerrainPoint, Float> nearbyPoints) {
		float average = 0;

		for (TerrainPoint point : nearbyPoints.keySet()) {
			average += point.getBiome().getScale() * nearbyPoints.get(point);
		}

		return average / nearbyPoints.size();
	}

	/**
	 * Remove prominent edges from the Grid of this Noiseform by averaging the
	 * elevations of nearby Biotices.
	 */
	private void alias() {
		int dist = 2;
		int rows = this.grid.getRows();
		int cols = this.grid.getColumns();

		float[][] sum = new float[rows][cols];
		int[][] count = new int  [rows][cols];

		// Compute the sum and quantity of the heights of nearby Biotices.
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				float z = this.grid.getBiotex(row, col).getZ();

				for (int r = Math.max(0, row - dist); r <= Math.min(rows - 1, row + dist); ++r) {
					for (int c = Math.max(0, col - dist); c <= Math.min(cols - 1, col + dist); ++c) {
						// Verify that the current Biotex is close to the reference Biotex.
						if (Math.abs(row - r) + Math.abs(col - c) <= dist) {
							sum[r][c] += z;
							++count[r][c];
						}
					}	
				}
			}
		}

		// Set the elevation of each Grid Biotex to the average elevation of nearby Biotices.
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				float avg = sum[row][col]/count[row][col];
				Biotex biotex = this.grid.getBiotex(row, col);
				biotex.setZ(avg);
			}
		}
	}

	/**
	 * Apply the weighted texturing of each Biotex.
	 */
	private void texture() {
		for (int row = 0; row < this.grid.getRows(); ++row) {
			for (int col = 0; col < this.grid.getColumns(); ++col) {
				this.grid.getBiotex(row, col).texturize();
			}
		}
	}
}