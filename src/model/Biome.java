package model;

public class Biome {
	
	private final float[] COLOUR;
	private final float Z_SCALE;
	
	private final String NAME;
	
	public Biome(String name, float[] colour, float zScale) {
		this.NAME = name;
		this.COLOUR = colour;
		this.Z_SCALE = zScale;
	}
	
	public float[] getColour() {
		return COLOUR;
	}
	
	public String getName() {
		return this.NAME;
	}
	
	public float getScale() {
		return Z_SCALE;
	}
	
	public String toString() {
		return String.format("Biome \"%s\": %s %.2f", NAME, Colour.colourString(COLOUR), Z_SCALE);
	}
}
