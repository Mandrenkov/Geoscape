package model;

import java.util.HashMap;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>BiomeMap</b> class.</p>
 */ 
public class BiomeMap {
	
	public static final Biome HILL = new Biome("Hills", Colour.TERRAIN_HILLS, 0.5f);
	public static final Biome MOUNTAIN = new Biome("Mountains", Colour.TERRAIN_MOUNTAINS, 1.5f);
	public static final Biome TUNDRA = new Biome("Tundra", Colour.TERRAIN_TUNDRA, 0.05f);
	
	
	private static final char DEFAULT_CHAR = 'T';
	private static final HashMap<Character, Biome> biomeMap = new HashMap<>();
	
	
	private char[][] biomeGrid;
	
	private final int ROWS;
	private final int COLS;
	
	static {
		biomeMap.put('H', HILL);
		biomeMap.put('M', MOUNTAIN);
		biomeMap.put('T', TUNDRA);
	}
	
	
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
	
	public Biome getBiome(int row, int col) {
		return biomeMap.get(biomeGrid[row][col]);
	}
	
	public int getCols() {
		return COLS;
	}
	
	public int getRows() {
		return ROWS;
	}
	
	public char getSymbol(int row, int col) {
		return biomeGrid[row][col];
	}
	
	public void setSymbol(int row, int col, char symbol) {
		biomeGrid[row][col] = symbol;
	}
	
	public void setSymbols(int startRow, int startCol, int endRow, int endCol, char symbol) {
		for (int r = startRow ; r < endRow ; r++) {
			for (int c = startCol ; c < endCol ; c++) {
				this.setSymbol(r, c, symbol);
			}
		}
	}
	
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