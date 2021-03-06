package bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import util.Pair;

/**
 * The Biomix class represents a mixture of Biomes.
 */
public class Biomix implements Iterable<Pair<Biome, Float>> {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs an empty Biomix.
     */
    public Biomix() {
        this.map = new HashMap<>();
    }

    /**
     * Adds the given Biome to this Biomix with the specified weight.
     *
     * @param biome The Biome to be added to this Biomemix.
     * @param weight The weight of this Biome.
     */
    public void add(Biome biome, float weight) {
        float prev = this.map.getOrDefault(biome, 0f);
        this.map.put(biome, prev + weight);
    }

    /**
     * Returns the average Biome scale represented by this Biomix.
     *
     * @return The average scale.
     */
    public float getScale() {
        float mult = 1;
        for (Biome biome : this.map.keySet()) {
            float scale = biome.getScale();
            float weight = this.map.get(biome);
            mult *= weight*scale;
        }
        return (float) Math.pow(mult, 1.0/this.map.size());
    }

    @Override
    public Iterator<Pair<Biome, Float>> iterator() {
        ArrayList<Pair<Biome, Float>> biolist = new ArrayList<>();
        for (Biome biome : this.map.keySet()) {
            biolist.add(new Pair<>(biome, this.map.get(biome)));
        }
        return biolist.iterator();
    }

    /**
     * Returns the String representation of this Biomix.
     *
     * @return The String representation.
     */
    public String toString() {
        ArrayList<String> strlist = new ArrayList<>();
        for (Biome biome : this.map.keySet()) {
            String name = biome.getName();
            double weight = this.map.get(biome);
            strlist.add(String.format("%.2f x %s", weight, name));
        }
        return strlist.toString();
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The list of Biomes associated with this Biomix along with their respective
     * weights.
     */
    private Map<Biome, Float> map;
}