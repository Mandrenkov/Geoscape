package env;

import util.Algebra;
import bio.Biomix;
import bio.Biotex;
import geo.Vector;

/**
 * @author  Mikhail Andrenkov
 * @since   March 4, 2018
 * @version 1.1
 *
 * <p>The <b>Noiseform</b> class represents a noise transform that is applied
 * to Grid objects.  Specifically, this class uses Perlin noise to distort
 * a given Grid and then texturizes the Biotices of the given Grid to reflect
 * the influence of nearby Biomes.</p>
 */
public class Noiseform {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Noiseform with the given Grid and the specified number of 
	 * Perlin rows and columns.
	 * 
	 * @param grid The Grid to be associated with this Noiseform.
	 * @param rows The number of Perlin rows.
	 * @param cols The number of Perlin columns.
	 */
	public Noiseform(Grid grid, int rows, int cols) {
		this.grid = grid;
		this.rows = rows;
		this.cols = cols;

		this.gradients = new Vector[rows + 1][cols + 1];
		for (int row = 0; row <= rows; ++row) {
			for (int col = 0; col <= cols; ++col) {
				double angle = 2*Math.PI*Math.random();
				float x = (float) Math.cos(angle);
				float y = (float) Math.sin(angle);
				this.gradients[row][col] = new Vector(x, y);
			}
		}
	}

	public void apply() {
		this.disturb();
		this.alias();
		this.texture();
	}

	// Private members
	// -------------------------------------------------------------------------

	/**
	 * Determines the radius of influence of a Point during scaling and Biome calculations.
	 */
	private static final int TOLERANCE = 2;

	/**
	 * The Grid associated with this Noiseform.
	 */
	private Grid grid;

	/**
	 * The number of Perlin rows to be used in this Noiseform.
	 */
	private int rows;

	/**
	 * The number of Perlin columns to be used in this Noiseform.
	 */
	private int cols;

	/**
	 * The map of Perlin gradients used to generate the Perlin noise.
	 */
	private Vector[][] gradients;

	/**
	 * Applies a Perlin noise transformation to the Grid associated with this Noiseform.
	 */
	private void disturb() {
		// Define a set of conversion ratios to convert a Grid coordinate into
		// a Perlin grid coordinate.  An epsilon is thrown in to avoid division
		// by 0 errors.
		float epsilon = 1E-6f;
		float colSize = grid.getWidth()/(this.cols - epsilon);
		float rowSize = grid.getHeight()/(this.rows + epsilon);

		for (int row = 0; row < grid.getRows(); ++row) {
			for (int col = 0; col < grid.getColumns(); ++col) {
				Biotex biotex = grid.getBiotex(col, row);

				/**
				 * Use Perlin noise to calculate the change in elevation of this
				 * Biotex.
				 */
				
				// Map the Grid coordinate of the Biotex to a Perlin grid coordinate.
				int colCell = (int) ((biotex.getX() - grid.getMinX())/colSize);
				int rowCell = (int) ((biotex.getY() - grid.getMinY())/rowSize);

				// Determine the offset of the Biotex to the Perlin coordinate.
				float colOffset = biotex.getX() % colSize;
				float rowOffset = biotex.getY() % rowSize;

				// Compute the dot products from each corner of the Perlin cell
				// to the Biotex.
				float[][] dotProducts = new float[2][2];
				dotProducts[0][0] = gradients[rowCell    ][colCell    ].dot(new Vector(rowOffset    , colOffset    ));
				dotProducts[0][1] = gradients[rowCell    ][colCell + 1].dot(new Vector(rowOffset    , colOffset - 1));
				dotProducts[1][1] = gradients[rowCell + 1][colCell + 1].dot(new Vector(rowOffset - 1, colOffset - 1));
				dotProducts[1][0] = gradients[rowCell + 1][colCell    ].dot(new Vector(rowOffset - 1, colOffset    ));

				// Calculate the elevation influence of the Perlin noise.
				float colWeight = Algebra.curve(colOffset);
				float colDot0 = Algebra.average(dotProducts[0][0], dotProducts[0][1], colWeight);
				float colDot1 = Algebra.average(dotProducts[1][0], dotProducts[1][1], colWeight);

				float rowWeight = Algebra.curve(rowOffset);
				float z = Algebra.average(colDot0, colDot1, rowWeight);

				// Tie the Biotex to the ground if it is near a Grid boundary.
				if (colCell == 0 || colCell == this.cols - 1 || rowCell == 0 || rowCell == this.rows - 1) {
					float rowOffsetMin = colSize*Math.min(biotex.getX() - grid.getMinX(), grid.getMaxX() - biotex.getX());
					float colOffsetMin = rowSize*Math.min(biotex.getY() - grid.getMinY(), grid.getMaxY() - biotex.getY());
					float offsetMin = Math.min(rowOffsetMin, colOffsetMin);
					z *= Algebra.curve(offsetMin);
				}
				
				/**
				 * Use nearby Biotexes to adjust the elevation scaling, Colour,
				 * and Biomix of the current Biotex.
				 */

				Nearmap nearmap = new Nearmap(grid, row, col, TOLERANCE);

				Colour colour = nearmap.getColour();
				biotex.setColour(colour);

				Biomix biomix = nearmap.getBiomix();
				biotex.setBiomix(biomix);
				
				z *= nearmap.getScale();
				biotex.translate(0, 0, z);
			}
		}
	}

	/**
	 * Remove prominent edges from the Grid of this Noiseform by averaging the
	 * elevations of nearby Biotices.
	 */
	private void alias() {
		int radius = 2;
		int rows = this.grid.getRows();
		int cols = this.grid.getColumns();

		float[][] sum    = new float[rows][cols];
		float[][] weight = new float[rows][cols];

		// Compute the weighted sum of the heights of nearby Biotices.
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Biotex refBiotex = this.grid.getBiotex(row, col);

				for (int r = Math.max(0, row - radius); r <= Math.min(rows - 1, row + radius); ++r) {
					for (int c = Math.max(0, col - radius); c <= Math.min(cols - 1, col + radius); ++c) {
						Biotex curBiotex = this.grid.getBiotex(r, c);

						// Verify that the current Biotex is close to the reference Biotex.
						float dist = refBiotex.distance(curBiotex);
						if (dist <= radius) {
							sum[r][c] += refBiotex.getZ()*dist;
							weight[r][c] += dist;
						}
					}	
				}
			}
		}

		// Set the elevation of each Grid Biotex to the average elevation of nearby Biotices.
		for (int row = 0; row < rows; ++row) {
			for (int col = 0; col < cols; ++col) {
				float avg = sum[row][col]/weight[row][col];
				Biotex biotex = this.grid.getBiotex(row, col);
				biotex.setZ(avg);
			}
		}
	}

	/**
	 * Apply the weighted texturing of each Biotex.
	 */
	private void texture() {
		for (int row = 0; row < this.grid.getRows(); ++row) {
			for (int col = 0; col < this.grid.getColumns(); ++col) {
				this.grid.getBiotex(row, col).texturize();
			}
		}
	}
}