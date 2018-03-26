package env;

import java.util.HashMap;
import java.util.Map;

import bio.Biome;
import bio.Biomix;
import bio.BioVertex;
import util.Algebra;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <pThe <b>LocalMap</b> class represents a weighted set of nearby BioVertexes.</p>
 */
public class LocalMap {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a LocalMap using the BioVertex located at the given row and column
     * of the specified Grid with the provided proximity distance.
     *
     * @param grid    The Grid containing the BioVertex.
     * @param row     The row of the BioVertex.
     * @param col     The column of the BioVertex.
     * @param maxdist The maximum manhattan distance to a nearby BioVertex.
     */
    public LocalMap(Grid grid, int row, int col, float maxdist) {
        this.map = new HashMap<>();
        this.weightSum = 0;

        this.biotex = grid.getVertex(row, col);

        // Convert the maximum distance parameter into its Manhattan and Euclidean
        // equivalents.
        int mandist = (int) (maxdist*Math.min(grid.getRows(), grid.getColumns()));
        float eucdist = maxdist*Math.min(grid.getWidth(), grid.getHeight());

        // Determine the bounding rows containing BioVertexes to be added to the map.
        int minRow = Math.max(0, row - mandist);
        int maxRow = Math.min(grid.getRows() - 1, row + mandist);

        for (int r = minRow; r <= maxRow; ++r) {
            // Calculate the Euclidean distance between the given row and the
            // current row.
            float height = Math.abs(r - row)*grid.getCellHeight();
            // Calculate the Euclidian distance between the given column and the
            // the column corresponding to the current row.
            float width = (float) Math.sqrt(Math.pow(eucdist, 2) - Math.pow(height, 2));

            // Determine the bounding column containing BioVertexes to be added to the map.
            int coldist = (int) Math.ceil(width/grid.getCellWidth());
            int minCol = (int) Math.max(0, col - coldist);
            int maxCol = (int) Math.min(grid.getColumns() - 1, col + coldist);
            for (int c = minCol; c <= maxCol; ++c) {
                BioVertex curtex = grid.getVertex(r, c);

                // Verify that the current BioVertex is within distance of the
                // reference BioVertex.
                float dist = this.biotex.distance(curtex);
                if (dist <= eucdist) {
                    float weight = (float) Math.pow(1f - Algebra.curve(dist/(eucdist + 1E-4f)), 0.8);
                    this.map.put(curtex, weight);
                    this.weightSum += weight;
                }
            }
        }
    }

    /**
     * Returns the representative Biomix of this LocalMap.
     *
     * @return The Biomix.
     */
    public Biomix getBiomix() {
        Biomix biomix = new Biomix();
        for (BioVertex biotex : this.map.keySet()) {
            Biome biome = biotex.getBiome();
            float weight = this.map.get(biotex);
            biomix.add(biome, weight/this.weightSum);
        }
        return biomix;
    }

    /**
     * Returns the representative Colour of this LocalMap.
     *
     * @return The Colour.
     */
    public Colour getColour() {
        Colour average = new Colour(0, 0, 0);
        for (BioVertex biotex : this.map.keySet()) {
            // The Biome Colour must be cloned to localize the scaling done in
            // this loop.
            Colour colour = new Colour(biotex.getBiome().getColour());
            float weight = this.map.get(biotex);
            float scalar = weight/this.weightSum;
            colour.scale(scalar);
            average.add(colour);
        }
        return average;
    }

    /**
     * Returns the representative elevation scale of this LocalMap.
     *
     * @return The elevation scale.
     */
    public float getScale() {
        float average = 0;
        for (BioVertex biotex : this.map.keySet()) {
            float scale = biotex.getBiome().getScale();
            float weight = this.map.get(biotex);
            average += weight*scale/this.map.size();
        }
        return average;
    }

    /**
     * Returns a String representation of this Shape.
     *
     * @return A String representing this Shape.
     */
    public String toString() {
        return String.format("%s: %s", this.biotex.toString(), this.map.toString());
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The reference BioVertex associated with this LocalMap.
     */
    private BioVertex biotex;

    /**
     * The map that associates BioVertexes with their influence on the reference BioVertex.
     */
    private Map<BioVertex, Float> map;

    /**
     * The sum of the weights in the LocalMap map.
     */
    private float weightSum;
}