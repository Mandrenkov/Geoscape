package env;

import java.util.ArrayList;
import util.Pair;

/**
 * @author Mikhail Andrenkov
 * @since February 25, 2018
 * @version 1.1
 *
 * <p>The <b>Biomix</b> class represents a mixture of Biomes.</p>
 */
public class Biomix {

    // Public members
	// -------------------------------------------------------------------------

    /**
	 * Constructs an empty Biomix.
	 */
    public Biomix() {}

    /**
     * Adds the given Biome to this Biomix with the specified weight.
     * 
     * @param biome The Biome to be added to this Biomemix.
     * @param weight The weight of this Biome.
     */
    public void add(Biome biome, double weight) {
        biomelist.add(new Pair<>(biome, weight));
    }

    /**
     * Returns the average Biome scale represented by this Biomix.
     * 
     * @return The average scale.
     */
    public double avgScale() {
        double mult = 1;
        for (Pair<Biome, Double> pair : biomelist) {
            mult *= pair.getSecond()*pair.getFirst().getScale();
        }
        return Math.pow(mult, 1.0/biomelist.size());
    }

    /**
     * Returns the String representation of this Biomix.
     * 
     * @return The String representation.
     */
    public String toString() {
        ArrayList<String> strlist = new ArrayList<>();
        for (Pair<Biome, Double> pair : biomelist) {
            strlist.add(String.format("%.2f x %s", pair.getSecond(), pair.getFirst().getName()));
        }
        return strlist.toString();
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The list of Biomes associated with this Biomix along with their respective weights.
     */
    private ArrayList<Pair<Biome, Double>> biomelist;
}