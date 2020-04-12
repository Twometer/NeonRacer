package neonracer.phys.entity;

import org.joml.Vector2f;
import org.jbox2d.common.Vec2;

public interface EntityPhysics {

    Vector2f getPosition();

    float getRotation();

    Vec2 getVelocity();

    void update();

}
