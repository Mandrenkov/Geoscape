package model;

import java.util.ArrayList;

import core.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Grid</b> class.</p>
 */ 
public class Grid {

	public final int ROWS, COLS;
	public final float MIN_X, MIN_Y, MAX_X, MAX_Y;

	private Point[][]  points;
	private ArrayList<Triangle> triangles;

	public Grid(float minX, float minY, float maxX, float maxY) {
		this.ROWS = (int) (DataManager.ROWS*(maxY - minY)/DataManager.RANGE_X);
		this.COLS = (int) (DataManager.COLS*(maxX - minX)/DataManager.RANGE_Y);

		this.MIN_X = minX;
		this.MIN_Y = minY;
		this.MAX_X = maxX;
		this.MAX_Y = maxY;

		points = new Point[ROWS][COLS];
		triangles = new ArrayList<>();
	}

	public void addTriangle(Triangle t) {
		triangles.add(t);
	}

	public Point getPoint(int row, int col) {
		return points[row][col];
	}

	public Point[][] getPoints() {
		return points;
	}

	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}

	public boolean inBounds(int row, int col) {
		return 0 <= row && row < ROWS && 0 <= col && col < COLS;
	}

	public void setPoint(Point point, int row, int col) {
		points[row][col] = point;
	}

	public String toString() {
		StringBuilder gridString = new StringBuilder();

		gridString.append(String.format("Grid from (%.2f, %.2f) to (%.2f, %.2f)", MIN_X, MIN_Y, MAX_X, MAX_Y));

		/*for (int row = 0 ; row < ROWS ; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				gridString.append(points[row][col] + " ");
			}
			gridString.append("\n");
		}*/

		return gridString.toString();
	}

	/*public void updateTriangles() {
		for (Triangle t : triangles) {
			t.updateColours();
		}
	}*/
}