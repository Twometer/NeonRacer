package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import neonracer.core.GameContext;
import neonracer.phys.EntityPhysics;
import neonracer.render.gl.core.Texture;
import org.joml.Vector2f;

/**
 * An entity is an object in the world the user
 * can interact with, such as a car they can drive or
 * items they can collect
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityItem.class, name = "item"),
        @JsonSubTypes.Type(value = EntityCar.class, name = "car")}
)
public abstract class Entity {

    private String type;

    private Vector2f position;

    private float rotation;

    private EntityPhysics physics;

    @JsonCreator
    Entity(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        this.type = type;
        this.position = new Vector2f(x, y);
        this.rotation = rotation;
    }

    public String getType() {
        return type;
    }

    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return Returns the rotation angle in radians
     */
    public float getRotation() {
        return rotation;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    /**
     * Set the rotation angle in radians
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public abstract Texture getColorTexture();

    public abstract Texture getGlowTexture();

    public EntityPhysics getPhysics() {
        return physics;
    }

    void setPhysics(EntityPhysics physics) {
        this.physics = physics;
    }

    public void onInitialize(GameContext gameContext) {

    }

    public float getWidth() {
        return getColorTexture().getWidth() / 150.0f;
    }

    public float getHeight() {
        return getColorTexture().getHeight() / 150.0f;
    }

}
