package neonracer.model.entities;

import neonracer.render.gl.Texture;
import org.joml.Vector2f;

/**
 * An entity is an object in the world the user
 * can interact with, such as a car they can drive or
 * items they can collect
 */
public abstract class Entity {

    private Vector2f position;

    private float rotation;

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public abstract Texture getColorTexture();

    public abstract Texture getGlowTexture();

}
