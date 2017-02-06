package model;

import java.util.ArrayList;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>The <i>World</i> class represents the world encapsulating the landscape.</p>
 */ 
public class World {
	public static final int ROWS = 80;
	public static final int COLS = 80;
	
	public static final int PERLIN_ROWS = 10;
	public static final int PERLIN_COLS = 10;
	
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
	
	private ArrayList<Grid> landscape;
	
	public World() {
		landscape = new ArrayList<>();
		
		Grid land = new Grid(ROWS, COLS, PERLIN_ROWS, PERLIN_COLS, MIN_X, MIN_Y, MAX_X, MAX_Y);
		BiomeMap landMap = land.getBiomeMap();
		
		landMap.setSymbols(0, 0, landMap.getRows(), landMap.getCols(), 'H');
		landMap.setSymbols(landMap.getRows()/4, landMap.getCols()/4, landMap.getRows()*3/4, landMap.getCols()*3/4, 'M');
		landMap.setSymbols(0, 0, landMap.getRows()/2, landMap.getCols()/2, 'M');
		
		landscape.add(land);
		
		for (Grid grid : landscape) {
			grid.buildPoints();
		}
	}
	
	public ArrayList<Grid> getLandscape() {
		return this.landscape;
	}
	

}
