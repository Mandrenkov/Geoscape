package bio;

import geo.Vertex;

/**
 * The BioRegion class represents a region of a BioMap.  Each BioVertex in a
 * BioRegion shares the same Biome.
 */
public class BioRegion {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a BioRegion with the specified parameters.
     *
     * @param center The center of this BioRegion.
     * @param biome  The Biome associated with this BioRegion.
     */
    public BioRegion(Vertex center, Biome biome) {
        this.center = center;
        this.biome = biome;
    }

    /**
     * Returns the Biome associated with this BioRegion.
     * 
     * @return The Biome.
     */
    public Biome getBiome() {
        return this.biome;
    }

    /**
     * Returns the center of this BioRegion.
     * 
     * @return The center.
     */
    public Vertex getCenter() {
        return this.center;
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The center of the BioRegion.
     */
    private Vertex center;

    /**
     * The Biome associated with this BioRegion.
     */
    private Biome biome;
}