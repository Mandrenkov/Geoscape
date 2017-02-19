package core;

import java.util.*;

import model.*;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>NoiseGen</b> class.</p>
 */
public class NoiseGen {

	private static final float EPSILON = (float) 1E-5;
	private static final float TOLERANCE = 5.0f;

	public static void generateNoise(Grid grid) {
		AlgVector2D[][] gradients = createGradients(grid);
		applyNoise(grid, gradients);
		refine(grid);
	}

	private static AlgVector2D[][] createGradients(Grid grid) {
		AlgVector2D[][] gradients = new AlgVector2D[grid.getPerlinCols() + 1][grid.getPerlinRows() + 1];

		for (int x = 0 ; x < grid.getPerlinCols() + 1 ; x++) {
			for (int y = 0 ; y < grid.getPerlinRows() + 1; y++) {
				float angle = (float) (Math.random() * 2 * Math.PI);

				float xComp = (float) Math.cos(angle);
				float yComp = (float) Math.sin(angle);

				gradients[x][y] = new AlgVector2D(xComp, yComp);
			}
		}

		return gradients;
	}

	private static void applyNoise(Grid grid, AlgVector2D[][] gradients) {
		float perlinXSize = grid.getPerlinCols()/(grid.getBounds()[2] - grid.getBounds()[0] + EPSILON);
		float perlinYSize = grid.getPerlinRows()/(grid.getBounds()[3] - grid.getBounds()[1] + EPSILON);

		for (int x = 0 ; x < grid.getCols() ; x++) {
			for (int y = 0 ; y < grid.getRows() ; y++) {
				TerrainPoint p = grid.getPoint(y, x);

				float xExact = (p.getX() - grid.getBounds()[0])*perlinXSize;
				float yExact = (p.getY() - grid.getBounds()[1])*perlinYSize;

				int xCell = (int) xExact;
				int yCell = (int) yExact;

				float xDiff = xExact - xCell;
				float yDiff = yExact - yCell;

				float weightX = smooth(xDiff);
				float weightY = smooth(yDiff);

				float[][] dotProducts = new float[2][2];

				dotProducts[0][0] = gradients[xCell    ][yCell    ].dot(new AlgVector2D(xDiff    , yDiff    ));
				dotProducts[0][1] = gradients[xCell    ][yCell + 1].dot(new AlgVector2D(xDiff    , yDiff - 1));
				dotProducts[1][1] = gradients[xCell + 1][yCell + 1].dot(new AlgVector2D(xDiff - 1, yDiff - 1));
				dotProducts[1][0] = gradients[xCell + 1][yCell    ].dot(new AlgVector2D(xDiff - 1, yDiff    ));

				float v1 = weightAvg(dotProducts[0][0], dotProducts[1][0], weightX);
				float v2 = weightAvg(dotProducts[0][1], dotProducts[1][1], weightX);

				HashMap<TerrainPoint, Float> nearbyWeightPoints = findNearbyPoints(grid, x, y, TOLERANCE);
				HashMap<Float, ArrayList<Float>> nearbyWeightScales = new HashMap<>();
				ArrayList<TerrainPoint> nearbyPoints = new ArrayList<>();

				for (TerrainPoint point : nearbyWeightPoints.keySet()) {
					nearbyPoints.add(point);

					float weight = nearbyWeightPoints.get(point);

					if (!nearbyWeightScales.containsKey(weight)) {
						nearbyWeightScales.put(weight, new ArrayList<Float>());
					}
					nearbyWeightScales.get(weight).add(point.getBiome().getScale());
				}

				float zScale = weightMapAverage(nearbyWeightScales);

				p.setColour(Colour.averageColour(nearbyPoints.toArray(new TerrainPoint[nearbyPoints.size()])));
				float zInit  = p.getZ();

				if (xCell == 0 || xCell == grid.getPerlinCols() - 1 || yCell == 0 || yCell == grid.getPerlinRows() - 1) {
					float minX = Math.min(p.getX() - grid.getBounds()[0], grid.getBounds()[2] - p.getX());
					float minY = Math.min(p.getY() - grid.getBounds()[1], grid.getBounds()[3] - p.getY());
					float minScale = Math.min(minX*perlinXSize, minY*perlinYSize);

					zScale *= smooth(minScale);
					zInit *= smooth(minScale);
				}

				p.setZ(zInit + weightAvg(v1, v2, weightY)*zScale);
			}
		}
	}

	private static void refine(Grid grid) {
		float[][] pointZ = new float[grid.getRows()][grid.getCols()];

		for (int r = 0 ; r < grid.getRows() ; r++) {
			for (int c = 0 ; c < grid.getCols() ; c++) {
				TerrainPoint centerP = grid.getPoint(r, c);

				HashMap<TerrainPoint, Float> nearbyWeightPoints = findNearbyPoints(grid, c, r, 1f);
				float avgZ = 0;
				float weightSum = 0;
				for (TerrainPoint p : nearbyWeightPoints.keySet()) {
					//avgZ += p.getZ()*nearbyWeightPoints.get(p);
					//weightSum += nearbyWeightPoints.get(p);
					avgZ += p.getZ();
					weightSum += 1f;
				}
				avgZ /= weightSum;

				pointZ[r][c] = avgZ;
				//centerP.setZ(avgZ);
			}
		}

		for (int r = 0 ; r < grid.getRows() ; r++) {
			for (int c = 0 ; c < grid.getCols() ; c++) {
				grid.getPoint(r, c).setZ(pointZ[r][c]);;
			}
		}
	}

	private static HashMap<TerrainPoint, Float> findNearbyPoints(Grid grid, int centerX, int centerY, float tolerance) {
		int maxDim = (int) tolerance;
		int startX = Math.max(centerX - maxDim, 0);
		int endX   = Math.min(centerX + maxDim, grid.getCols() - 1);
		int startY = Math.max(centerY - maxDim, 0);
		int endY   = Math.min(centerY + maxDim, grid.getRows() - 1);

		TerrainPoint centerP = grid.getPoint(centerY, centerX);
		HashMap<TerrainPoint, Float> points = new HashMap<>();

		for (int x = startX ; x <= endX ; x++) {
			for (int y = startY ; y <= endY ; y++) {
				float distance = centerP.distance(grid.getPoint(y, x));

				if (distance <= tolerance) {
					float weight = (float) Math.pow(1f -  smooth(distance/tolerance), 0.8);
					points.put(grid.getPoint(y, x), weight);
				}
			}
		}

		return points;
	}

	private static float weightMapAverage(HashMap<Float, ArrayList<Float>> map) {
		float average = 0;
		int elements = 0;

		for (float weight : map.keySet()) {
			for (float value : map.get(weight)) {
				average += weight*value;
				++elements;
			}
		}

		return average / elements;
	}

	private static float weightAvg(float x, float y, float w) {
		return x + w*(y - x);
	}

	public static float smooth(float v) {
		return 1 - (float) (Math.cos(v*Math.PI) + 1)/2;
	}
}