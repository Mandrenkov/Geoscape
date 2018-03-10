package env;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import bio.Biome;
import bio.Biomix;
import bio.Biotex;
import util.Algebra;
import util.Pair;

/**
 * @author Mikhail Andrenkov
 * @since March 6, 2018
 * @version 1.1
 *
 * <pThe <b>Nearmap</b> class represents a weighted set of nearby Biotexes.</p>
 */
public class Nearmap {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Nearmap using the Biotex located at the given row and column
     * of the specified Grid with the provided proximity distance.
     * 
     * @param grid    The Grid containing the Biotex.
     * @param row     The row of the Biotex.
     * @param col     The column of the Biotex.
     * @param maxdist The maximum distance to a nearby Biotex.
     */
    public Nearmap(Grid grid, int row, int col, int maxdist) {
        this.map = new HashMap<>();
        this.weightSum = 0;

        this.biotex = grid.getBiotex(row, col);

        // Determine the bounding rows containing Biotexes to be added to the map.
        int minRow = Math.max(0, row - maxdist);
        int maxRow = Math.min(grid.getRows() - 1, row + maxdist);

        // Determine the bounding columns containing Biotexes to be added to the map.
        int minCol = Math.max(0, col - maxdist);
        int maxCol = Math.min(grid.getColumns() - 1, col + maxdist);

        for (int r = minRow; r <= maxRow; ++r) {
            for (int c = minCol; c <= maxCol; ++c) {
                Biotex curtex = grid.getBiotex(r, c);

                // Verify that the current Biotex within distance of the reference Biotex.
                float dist = biotex.distance(curtex);
                if (dist <= maxdist) {
                    float weight = 1f - Algebra.curve(dist/maxdist);
                    this.map.put(curtex, weight);
                    this.weightSum += weight;
                }
            }	
        }
    }

    /**
     * Returns the representative Biomix of this Nearmap.
     * 
     * @return The Biomix.
     */
    public Biomix getBiomix() {
        Biomix biomix = new Biomix();     
        for (Pair<Biome, Float> pair : biomix) {
            biomix.add(pair.getFirst(), pair.getSecond());
        }
        return biomix;
    }

    /**
     * Returns the representative Colour of this Nearmap.
     * 
     * @return The Colour.
     */
    public Colour getColour() {
        Stream<Biotex> biotexes = this.map.keySet().stream(); 
        Stream<Colour> colours = biotexes.map(biotex -> biotex.getColour());
        return Colour.average(colours.toArray(Colour[]::new));
    }

    /**
     * Returns the representative elevation scale of this Nearmap.
     * 
     * @return The elevation scale.
     */
    public float getScale() {
        float sum = 0;
		for (Biotex biotex : this.map.keySet()) {
            float scale = biotex.getBiomix().getScale();
            float weight = this.map.get(biotex);
			sum += weight*scale;
		}
		return sum/weightSum;
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
     * The reference Biotex associated with this Nearmap.
     */
    private Biotex biotex;

    /**
     * The map that associates Biotexes with their influence on the reference Biotex.
     */
    private Map<Biotex, Float> map;

    /**
     * The sum of the weights in the Nearmap map.
     */
    private float weightSum;
}