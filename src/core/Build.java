package core;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * The Build class represents the current build.
 */
public class Build {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns the major version number of this Geoscape build.
     * 
     * @return The major version number.
     */
    public static int getMajorVersion() {
        return Build.MAJOR_VERSION;
    }

    /**
     * Returns the minor version number of this Geoscape build.
     * 
     * @return The minor version number.
     */
    public static int getMinorVersion() {
        return Build.MINOR_VERSION;
    }

    /**
     * Returns the timestamp of this Geoscape build.
     * 
     * @return The timestamp.
     */
    public static String getTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy @ hh:mm a");
        return simpleDateFormat.format(Build.date);
    }

    /**
     * Returns a String representation of the version of this Geoscape build.
     * 
     * @return The String representation of this Build.
     */
    public static String getVersionString() {
        String prefix = Top.DEBUG ? "Geoscape DEBUG" : "Geoscape";
        return String.format("%s V%d.%d - %s", prefix, Build.MAJOR_VERSION, Build.MINOR_VERSION, Build.getTimestamp());
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The major version number of this Geoscape build.
     */
    private static final int MAJOR_VERSION = 2;

    /**
     * The minor version number of this Geoscape build.
     */
    private static final int MINOR_VERSION = 0;

    /**
     * The date when this instance of Geoscape was launched.
     */
    private static final Date date = new Date();
}