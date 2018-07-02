package core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The Logger class logs application messages.
 */
public class Logger {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Logs a debug message to stdout.
     *
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.
     */
    public static void debug(String msg, Object ... args) {
        if (Top.DEBUG) {
            print("Debug", msg, args);
        }
    }

    /**
     * Logs an information message to stdout.
     *
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.
     */
    public static void info(String msg, Object ... args) {
        Logger.info(0, msg, args);
    }

    /**
     * Logs an information message to stdout.
     *
     * @param level The indentation level of the message.
     * @param msg   The message to display.
     * @param args  The arguments to be substituted into the message.
     */
    public static void info(int level, String msg, Object ... args) {
        // Declare the unit of indentation.
        String tab = "    ";

        // Appending to a StringBuilder object is much more efficient than
        // concatenating Strings.
        int size = level*tab.length();
        StringBuilder indent = new StringBuilder(size);
        for (int i = 0; i < level; ++i) {
            indent.append(tab);
        }

        print("Info", indent.toString() + msg, args);
    }

    /**
     * Logs a warning message to stdout.
     *
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.
     */
    public static void warn(String msg, Object ... args) {
        print("Warn", msg, args);
    }

    /**
     * Logs an error message to stdout.
     *
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.
     */
    public static void error(String msg, Object ... args) {
        print("Error", msg, args);
    }

    /**
     * Logs an error message to stdout and then terminates the program.
     *
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.
     */
    public static void fatal(String msg, Object ... args) {
        error(msg, args);
        System.exit(1);
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * Substitutes the given arguments into the provided message to generate a
     * string that is logged to stdout with the given severity tag.
     *
     * @param severity The severity of the message.
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.
     */
    private static void print(String severity, String msg, Object ... args) {
        String now = getTimestamp();
        String prefix = String.format("[%s] %s: ", now, severity);
        System.out.printf(prefix + msg + "\n", args);
    }

    /**
     * Generates a String representation of the current time.
     *
     * @return The current timestamp.
     */
    private static String getTimestamp() {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return formatter.format(now);
    }
}