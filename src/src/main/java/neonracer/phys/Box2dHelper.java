package neonracer.phys;

import org.jbox2d.common.Vec2;
import org.joml.Vector2f;

public class Box2dHelper {

    public static Vec2 toVec2(Vector2f vec) {
        return new Vec2(vec.x, vec.y);
    }

    public static Vector2f toVector2f(Vec2 vec2) {
        return new Vector2f(vec2.x, vec2.y);
    }

}
