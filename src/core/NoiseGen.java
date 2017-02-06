package core;

import java.util.ArrayList;

import model.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>NoiseGen</b> class.</p>
 */ 
public class NoiseGen {

	private static final float EPSILON = (float) 1E-5;

	public static void generateNoise(Grid grid) {
		Vector2D[][] gradients = createGradients(grid);
		applyNoise(grid, gradients);
	}

	private static Vector2D[][] createGradients(Grid grid) {
		Vector2D[][] gradients = new Vector2D[grid.getPerlinCols() + 1][grid.getPerlinRows() + 1];

		for (int x = 0 ; x < grid.getPerlinCols() + 1 ; x++) {
			for (int y = 0 ; y < grid.getPerlinRows() + 1; y++) {
				float angle = (float) (Math.random() * 2 * Math.PI);

				float xComp = (float) Math.cos(angle);
				float yComp = (float) Math.sin(angle);

				gradients[x][y] = new Vector2D(xComp, yComp);
			}
		}
		
		return gradients;
	}

	private static void applyNoise(Grid grid, Vector2D[][] gradients) {		
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

				dotProducts[0][0] = gradients[xCell    ][yCell    ].dot(new Vector2D(xDiff    , yDiff    ));
				dotProducts[0][1] = gradients[xCell    ][yCell + 1].dot(new Vector2D(xDiff    , yDiff - 1));
				dotProducts[1][1] = gradients[xCell + 1][yCell + 1].dot(new Vector2D(xDiff - 1, yDiff - 1));
				dotProducts[1][0] = gradients[xCell + 1][yCell    ].dot(new Vector2D(xDiff - 1, yDiff    ));

				float v1 = weightAvg(dotProducts[0][0], dotProducts[1][0], weightX);
				float v2 = weightAvg(dotProducts[0][1], dotProducts[1][1], weightX);

				ArrayList<TerrainPoint> points = new ArrayList<>();
				int reach = 3;
				
				for (int i = 1 ; i <= reach ; i ++) {
					if (x > i - 1) points.add(grid.getPoint(y, x - i));
					if (x < grid.getCols() - i) points.add(grid.getPoint(y, x + i));
					if (y > i - 1) points.add(grid.getPoint(y - i, x));
					if (y < grid.getRows() - i) points.add(grid.getPoint(y + i, x));
				}
				
				points.add(p);
				
				float zScale = 0;
				for (TerrainPoint point : points) zScale += point.getBiome().getScale();
				zScale /= points.size();
				
				//float zScale = p.getBiome().getScale();
				p.setColour(Colour.averageColour(points.toArray(new TerrainPoint[points.size()])));
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

	private static float weightAvg(float x, float y, float w) {
		return x + w*(y - x);
	}

	private static float smooth(float v) {
		return 1 - (float) (Math.cos(v*Math.PI) + 1)/2;
	}
}