package model;

import java.util.ArrayList;

import core.NoiseGen;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>The <i>World</i> class represents the world encapsulating the landscape.</p>
 */ 
public class World {
	public static final int ROWS = 40;
	public static final int COLS = 40;
	
	public static final float PADDING_X = 0.2f;
	public static final float PADDING_Y = 0.2f;
	public static final float MIN_X = -1 + PADDING_X;
	public static final float MIN_Y = -1 + PADDING_Y;
	public static final float MAX_X =  1 - PADDING_X;
	public static final float MAX_Y =  1 - PADDING_Y;
	public static final float RANGE_X = MAX_X - MIN_X;
	public static final float RANGE_Y = MAX_Y - MIN_Y;
	public static final float BOUNDARY_PADDING = 0.01f;


	public static final float BASE_MIN_Z = -0.30f;
	public static final float BASE_MAX_Z = 0.00f;

	
	private ArrayList<Terrain> landscape;


	public World() {
		landscape = new ArrayList<>();
		
		float boundaryX1 = -0.3f;
		float boundaryX2 =  0.3f;
		float boundaryY1 =  0.0f;

		landscape.add(new Hill     (new Grid(MIN_X     , MIN_Y, boundaryX1, boundaryY1)));
		landscape.add(new Tundra   (new Grid(boundaryX1, MIN_Y, boundaryX2, boundaryY1)));
		landscape.add(new Wetland  (new Grid(boundaryX2, MIN_Y, MAX_X     , boundaryY1)));

		landscape.add(new Mountain (new Grid(MIN_X     , boundaryY1, boundaryX1, MAX_Y)));
		landscape.add(new Desert   (new Grid(boundaryX1, boundaryY1, boundaryX2, MAX_Y)));
		landscape.add(new Plateau  (new Grid(boundaryX2, boundaryY1, MAX_X     , MAX_Y)));
		
		//landscape.add(new Lake    (new Grid(MIN_X, MIN_Y, MAX_X, MAX_Y)));
		//landscape.add(new Lake    (new Grid(MIN_X + BOUNDARY_PADDING, MIN_Y + BOUNDARY_PADDING, MAX_X - BOUNDARY_PADDING, MAX_Y - BOUNDARY_PADDING)));
		
		for (Terrain terrain : landscape) {
			initPoints(terrain);
			initTriangles(terrain);
			
			new NoiseGen(
					terrain,
					terrain.getPerlinRows(),
					terrain.getPerlinCols(),
					terrain.getElevationScale()
			).run();

			terrain.updateColours();
		}
	}
	
	public ArrayList<Terrain> getLandscape() {
		return landscape;
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
