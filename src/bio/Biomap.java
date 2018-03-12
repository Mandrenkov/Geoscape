package bio;

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
	 * @param top    The start row of the rectangle.
	 * @param left   The start column of the rectangle.
	 * @param bottom The final row of the rectangle.
	 * @param right  The final column of the rectangle.
	 * @param biome  The Biome to be associated with each coordinate in the rectangle.
	 */
	public void setRect(int top, int left, int bottom, int right, Biome biome) {
		for (int row = top; row <= bottom; ++row) {
			for (int col = left; col <= right; ++col) {
				map[row][col] = biome;
			}
		}
	}

	/**
	 * Sets all the Biomes in the specified rectangle to the given Biome.
	 * The boundaries of the rectangle will be softened to simulate a natural
	 * Biome border.
	 * 
	 * @param top  The start row of the rectangle.
	 * @param left  The start column of the rectangle.
	 * @param bottom  The final row of the rectangle.
	 * @param right  The final column of the rectangle.
	 * @param biome The Biome to be associated with each coordinate in the rectangle.
	 */
	public void setSoft(int top, int left, int bottom, int right, Biome biome) {
		this.setRect(top, left, bottom, right, biome);

		
		float amplitude = 0.1f;
		float period = 0.1f;

		int waveAmplitudeRow = (int) (this.rows*amplitude);
		int waveAmplitudeCol = (int) (this.cols*amplitude);
		int wavePeriodRow = (int) (this.rows*period);
		int wavePeriodCol = (int) (this.cols*period);

		for (int row = top + waveAmplitudeRow; row < bottom - waveAmplitudeCol ; row++) {
			int startColWave = Math.max(0   , left - waveAmplitudeCol/2 + (int) (waveAmplitudeCol/2f*Math.cos(row*(Math.PI/wavePeriodRow))));
			int endColWave   = Math.min(this.cols, right   + waveAmplitudeCol/2 + (int) (waveAmplitudeCol/2f*Math.cos(row*(Math.PI/(wavePeriodRow*7/3)))));

			for (int col = startColWave ; col < endColWave ; col++) {
				map[row][col] = biome;
			}
		}

		for (int col = left + waveAmplitudeCol; col < right - waveAmplitudeCol; col++) {
			int startRowWave = Math.max(0   , top - waveAmplitudeRow/2 + (int) (waveAmplitudeRow/2f*Math.cos(col*(Math.PI/wavePeriodCol))));
			int endRowWave   = Math.min(this.cols, bottom   + waveAmplitudeRow/2 + (int) (waveAmplitudeRow/2f*Math.cos(col*(Math.PI/(wavePeriodCol*7/3)))));

			for (int row = startRowWave ; row < endRowWave ; row++) {
				map[row][col] = biome;
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

		String header = String.format("(%d x %d):", this.rows, this.cols);
		str.append(header + "\n");

		for (int row = 0; row < this.rows ; ++row) {
			for (int col = 0; col < this.cols; ++col) {
				char c = map[row][col].getName().charAt(0);
				str.append(c);
			}
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
	 * Amplitude multiplier of the wave generator function.
	 */
	private static final float WAVE_FACTOR_AMPLITUDE = 0.10f;

	/**
	 * Period multiplier of the wave generator function.
	 */
	private static final float WAVE_FACTOR_PERIOD = 0.08f;

	/**
	 * The Biome map that associates coordinates with Biomes.
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