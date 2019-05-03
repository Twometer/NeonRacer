package neonracer.render.engine.collider;

public abstract class ColliderResult {

    private boolean collided;

    ColliderResult(boolean collided) {
        this.collided = collided;
    }

    public boolean isCollided() {
        return collided;
    }
}
