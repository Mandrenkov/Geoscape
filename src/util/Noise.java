package util;

import java.util.*;

import env.*;
import geo.*;

/**
 * @author Mikhail Andrenkov
 * @since April 10, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Noise</b> class.</p>
 */
public class Noise {

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
	public static float weightAvg(float x, float y, float w) {
		return x + w*(y - x);
	}

	/**
	 * Maps the given value to a smooth curve with range [0, 1].
	 *
	 * @param v Value to be mapped to the curve; v should shall within the range [0, 1]
	 * @return The curved value of v.
	 */
	public static float unitCurve(float v) {
		return 1 - (float) (Math.cos(v*Math.PI) + 1)/2;
	}

	/**
	 * Applies a Perlin noise filter to the given Grid.
	 *
	 * @param grid Grid to be filtered
	 */
	public static void generateNoise(Grid grid) {
		GeoVector[][] gradients = createGradients(grid.getPerlinRows(), grid.getPerlinCols());
		applyNoise(grid, gradients);
		applyAliasing(grid);
		applyTexturing(grid);
	}

	/**
	 * Creates a 2D array of normalized vectors according to the given Grid's Perlin dimensions.
	 *
	 * @param grid Grid to be filtered
	 * @return The 2D array of normalized vectors.
	 */
	private static GeoVector[][] createGradients(int perlinRows, int perlinCols) {
		perlinRows++;
		perlinCols++;
		GeoVector[][] gradients = new GeoVector[perlinRows][perlinCols];

		for (int x = 0 ; x < perlinRows ; x++) {
			for (int y = 0 ; y < perlinCols ; y++) {
				float angle = (float) (Math.random() * 2 * Math.PI);

				float xComp = (float) Math.cos(angle);
				float yComp = (float) Math.sin(angle);

				gradients[x][y] = new GeoVector(xComp, yComp);
			}
		}

		return gradients;
	}

	/**
	 * Removes prominent Grid edges by averaging the elevations of nearby Points.
	 *
	 * @param grid Grid to be aliased.
	 */
	private static void applyAliasing(Grid grid) {
		float[][] pointZ = new float[grid.getRows()][grid.getCols()];

		// Calculate new Point elevations
		for (int r = 0 ; r < grid.getRows() ; r++) {
			for (int c = 0 ; c < grid.getCols() ; c++) {
				HashMap<TerrainPoint, Float> nearbyPoints = findNearbyPoints(grid, c, r, 0.01f);

				float averageZ = 0f;
				for (TerrainPoint p : nearbyPoints.keySet()) {
					averageZ += p.getZ();
				}
				averageZ /= nearbyPoints.size();

				pointZ[r][c] = averageZ;
			}
		}

		// Apply calculated elevations to the Grid Points
		for (int r = 0 ; r < grid.getRows() ; r++) {
			for (int c = 0 ; c < grid.getCols() ; c++) {
				grid.getPoint(r, c).setZ(pointZ[r][c]);;
			}
		}
	}

	/**
	 * Applies Perlin noise to the given Grid with respect to the gradients array and the Biome characteristics of the Points.
	 *
	 * @param grid Grid to be filtered.
	 * @param gradients Array of gradient vectors.
	 */
	private static void applyNoise(Grid grid, GeoVector[][] gradients) {
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
	 * Removes prominent Grid edges by averaging the elevations of nearby Points.
	 *
	 * @param grid Grid to be aliased.
	 */
	private static void applyTexturing(Grid grid) {
		for (int r = 0 ; r < grid.getRows() ; r++) {
			for (int c = 0 ; c < grid.getCols() ; c++) {
				TerrainPoint point = grid.getPoint(r, c);

				HashMap<Biome, Float> biomeMix = point.getBiomeMix();

				for (Biome biome : biomeMix.keySet()) {
					biome.texturize(point, biomeMix.get(biome));
				}
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

}