package core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mikhail Andrenkov
 * @since February 12, 2018
 * @version 1.1.0
 *
 * <p>The <i>Logger</i> class logs application messages.</p>
 */
public class Logger {
    /**
     * Logs an information message to stdout.
     * 
     * @param msg  The message to display.
     * @param args The arguments to be substituted into the message.  
     */
    public static void info(String msg, Object ... args) {
        print("Info", msg, args);
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
        if (Top.DEBUG) {
            System.out.printf(prefix + msg + "\n", args);
        }
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