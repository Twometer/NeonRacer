package neonracer.util;

public class MathHelper {

    public static float clamp(float f, float min, float max) {
        if (f < min) return min;
        if (f > max) return max;
        return f;
    }


}
