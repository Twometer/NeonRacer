package neonracer.phys.entity;

import org.joml.Vector2f;

public interface EntityPhysics {

    Vector2f getPosition();

    float getRotation();

    void update();

}
