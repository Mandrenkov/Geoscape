package model;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>TerrainMap</b> class.</p>
 */ 
public class TerrainMap {

	private char[][] landMap;
	private char[][] waterMap;
	private final int ROWS, COLS;

	private static final char[] landChars = {'D', 'H', 'M', 'P'};

	public TerrainMap(int rows, int cols) {
		this.ROWS = rows;
		this.COLS = cols;

		landMap = new char[ROWS][COLS];
		waterMap = new char[ROWS][COLS];

		sampleScape();
	}

	public void clear() {
		for (int row = 0 ; row < ROWS ; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				landMap[row][col] = '.';
				waterMap[row][col] = '.';
			}
		}
	}

	public void sampleScape() {
		clear();

		// Water
		for (int row = 0 ; row < ROWS ; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				waterMap[row][col] = 'L';
			}
		}

		// Land
		int d = (int) Math.ceil(Math.sqrt(landChars.length));
		int rScale = ROWS/d;
		int cScale = COLS/d;

		for (int row = 0 ; row < ROWS ; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				int i = d*(row/rScale) + col/cScale;

				if (i < landChars.length) {
					landMap[row][col] = landChars[i];
				}
			}
		}
	}

	public String toString() {
		StringBuffer mapString = new StringBuffer();

		mapString.append("Land Map\n");
		for (int row = 0 ; row < ROWS ; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				mapString.append(landMap[row][col]);
			}
			mapString.append("\n");
		}

		mapString.append("\nWater Map\n");
		for (int row = 0 ; row < ROWS ; row++) {
			for (int col = 0 ; col < COLS ; col++) {
				mapString.append(waterMap[row][col]);
			}
			mapString.append("\n");
		}

		return mapString.toString();
	}



}