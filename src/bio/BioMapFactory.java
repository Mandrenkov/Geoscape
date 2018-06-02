package bio;

import core.Logger;

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
     * The type of BioMaps that can be created by the BioMapFactory.
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
                Logger.error("Failed to create BioMap: Unknown type \"%s\".", type.toString());
                return null;
        }
    }

    // Private members
    // -------------------------------------------------------------------------

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
        BioMap biomap = new BioMap(rows, cols);
        biomap.setRect(0,         rows/3,   cols - 1, rows - 1,       Biome.HILL);
        biomap.setCloud(0,        rows*2/3, cols/3,   rows - 1, 4, 4, Biome.PLAINS);
        biomap.setCloud(cols*2/3, rows*2/3, cols - 1, rows - 1, 4, 4, Biome.DESERT);
        biomap.setCloud(0,        0,        cols/4,   rows/3,   4, 4, Biome.TUNDRA);
        biomap.setCloud(cols*2/3, rows/4,   cols - 1, rows*2/3, 4, 4, Biome.MOUNTAIN);
        return biomap;
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
        BioMap biomap = new BioMap(rows, cols);
        biomap.setRect(0, 0, cols - 1, rows - 1, Biome.WATER);
        return biomap;
    }
}