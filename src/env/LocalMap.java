package env;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import bio.Biome;
import bio.Biomix;
import bio.Biotex;
import core.Logger;
import util.Algebra;
import util.Pair;

/**
 * @author Mikhail Andrenkov
 * @since March 6, 2018
 * @version 1.1
 *
 * <pThe <b>LocalMap</b> class represents a weighted set of nearby Biotexes.</p>
 */
public class LocalMap {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a LocalMap using the Biotex located at the given row and column
     * of the specified Grid with the provided proximity distance.
     * 
     * @param grid    The Grid containing the Biotex.
     * @param row     The row of the Biotex.
     * @param col     The column of the Biotex.
     * @param maxdist The maximum manhattan distance to a nearby Biotex.
     */
    public LocalMap(Grid grid, int row, int col, float maxdist) {
        this.map = new HashMap<>();
        this.weightSum = 0;

        this.biotex = grid.getBiotex(row, col);

        float mandist = maxdist*Math.min(grid.getRows(), grid.getColumns());
        float eucdist = maxdist*Math.min(grid.getWidth(), grid.getHeight());

        // Determine the bounding rows containing Biotexes to be added to the map.
        int minRow = (int) Math.max(0, row - mandist);
        int maxRow = (int) Math.min(grid.getRows() - 1, row + mandist);

        // Determine the bounding columns containing Biotexes to be added to the map.
        int minCol = (int) Math.max(0, col - mandist);
        int maxCol = (int) Math.min(grid.getColumns() - 1, col + mandist);        

        for (int r = minRow; r <= maxRow; ++r) {
            for (int c = minCol; c <= maxCol; ++c) {
                Biotex curtex = grid.getBiotex(r, c);

                // Verify that the current Biotex is within distance of the
                // reference Biotex.
                float dist = this.biotex.distance(curtex);
                if (dist <= eucdist) {
                    //System.out.println(dist);
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
        for (Biotex biotex : this.map.keySet()) {
            Biome biome = biotex.getBiome();
            float weight = this.map.get(biotex);
            biomix.add(biome, weight/weightSum);
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
        for (Biotex biotex : this.map.keySet()) {
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
		for (Biotex biotex : this.map.keySet()) {
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
		return String.format("%s: %s", this.biotex.toString(), map.toString());
	}


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The reference Biotex associated with this LocalMap.
     */
    private Biotex biotex;

    /**
     * The map that associates Biotexes with their influence on the reference Biotex.
     */
    private Map<Biotex, Float> map;

    /**
     * The sum of the weights in the LocalMap map.
     */
    private float weightSum;
}