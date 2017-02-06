package model;

public class TerrainPoint extends Point {
	
	private Biome biome;
	private float[] colour;

	public TerrainPoint(float x, float y, float z) {
		super(x, y, z);
	}
	
	public TerrainPoint(Biome biome, float x, float y, float z) {
		this(x, y, z);
		this.biome = biome;
		this.colour = biome.getColour();
	}
	
	public Biome getBiome() {
		return biome;
	}
	
	public float[] getColour() {
		return colour;
	}
	
	public void setBiome(Biome biome) {
		this.biome = biome;
		this.colour = biome.getColour();
	}
	
	public void setColour(float[] colour) {
		this.colour = colour;
	}
	
	public String toString() {
		return String.format("TerrainPoint: \"%s\", (%.2f, %.2f, %.2f)", biome.getName(), super.getX(), super.getY(), super.getZ());
	}
	
}
