package env;

import bio.Biomix;
import bio.BioVertex;
import core.Logger;
import geo.Vector;
import util.Algebra;

/**
 * @author  Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Noiseform</b> class represents a noise transform that is applied
 * to Grid objects.  Specifically, this class uses Perlin noise to distort
 * a given Grid and then texturizes the BioVertexes of the given Grid to reflect
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

	/**
	 * Applies this Noisform transformation.
	 */
    public void apply() {
        this.disturb();
        this.alias();
        this.texture();
    }

    // Private members
    // -------------------------------------------------------------------------

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
        Logger.debug("Applying Perlin noise transformation to %s.", this.grid);

        // Define a set of conversion ratios to convert a Grid coordinate into
        // a Perlin grid coordinate.  An epsilon is thrown in to avoid division
        // by 0 errors.
        float epsilon = 1E-4f;
        float colSize = this.grid.getWidth()/(this.cols - epsilon);
        float rowSize = this.grid.getHeight()/(this.rows - epsilon);
        float minSize = Math.min(colSize, rowSize);

        for (int row = 0; row < this.grid.getRows(); ++row) {
            for (int col = 0; col < this.grid.getColumns(); ++col) {
                BioVertex biotex = this.grid.getVertex(row, col);

                /**
                 * Use Perlin noise to calculate the change in elevation of this
                 * BioVertex.
                 */

                // Calculate the X and Y components of the BioVertex relative to
                // the origin of the Grid.
                float dx = biotex.getX() - this.grid.getMinX();
                float dy = biotex.getY() - this.grid.getMinY();

                // Map the Grid coordinate of the BioVertex to a Perlin grid coordinate.
                int colCell = (int) (dx/colSize);
                int rowCell = (int) (dy/rowSize);

                // Determine the offset of the BioVertex to the Perlin coordinate.
                float colOffset = dx % colSize;
                float rowOffset = dy % rowSize;

                // Compute the dot products from each corner of the Perlin cell
                // to the BioVertex.
                float[][] dotProducts = new float[2][2];
                dotProducts[0][0] = gradients[rowCell    ][colCell    ].dot(new Vector(colOffset          , rowOffset    ));
                dotProducts[0][1] = gradients[rowCell    ][colCell + 1].dot(new Vector(colOffset - colSize, rowOffset    ));
                dotProducts[1][1] = gradients[rowCell + 1][colCell + 1].dot(new Vector(colOffset - colSize, rowOffset - rowSize));
                dotProducts[1][0] = gradients[rowCell + 1][colCell    ].dot(new Vector(colOffset          , rowOffset - rowSize));

                // Calculate the elevation influence of the Perlin noise.
                float colWeight = Algebra.curve(colOffset/colSize);
                float colDot0 = Algebra.average(dotProducts[0][0], dotProducts[0][1], colWeight);
                float colDot1 = Algebra.average(dotProducts[1][0], dotProducts[1][1], colWeight);

                float rowWeight = Algebra.curve(rowOffset/rowSize);
                float dz = Algebra.average(colDot0, colDot1, rowWeight);

                /**
                 * Use nearby BioVertexes to adjust the elevation scaling, Colour,
                 * and Biomix of the current BioVertex.
                 */

                LocalMap locals = new LocalMap(this.grid, row, col, 0.05f);

                //Logger.debug("locals = %s", locals.toString());

                Colour colour = locals.getColour();
                biotex.setColour(colour);

                Biomix biomix = locals.getBiomix();
                biotex.setBiomix(biomix);

                dz *= locals.getScale();
                float z = biotex.getZ() + dz;

                // Tie the BioVertex to the ground if it is near a Grid boundary.
                float colBorder = Math.min(biotex.getX() - this.grid.getMinX(), this.grid.getMaxX() - biotex.getX());
                float rowBorder = Math.min(biotex.getY() - this.grid.getMinY(), this.grid.getMaxY() - biotex.getY());
                float minBorder = Math.min(colBorder, rowBorder);
                if (minBorder < minSize/2) {
                    z *= Algebra.curve(2*minBorder/minSize);
                }
                biotex.setZ(z);
            }
        }
    }

    /**
     * Remove prominent edges from the Grid of this Noiseform by averaging the
     * elevations of nearby BioVertexes.
     */
    private void alias() {
        Logger.debug("Applying aliasing to %s.", this.grid);

        int radius = 2;
        int rows = this.grid.getRows();
        int cols = this.grid.getColumns();

        float[][] sum    = new float[rows][cols];
        float[][] weight = new float[rows][cols];

        // Compute the weighted sum of the heights of nearby BioVertexes.
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                BioVertex reftex = this.grid.getVertex(row, col);

                for (int r = Math.max(0, row - radius); r <= Math.min(rows - 1, row + radius); ++r) {
                    for (int c = Math.max(0, col - radius); c <= Math.min(cols - 1, col + radius); ++c) {
                        BioVertex curtex = this.grid.getVertex(r, c);

                        // Verify that the current BioVertex is close to the reference BioVertex.
                        float dist = reftex.distance(curtex);
                        if (dist <= radius) {
                            sum[r][c] += reftex.getZ()*dist;
                            weight[r][c] += dist;
                        }
                    }
                }
            }
        }

        // Set the elevation of each Grid BioVertex to the average elevation of nearby BioVertexes.
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                float avg = sum[row][col]/weight[row][col];
                BioVertex biotex = this.grid.getVertex(row, col);
                biotex.setZ(avg);
            }
        }
    }

    /**
     * Apply the weighted texturing of each BioVertex.  The Colour of each
     * BioVertex will also be increase proptional to its elevation.
     */
    private void texture() {
        Logger.debug("Applying textures to %s.", this.grid);

        for (int row = 0; row < this.grid.getRows(); ++row) {
            for (int col = 0; col < this.grid.getColumns(); ++col) {
                BioVertex biotex = this.grid.getVertex(row, col);
                biotex.getColour().illuminate(biotex.getZ());
                biotex.texturize();
            }
        }
    }
}