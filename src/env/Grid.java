package env;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import geo.Vertex;
import geo.Biotex;
import geo.Biogle;
import util.BiomeMap;
import util.Noise;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Grid</b> class.</p>
 */
public class Grid implements Drawable {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Grid with the given name, number of rows, columns, and coordinate bounds.
	 * 
	 * @param name    The name of this Grid.
	 * @param rows    The number of rows in this Grid.
	 * @param columns The number of columns in this Grid.
	 * @param minX    The minimum X-coordinate of this Grid.
	 * @param minY    The minimum Y-coordinate of this Grid.
	 * @param maxX    The maximum X-coordinate of this Grid. 
	 * @param maxY    The maximum Y-coordinate of this Grid.
	 */
	public Grid(String name, int rows, int columns, float minX, float minY, float maxX, float maxY) {
		this.name = name;
		this.rows = rows;
		this.columns = columns;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;

		this.biotices = new Biotex[this.rows][this.columns];
		this.biogles = new ArrayList<>();

		// Initialize the Biotices in this Grid.
		for (int row = 0 ; row < this.rows; ++row) {
			for (int col = 0 ; col < this.columns ; ++col) {
				float x = this.minX + row*(this.maxX - this.minX)/(this.columns - 1);
				float y = this.minY + col*(this.maxY - this.minY)/(this.rows - 1);
				float z = DEFAULT_Z;
				this.biotices[row][col] = new Biotex(x, y, z);
			}
		}

		// Initialize the Biogles in this Grid.
		for (int row = 0; row < this.rows - 1; ++row) {
			for (int p = 0; p < 2*this.columns - 2; ++p) {
				int col = p/2;
				boolean forward = p % 2 == 0;

				Biotex[] biogle = new Biotex[3];
				biogle[0] = forward ? this.biotices[row][col]     : this.biotices[row + 1][col + 1];
				biogle[1] = forward ? this.biotices[row + 1][col] : this.biotices[row][col + 1];
				biogle[2] = forward ? this.biotices[row][col + 1] : this.biotices[row + 1][col];
				this.biogles.add(new Biogle(biogle));
			}
		}
	}

	/**
	 * Initializes the Points and Triangles that constitute this Grid.
	 */
	public void buildPoints() {
		Noise.generateNoise(this);
	}

	/**
	 * Draws this Grid.
	 */
	public void draw() {
		glPolygonMode(GL_FRONT, GL_FILL);
		glCullFace(GL_BACK);

		for (Biogle biogle : this.biogles) {
			biogle.draw();
		}
	}

	/**
	 * Returns the number of columns in this Grid.
	 * 
	 * @return The number of columns.
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * Returns the number of rows in this Grid.
	 * 
	 * @return The number of rows.
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * Returns the Biotex in this Grid located at the given position.
	 * 
	 * @param row The row of this Grid containing the desired Biotex.
	 * @param col The column of this Grid containing the desired Biotex.
	 * 
	 * @return The Biotex located at the given position.
	 */
	public Biotex getBiotex(int row, int col) {
		return this.biotices[row][col];
	}

	/**
	 * Returns the Biogles comprising this Grid.
	 * 
	 * @return The Biogles.
	 */
	public ArrayList<Biogle> getBiogles() {
		return this.biogles;
	}

	/**
	 * Returns a String representation of this Grid.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("%s [(%.2f, %.2f) to (%.2f, %.2f)]", this.name, this.minX, this.minY, this.maxX, this.maxY);
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The default elevation of the Biotices in a Grid.
	 */
	private static final float DEFAULT_Z = 0.1f;

	/**
	 * The name of this Grid.
	 */
	private String name;

	/**
	 * The number of rows in this Grid.
	 */
	private int rows;

	/**
	 * The number of columns in this Grid.
	 */
	private int columns;

	/**
	 * The minimum X-coordinate of this Grid. 
	 */
	private float minX;

	/**
	 * The minimum Y-coordinate of this Grid. 
	 */
	private float minY;

	/**
	 * The maximum X-coordinate of this Grid. 
	 */
	private float maxX;

	/**
	 * The maximum Y-coordinate of this Grid. 
	 */
	private float maxY;

	/**
	 * The matrix of Biotices that comprise the Biogles of this Grid.
	 */
	private Biotex[][] biotices;

	/**
	 * The Biogles that comprise this Grid.
	 */
	private ArrayList<Biogle> biogles;
}