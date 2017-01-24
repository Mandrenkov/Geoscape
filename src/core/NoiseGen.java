package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import model.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>NoiseGen</b> class.</p>
 */ 
public class NoiseGen {

	private final int PERLIN_X, PERLIN_Y;
	private final float Z_SCALING;

	private static final float EPSILON = (float) 1E-5;

	private final Terrain terrain;
	private final Grid grid;
	private Vector2D[][] gradients;

	public NoiseGen(Terrain terrain, int perlinX, int perlinY, float zScaling) {
		this.terrain = terrain;
		this.grid = terrain.getGrid();

		this.PERLIN_X = perlinX;
		this.PERLIN_Y = perlinY;

		this.Z_SCALING = zScaling;
	}

	public void run() {
		createGradients();
		applyNoise();
	}

	private void createGradients() {
		gradients = new Vector2D[PERLIN_X + 1][PERLIN_Y + 1];

		for (int x = 0 ; x < PERLIN_X + 1 ; x++) {
			for (int y = 0 ; y < PERLIN_Y + 1; y++) {
				float angle = (float) (Math.random() * 2 * Math.PI);

				float xComp = (float) Math.cos(angle);
				float yComp = (float) Math.sin(angle);

				gradients[x][y] = new Vector2D(xComp, yComp);
			}
		}
	}

	private void applyNoise() {
		float perlinXSize = PERLIN_X/(grid.MAX_X - grid.MIN_X + EPSILON);
		float perlinYSize = PERLIN_Y/(grid.MAX_Y - grid.MIN_Y + EPSILON);

		HashSet<Float> zThings = new HashSet<>();

		for (int x = 0 ; x < grid.COLS ; x++) {
			for (int y = 0 ; y < grid.ROWS ; y++) {
				Point p = grid.getPoint(y, x);

				float xExact = (p.getX() - grid.MIN_X)*perlinXSize;
				float yExact = (p.getY() - grid.MIN_Y)*perlinYSize;

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

				float zScale = Z_SCALING;
				float zInit  = p.getZ();

				if (xCell == 0 || xCell == PERLIN_X - 1 || yCell == 0 || yCell == PERLIN_Y - 1) {
					float minX = Math.min(p.getX() - grid.MIN_X, grid.MAX_X - p.getX());
					float minY = Math.min(p.getY() - grid.MIN_Y, grid.MAX_Y - p.getY());
					float minScale = Math.min(minX*perlinXSize, minY*perlinYSize);

					//System.out.println((p.getX() - grid.MIN_X) + " " + (grid.MAX_X - p.getX()));

					zScale *= smooth(minScale);
					zInit *= smooth(minScale);
					//zThings.add(Math.min(minX*perlinXSize, minY*perlinYSize));

					if (terrain.is("Lake")) {
						//zInit -= 0.005f;
					}
				}

				p.setZ(zInit + weightAvg(v1, v2, weightY)*zScale);
			}
		}
		/*ArrayList<Float> zThings2 = new ArrayList<>(zThings);
		Collections.sort(zThings2);
		for (float f : zThings2)
			System.out.println(f);*/
	}

	private float weightAvg(float x, float y, float w) {
		return x + w*(y - x);
	}

	private float smooth(float v) {
		return 1 - (float) (Math.cos(v*Math.PI) + 1)/2;
	}



	/* Old Generator

	private void applyNoise() {
		for (int row = 0 ; row < ROWS; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				Point p = grid.getPoint(row, col);

				ArrayList<Float> nearbyZ = new ArrayList<>();

				for (int row2 = -1 ; row2 < 2 ; row2 ++) {
					for (int col2 = -1 ; col2 < 2 ; col2 ++) {
						if ( grid.inBounds(row + row2, col + col2)) {
							nearbyZ.add(grid.getPoint(row + row2, col + col2).z);
						}
					}
				}

				float averageZ = 0;
				for (float z : nearbyZ) averageZ += z;
				averageZ /= nearbyZ.size();

				averageZ = Math.min(1, averageZ);
				averageZ = Math.max(0, averageZ);

				p.z = averageZ;
			}
		}
	}

	private void finalNoise() {
		int exp = 3;

		float minZ = 1f;
		float maxZ = 0f;
		float avgZ = 0f;

		for (int row = 0 ; row < ROWS; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				Point p = grid.getPoint(row, col);

				p.z = (float) Math.pow(p.z, exp);

				minZ = Math.min(minZ, p.z);
				maxZ = Math.max(maxZ, p.z);
				avgZ += p.z;
			}
		}

		avgZ /= (ROWS * COLS);

		float up = 0.5f - avgZ;
		float diff = Math.max(maxZ - avgZ, avgZ - minZ);

		for (int row = 0 ; row < ROWS; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				Point p = grid.getPoint(row, col);

				p.z += up;
				p.z -= 0.5f;
				p.z /= (diff*2);
				p.z += 1f;
				p.z /= 2f;
			}
		}
	}*/

}