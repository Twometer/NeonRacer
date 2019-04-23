package neonracer.render.engine.collider;

import org.joml.Vector2f;

public interface ICollider<T> {

    void initialize(T obj);

    boolean collides(Vector2f vector2f);

}
