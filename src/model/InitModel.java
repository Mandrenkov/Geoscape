package model;

import core.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>InitModel</b> class.</p>
 */ 
public class InitModel {

	private static DataManager data;

	public void run() {
		data = Top.data;

		for (Terrain terrain : data.landscape) {
			initPoints(terrain);
			initTriangles(terrain);
		}
	}

	private void initPoints(Terrain terrain) {
		Grid grid = terrain.getGrid();

		for (int row = 0 ; row < grid.ROWS ; row++) {
			for (int col = 0 ; col < grid.COLS ; col++) {
				float x = grid.MIN_X + col*(grid.MAX_X - grid.MIN_X)/(grid.COLS - 1);
				float y = grid.MIN_Y + row*(grid.MAX_Y - grid.MIN_Y)/(grid.ROWS - 1);
				float z = terrain.getElevationBase();

				grid.setPoint(new Point(x, y, z), row, col);
			}
		}
	}

	private void initTriangles(Terrain terrain) {
		Grid grid = terrain.getGrid();

		Point[] points = new Point[grid.COLS*2];

		for (int row = 0 ; row < grid.ROWS - 1 ; row ++) {
			int pointIndex = 0;

			for (int col = 0 ; col < grid.COLS ; col++) {
				points[pointIndex++] = grid.getPoint(row    , col);
				points[pointIndex++] = grid.getPoint(row + 1, col);
			}

			boolean flip = false;

			for (int p = 0 ; p < pointIndex - 2 ; p ++) {
				float[] colour = terrain.getBaseColour();

				float[] colours = new float[] {colour[0], colour[1], colour[2]};

				if (flip = !flip) grid.addTriangle(new Triangle(colours, points[p    ], points[p + 1], points[p + 2]));
				else              grid.addTriangle(new Triangle(colours, points[p + 2], points[p + 1], points[p    ]));
			}
		}
	}

}