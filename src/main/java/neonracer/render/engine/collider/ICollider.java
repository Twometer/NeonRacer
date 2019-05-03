package neonracer.render.engine.collider;

import org.joml.Vector2f;

public interface ICollider<T, R extends ColliderResult> {

    void initialize(T obj);

    R collides(Vector2f vector2f);

}
