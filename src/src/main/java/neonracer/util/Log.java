package neonracer.util;

/**
 * Simple logging helper
 */
public class Log {

    /**
     * Logs an info to the console with the specified message
     *
     * @param msg The message to log
     */
    public static void i(String msg) {
        System.out.println("[INFO] " + msg);
    }

    /**
     * Logs a warning to the console with the specified message
     *
     * @param msg The message to log
     */
    public static void w(String msg) {
        System.out.println("[WARN] " + msg);
    }

    /**
     * Logs an error to the console with the specified message
     *
     * @param msg The message to log
     */
    public static void e(String msg) {
        System.err.println("[ERROR] " + msg);
    }

    /**
     * Logs an exception to the console
     *
     * @param throwable The exception to log
     */
    public static void e(Throwable throwable) {
        e("Exception thrown");
        throwable.printStackTrace();
    }

}
