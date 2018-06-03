package util;

import geo.Vector;

/**
 * <p>The <b>Perlin</b> class represents a matrix with a Perlin noise transformation.</p>
 */
public class Perlin {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Perlin with given matrix and Perlin noise gradient dimensions.
     *
     * @param matrixRows   The number of rows in the matrix of this Perlin.
     * @param matrixCols   The number of columns in the matrix of this Perlin.
     * @param gradientRows The number of rows in the Perlin noise gradient grid.
     * @param gradientCols The number of columns in the Perlin noise gradient grid.
     */
    public Perlin(int matrixRows, int matrixCols, int gradientRows, int gradientCols) {
        // Instantiate the matrix.
        this.matrixRows = matrixRows;
        this.matrixCols = matrixCols;
        this.matrix = new float[matrixRows][matrixCols];

        // Initialize the Perlin noise gradient grid.
        this.gradientRows = gradientRows;
        this.gradientCols = gradientCols;
        this.gradients = new Vector[gradientRows][gradientCols];

        for (int row = 0; row < gradientRows; ++row) {
            for (int col = 0; col < gradientCols; ++col) {
                double angle = 2*Math.PI*Math.random();
                float x = (float) Math.cos(angle);
                float y = (float) Math.sin(angle);
                this.gradients[row][col] = new Vector(x, y);
            }
        }
    }

	/**
	 * Applies a Perlin noise transformation to this Perlin object.
	 */
    public void transform() {
        // Determine the width and height of each matrix cell with respect to the
        // gradient grid.  The epsilon helps avoid division by 0 errors.
        float epsilon = 1E-4f;
        float width =  this.matrixCols/(this.gradientCols - 1 - epsilon);
        float height = this.matrixRows/(this.gradientRows - 1 - epsilon);

        for (int matrixRow = 0; matrixRow < this.matrixRows; ++matrixRow) {
            for (int matrixCol = 0; matrixCol < this.matrixCols; ++matrixCol) {
                // Map the matrix cell to a gradient cell.
                int noiseRow = (int) (matrixRow/height);
                int noiseCol = (int) (matrixCol/width);

                // Determine the normalized offset of the matrix coordinate
                // relative to the gradient cell.
                float dx = (matrixCol % width)/width;
                float dy = (matrixRow % height)/width;

                // Generate a set of Vectors that start a gradient cell corner
                // and terminate at the matrix cell.
                //
                // [0][0] +----------+ [0][1]
                //        |          |
                //        |      X   |
                //        |          |
                // [1][0] +----------+ [1][1]
                Vector[][] from = new Vector[2][2];
                from[0][0] = new Vector(dx,      dy);
                from[0][1] = new Vector(1f - dx, dy);
                from[1][1] = new Vector(1f - dx, 1f - dy);
                from[1][0] = new Vector(dx,      1f - dy);

                // Compute the dot products between the gradient vectors of each
                // Perlin noise gradient cell corner and the vectors to the
                // matrix cell.
                float[][] dots = new float[2][2];
                dots[0][0] = this.gradients[noiseRow    ][noiseCol    ].dot(from[0][0]);
                dots[0][1] = this.gradients[noiseRow    ][noiseCol + 1].dot(from[0][1]);
                dots[1][1] = this.gradients[noiseRow + 1][noiseCol + 1].dot(from[1][1]);
                dots[1][0] = this.gradients[noiseRow + 1][noiseCol    ].dot(from[1][0]);

                // Average the dot products to arrive at the Perlin noise value.
                float colAvg0 = Algebra.average(dots[0][0], dots[0][1], dx);
                float colAvg1 = Algebra.average(dots[1][0], dots[1][1], dx);
                float rowAvg = Algebra.average(colAvg0, colAvg1, dy);

                // Normalize the value such that it falls in the range [0, 1].
                float value = (rowAvg + 1)/2.0f;
                this.matrix[matrixRow][matrixCol] = value;
            }
        }
    }

    /**
     * Returns the transform Perlin noise value at the given coordinate.
     * 
     * @param row The row of the coordinate to fetch.
     * @param col The column of the coordinate to fetch.
     * 
     * @return The Perlin noise value.
     */
    public float get(int row, int col) {
        return this.matrix[row][col];
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The number of rows in the matrix.
     */
    private int matrixRows;

    /**
     * The number of columns in the matrix.
     */
    private int matrixCols;

    /**
     * The matrix holding the values of the Perlin noise transformation.
     */
    private float[][] matrix;

    /**
     * The number of rows in the Perlin noise gradient grid.
     */
    private int gradientRows;

    /**
     * The number of columns in the Perlin noise gradient grid.
     */
    private int gradientCols;

    /**
     * The Perlin noise gradients.
     */
    private Vector[][] gradients;
}