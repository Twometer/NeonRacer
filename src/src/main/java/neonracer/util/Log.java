package neonracer.util;

public class Log {

    public static void i(String msg) {
        System.out.println("[INFO] " + msg);
    }

    public static void e(String msg) {
        System.err.println("[ERROR] " + msg);
    }

    public static void w(String msg) {
        System.out.println("[WARN] " + msg);
    }

    public static void e(Throwable throwable) {
        e("Exception thrown");
        throwable.printStackTrace();
    }

}
