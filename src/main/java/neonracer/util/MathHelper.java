package neonracer.util;

import org.jbox2d.common.Vec2;

public class MathHelper {

    public static Vec2 nullVector = new Vec2(0,0);

    public static float clamp(float f, float min, float max) {
        if (f < min) return min;
        if (f > max) return max;
        return f;
    }

    public static float modAngle(float a) {
        return (float) ((a+Math.PI)%(Math.PI*2)-Math.PI);
    }

    public static float vec2ToAngle(Vec2 vec) {
        if(vec == null)
            return 0;
        if(vec.y == 0)
            return 0;
        return modAngle((float) Math.atan(vec.x/vec.y));
    }

    public static Vec2 angleToUnitVec2(float a)
    {
        Vec2 vec = new Vec2((float) -Math.sin(a),(float) Math.cos(a));
        return vec;
    }

    public static Vec2 rotateVec2(Vec2 vec, float a)
    {
        Vec2 out = new Vec2((float) ((vec.x * Math.cos(a)) - (vec.y * Math.sin(a))),(float) ((vec.x * Math.sin(a)) + (vec.y * Math.cos(a))));
        return out;
    }

}
