package bio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import core.Logger;
import geo.Vertex;
import util.Perlin;
import util.RNG;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>BioMapFactory</b> class creates BioMaps of various sizes.</p>
 */
public class BioMapFactory {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * The BioMap archetypes that can be generated using this BioMapFactory.
     */
    public static enum Type {
        LAND,
        WATER
    };

    /**
     * Creates the given type of BioMap with the specified number of rows and columns.
     *
     * @param type The type of the BioMap.
     * @param rows The number of rows.
     * @param cols The number of columns.
     */
    public static BioMap create(Type type, int rows, int cols) {
        switch (type) {
            case LAND:
                return createLandMap(rows, cols);
            case WATER:
                return createWaterMap(rows, cols);
            default:
                Logger.error("Failed to create BioMap: unknown type \"%s\".", type.toString());
                return null;
        }
    }

    // Private members
    // ------------------------------------------------------------------------

    /**
     * Creates a BioMap that represents the landscape of the World using the given
     * number of rows and columns.
     * 
     * @param rows The number of rows.
     * @param cols The number of columns.
     *
     * @return The landscape BioMap.
     */
    private static BioMap createLandMap(int rows, int cols) {
        Logger.info("Creating a landscape BioMap with %d rows and %d columns.", rows, cols);
        BioMap biomap = new BioMap(rows, cols);
        
        // Generate a moisture map using a Perlin noise distribution.
        int moistRows = 5, moistCols = 5;
        Logger.info(1, "Initializing the moisture map with %d rows and %d columns.", moistRows, moistCols);
        Perlin moistMap = new Perlin(rows, cols, moistRows, moistCols);
        moistMap.transform();

        // Generate an elevation map using a Perlin noise distribution.
        int heightRows = 3, heightCols = 3;
        Logger.info(1, "Initializing the elevation map with %d rows and %d columns.", heightRows, heightCols);
        Perlin heightMap = new Perlin(rows, cols, heightRows, heightCols);
        heightMap.transform();

        // Generate a list of BioRegions using the moisture and elevation maps.
        int regions = 30;
        Logger.info(1, "Partitioning the BioMap into %d BioRegions:", regions);
        List<BioRegion> bioRegions = BioMapFactory.createRegions(moistMap, heightMap, regions, rows, cols);

        Map<Biome, Integer> biomeCounter = new TreeMap<>();

        // Apply the Biome of each BioRegion to their respective BioVertexes.
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                BioRegion region = closestRegion(bioRegions, row, col);
                Biome biome = region.getBiome();
                biomap.set(row, col, biome);

                int counter = biomeCounter.getOrDefault(biome, 0) + 1;
                biomeCounter.put(biome, counter);
            }
        }

        for (Biome biome : biomeCounter.keySet()) {
            String name = biome.getName();
            int counter = biomeCounter.get(biome);
            int total = rows*cols;
            int percent = 100*counter/total;
            Logger.info(2, "%-10s : %5d/%d (%d%%).", name, counter, total, percent);
        }

        return biomap;
    }

    /**
     * Returns the Biome matching the given row and column using the provided
     * moisture and elevation maps.  The returned Biome is computed according to
     * the following table:
     * 
     *       1.00 +----------+----------+----------+----------+----------+----------+
     *   E        |                  Mountain                 |        Alpine       |
     *   l   0.60 |----------+----------+----------+----------+----------+----------|
     *   e        |  Barren  |           Grasslands           |        Taiga        |
     *   v   0.53 |----------+----------+----------+----------+----------+----------|
     *   a        |  Barren  | Prairie  |      Grasslands     |      Deciduous      |
     *   t   0.47 |----------+----------+----------+----------+----------+----------|
     *   i        |  Desert  |  Barren  |            Prairie             | Tropical |
     *   o   0.40 +----------+----------+----------+----------+----------+----------+
     *   n        |                               Void                              |
     *       0.00 +----------+----------+----------+----------+----------+----------+
     *           0.0        0.4        0.47       0.5        0.53       0.6        1.0
     *                                          Moisture
     * 
     * @param moistMap  The moisture map.
     * @param heightMap The elevation map.
     * @param row       The row in the BioMap.
     * @param col       The column in the BioMap.
     * 
     * @return The associated Biome.
     */
    private static Biome getBiome(Perlin moistMap, Perlin heightMap, int row, int col) {
        float moisture = moistMap.get(row, col);
        float elevation = heightMap.get(row, col);

        if (elevation > 0.6f) {
            if (moisture < 0.53f) return Biome.MOUNTAIN;
            else                  return Biome.ALPINE;
        } else if (elevation > 0.53f) {
            if      (moisture < 0.40f) return Biome.BARREN;
            else if (moisture < 0.53f) return Biome.GRASSLANDS;
            else                       return Biome.TAIGA;
        } else if (elevation > 0.47f) {
            if      (moisture < 0.40f) return Biome.BARREN;
            else if (moisture < 0.47f) return Biome.PRAIRIE;
            else if (moisture < 0.53f) return Biome.GRASSLANDS;
            else                       return Biome.DECIDUOUS;
        } else if (elevation > 0.4f) {
            if (moisture < 0.4f)       return Biome.DESERT;
            else if (moisture < 0.47f) return Biome.BARREN;
            else if (moisture < 0.60f) return Biome.PRAIRIE;
            else                       return Biome.TROPICAL;
        } else {
            return Biome.VOID;
        }
    }

    /**
     * Creates a BioMap that represents the water in the World using the given
     * number of rows and columns.
     * 
     * @param rows The number of rows.
     * @param cols The number of columns.
     *
     * @return The water BioMap.
     */
    private static BioMap createWaterMap(int rows, int cols) {
        Logger.info("Creating water BioMap with %d rows and %d columns.", rows, cols);
        BioMap biomap = new BioMap(rows, cols);
        biomap.setRect(0, 0, cols - 1, rows - 1, Biome.WATER);
        return biomap;
    }

    /**
     * Returns the closet BioRegion to the given cell.
     * 
     * @param bioRegions The list of candidate BioRegions.
     * @param row        The row of the cell.
     * @param col        The column of the cell.
     */
    private static BioRegion closestRegion(List<BioRegion> bioRegions, int row, int col) {
        Vertex cell = new Vertex(col, row, 0);
        Function<BioRegion, Float> distance = region -> region.getCenter().distance(cell);
        return bioRegions.stream().min(Comparator.comparing(distance)).get();
    }

    /**
     * Returns a list of |numRegions| BioRegions using the given moisture and
     * height maps.  The centers of the BioRegions are randomly distributed
     * across the specified number rows and columns.
     * 
     * @param moistMap  The moisture map.
     * @param heightMap The elevation map.
     * @param rows      The number of rows.
     * @param cols      The number of columns.
     * 
     * @return The list of BioRegions.
     */
    private static List<BioRegion> createRegions(Perlin moistMap, Perlin heightMap, int numRegions, int rows, int cols) {
        List<BioRegion> regions = new ArrayList<>();
        for (int i = 0; i < numRegions; ++i) {
            // The center of the BioRegion can appear anywhere on the BioMap.
            float x = RNG.random(0, cols);
            float y = RNG.random(0, rows);
            Vertex center = new Vertex(x, y, 0);

            // The Biome associated with the BioRegion is determined by the
            // moisture and elevation map values at the current row and column.
            Biome biome = getBiome(moistMap, heightMap, (int) x, (int) y);

            // Append the BioRegion to the list.
            BioRegion region = new BioRegion(center, biome);
            regions.add(region);
        }
        return regions;
    }
}