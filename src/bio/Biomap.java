package bio;

import core.Logger;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Mikhail Andrenkov
 * @since March 4, 2018
 * @version 1.1
 *
 * <p>The <b>Biomap</b> class stores and manipulates the 2D distribution of Biomes.</p>
 */
public class Biomap {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Biomap with the given number of rows and columns.
	 * 
	 * @param rows The number of rows.
	 * @param cols The number of columns.
	 */
	public Biomap(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		// Initialize the map with the default Biome.
		this.map = new Biome[this.rows][this.cols];
		for (int row = 0; row < this.rows; ++row) {
			for (int col = 0; col < this.cols; ++col) {
				map[row][col] = DEFAULT_BIOME;
			}
		}
	}

	/**
	 * Returns the Biome located at the given row and column coordinate.
	 * 
	 * @param row The row component of the coordinate.
	 * @param col The column component of the coordinate.
	 * 
	 * @return The Biome.
	 */
	public Biome getBiome(int row, int col) {
		return map[row][col];
	}

	/**
	 * Returns the number of columns in this Biomap.
	 * 
	 * @return The number of columns.
	 */
	public int getCols() {
		return this.cols;
	}

	/**
	 * Returns the number of rows in the Biomap.
	 * 
	 * @return The number of rows.
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * Sets the entry at the given coordinate to the specified Biome.
	 * 
	 * @param row   The row component of the coordinate.
	 * @param col   The column component of the coordinate.
	 * @param biome The Biome to be associated with the coordinate.
	 */
	public void set(int row, int col, Biome biome) {
		map[row][col] = biome;
	}

	/**
	 * Sets all the Biomes in the given rectangle to the specified Biome.
	 * 
	 * @param left    The start column of the rectangle.
	 * @param bottom  The final row of the rectangle.
	 * @param right   The final column of the rectangle.
	 * @param top     The start row of the rectangle.
	 * @param biome  The Biome to be associated with each coordinate in the rectangle.
	 */
	public void setRect(int left, int bottom, int right, int top, Biome biome) {
		for (int row = bottom; row <= top; ++row) {
			for (int col = left; col <= right; ++col) {
				this.map[row][col] = biome;
			}
		}
	}

	/**
	 * Sets all the Biomes in the specified soft rectangle to the given Biome.
	 * Softened rectangles are ideal for simulating a natural Biome border.
	 * 
	 * @param left    The start column of the rectangle.
	 * @param bottom  The final row of the rectangle.
	 * @param right   The final column of the rectangle.
	 * @param top     The start row of the rectangle.
	 * @param vWaves  The number of waves along a vertical side of the rectangle.
	 * @param hWaves  The number of waves along a horizontal side of the rectangle.
	 * @param biome   The Biome to be associated with each coordinate in the rectangle.
	 */
	public void setCloud(int left, int bottom, int right, int top, int vWaves, int hWaves, Biome biome) {
		this.setRect(left, bottom, right, top, biome);
		
		int height = top - bottom;
		int width = right - left;

		// The amplitude of the waves is proportional to the size of the cloud.
		int amplitude = Math.min(width, height)/8;
		int peaks = 2*amplitude;

		// Compute the periods of the vertical and horizontal waves.
		float vPeriod = (float) height/vWaves;
		float hPeriod = (float) width/hWaves;

		// Soften the vertical walls of the cloud.
		for (int row = bottom; row <= top; ++row) {
			// Determine the height of the wave for the current row.
			double angle = 2*Math.PI*(row - bottom)/vPeriod;
			int away = (int) ((Math.cos(angle) + 1)*amplitude);

			// Set all the cells between |away| and the nearest rectangle wall
			// to the given Biome.
			for (int col = 1; col <= away; ++col) {
				int col1 = Math.max(0,             left - col);
				int col2 = Math.min(this.cols - 1, right + col);

				this.map[row][col1] = biome;
				this.map[row][col2] = biome;
			}
		}

		// Soften the horizontal walls of the cloud.
		for (int col = left; col <= right; ++col) {
			// Determine the height of the wave for the current column.
			double angle = 2*Math.PI*(col - left)/hPeriod;
			int away = (int) ((Math.cos(angle) + 1)*amplitude);

			// Set all the cells between |away| and the nearest rectangle wall
			// to the given Biome.
			for (int row = 1; row <= away; ++row) {
				int row1 = Math.max(0,             bottom - row);
				int row2 = Math.min(this.rows - 1, top + row);

				this.map[row1][col] = biome;
				this.map[row2][col] = biome;
			}
		}

		// Soften the corners of the cloud.
		int[][] corners = {{bottom, left},
						   {bottom, right},
						   {top,    left}, 
						   {top,    right}};
		for (int[] corner : corners) {
			int row = corner[0];
			int col = corner[1];

			// Compute the bounds of the corner rows.
			int row1 = Math.max(0,             row - peaks);
			int row2 = Math.min(this.rows - 1, row + peaks);

			// Compute the bounds of the columns columns.
			int col1 = Math.max(0,             col - peaks);
			int col2 = Math.min(this.cols - 1, col + peaks);

			// Set all the cells inside the bounding box that are within |peaks|
			// of the corner to the given Biome.
			for (int r = row1; r <= row2; ++r) {
				for (int c = col1; c <= col2; ++c) {
					int dr = r - row;
					int dc = c - col;
					double dist = Math.sqrt(dr*dr + dc*dc);
					if (dist <= peaks) {
						this.map[r][c] = biome;
					}
				}
			}
		}
	}

	/**
	 * Returns a String representation of this Biomap.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();

		String header = String.format("Biomap (%d x %d):", this.rows, this.cols);
		str.append(header + "\n");

		for (Biome[] row : this.map) {
			Character[] chars = Arrays.stream(row)
									  .map(biome -> biome.getName().charAt(0))
									  .toArray(Character[]::new);
			str.append(chars);
			str.append('\n');
		}
		return str.toString();
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The default Biome populated in the map.
	 */
	private static final Biome DEFAULT_BIOME = Biome.HILL;

	/**
	 * The 2D-array representing the Biome distribution of a Grid.
	 */
	private Biome[][] map;

	/**
	 * The number of rows in this Biomap.
	 */
	private int rows;

	/**
	 * The number of columns in this Biomap.
	 */
	private int cols;
}