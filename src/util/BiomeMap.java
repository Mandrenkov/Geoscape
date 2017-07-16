package util;

import java.util.HashMap;

import env.Biome;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <b>BiomeMap</b> class stores and manipulates the 2D distribution of Biomes.</p>
 */
public class BiomeMap {
	/**
	 * The default Biome entry in the BiomeMap.
	 */
	private static final char DEFAULT_CHAR = 'H';
	/**
	 * Amplitude multiplier of the wave generator function.
	 */
	private static final float WAVE_FACTOR_AMPLITUDE = 0.10f;
	/**
	 * Period multiplier of the wave generator function.
	 */
	private static final float WAVE_FACTOR_PERIOD = 0.08f;

	/**
	 * Mapping that associates characters with Biomes.
	 */
	private static final HashMap<Character, Biome> biomeMap = new HashMap<>();

	/**
	 * 2D array that represents the Biome distribution.
	 */
	private char[][] biomeGrid;

	/**
	 * Number of rows in the BiomeMap.
	 */
	private final int ROWS;
	/**
	 * Number of columns in the BiomeMap.
	 */
	private final int COLS;

	static {
		biomeMap.put('D', Biome.DESERT);
		biomeMap.put('H', Biome.HILL);
		biomeMap.put('M', Biome.MOUNTAIN);
		biomeMap.put('P', Biome.PLAIN);
		biomeMap.put('T', Biome.TUNDRA);
	}

	/**
	 * Constructs a new BiomeMap with the given row and column dimensions.
	 * 
	 * @param rows The number of rows in the BiomeMap.
	 * @param cols The number of columns in the BiomeMap.
	 */
	public BiomeMap(int rows, int cols) {
		this.ROWS = rows;
		this.COLS = cols;

		biomeGrid = new char[this.ROWS][this.COLS];
		for (int r = 0 ; r < this.ROWS ; r++) {
			for (int c = 0 ; c < this.COLS ; c++) {
				biomeGrid[r][c] = DEFAULT_CHAR;
			}
		}
	}

	/**
	 * Returns the Biome at the given row and column coordinate.
	 * 
	 * @param row The row component of the coordinate.
	 * @param col The column component of the coordinate.
	 * @return The Biome at the given coordinate.
	 */
	public Biome getBiome(int row, int col) {
		return biomeMap.get(biomeGrid[row][col]);
	}

	/**
	 * Returns the number of columns in the BiomeMap.
	 * 
	 * @return The number of columns.
	 */
	public int getCols() {
		return COLS;
	}

	/**
	 * Returns the number of rows in the BiomeMap.
	 * 
	 * @return The number of rows.
	 */
	public int getRows() {
		return ROWS;
	}

	/**
	 * Returns the character representing the biome at the given coordinate.
	 * 
	 * @param row The row component of the coordinate.
	 * @param col The column component of the coordinate.
	 * @return The representative character.
	 */
	public char getSymbol(int row, int col) {
		return biomeGrid[row][col];
	}

	/**
	 * Sets the BiomeMap symbol at the given coordinate to the specified character.
	 * 
	 * @param row The row component of the coordinate.
	 * @param col The column component of the coordinate.
	 * @param symbol The new symbol at the coordinate.
	 */
	public void setSymbol(int row, int col, char symbol) {
		biomeGrid[row][col] = symbol;
	}

	/**
	 * Sets all the symbols in the given rectangle to the specified character.
	 * 
	 * @param startRow The starting row of the rectangle.
	 * @param startCol The starting column of the rectangle.
	 * @param endRow The final row of the rectangle.
	 * @param endCol The final column of the rectangle.
	 * @param symbol The new symbol of the BiomeMap entries in the rectangle.
	 */
	public void setSymbols(int startRow, int startCol, int endRow, int endCol, char symbol) {
		for (int r = startRow ; r < endRow ; r++) {
			for (int c = startCol ; c < endCol ; c++) {
				this.setSymbol(r, c, symbol);
			}
		}
	}

	/**
	 * Sets all the symbols in the given rectangle to the specified character.
	 * Each side of the rectangle is superimposed with a wave function to generate
	 * a wave-like rectangle.
	 * 
	 * @param startRow The starting row of the rectangle.
	 * @param startCol The starting column of the rectangle.
	 * @param endRow The final row of the rectangle.
	 * @param endCol The final column of the rectangle.
	 * @param symbol The new symbol of the BiomeMap entries in the rectangle.
	 */
	public void setSymbolsWave(int startRow, int startCol, int endRow, int endCol, char symbol) {
		setSymbols(startRow, startCol, endRow, endCol, symbol);

		int waveAmplitudeRow = (int) (ROWS*WAVE_FACTOR_AMPLITUDE);
		int waveAmplitudeCol = (int) (COLS*WAVE_FACTOR_AMPLITUDE);
		int wavePeriodRow = (int) (ROWS*WAVE_FACTOR_PERIOD);
		int wavePeriodCol = (int) (COLS*WAVE_FACTOR_PERIOD);

		for (int r = startRow + waveAmplitudeRow; r < endRow - waveAmplitudeCol ; r++) {
			int startColWave = Math.max(0   , startCol - waveAmplitudeCol/2 + (int) (waveAmplitudeCol/2f*Math.cos(r*(Math.PI/wavePeriodRow))));
			int endColWave   = Math.min(COLS, endCol   + waveAmplitudeCol/2 + (int) (waveAmplitudeCol/2f*Math.cos(r*(Math.PI/(wavePeriodRow*7/3)))));

			for (int c = startColWave ; c < endColWave ; c++) {
				this.setSymbol(r, c, symbol);
			}
		}

		for (int c = startCol + waveAmplitudeCol; c < endCol - waveAmplitudeCol; c++) {
			int startRowWave = Math.max(0   , startRow - waveAmplitudeRow/2 + (int) (waveAmplitudeRow/2f*Math.cos(c*(Math.PI/wavePeriodCol))));
			int endRowWave   = Math.min(COLS, endRow   + waveAmplitudeRow/2 + (int) (waveAmplitudeRow/2f*Math.cos(c*(Math.PI/(wavePeriodCol*7/3)))));

			for (int r = startRowWave ; r < endRowWave ; r++) {
				this.setSymbol(r, c, symbol);
			}
		}
	}

	/**
	 * Returns a String representation of the given BiomeMap.
	 */
	public String toString() {
		StringBuilder mapString = new StringBuilder(String.format("BiomeMap (%d x %d):\n", ROWS, COLS));
		for (int r = 0 ; r < ROWS ; r++) {
			for (int c = 0 ; c < COLS ; c++) {
				mapString.append(biomeGrid[r][c]);
			}
			mapString.append('\n');
		}

		return mapString.toString();
	}

}