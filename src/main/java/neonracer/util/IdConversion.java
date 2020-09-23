package neonracer.util;

public class IdConversion {

    public static int toClientId(long entityId) {
        return (int) (entityId >> 32);
    }

}
