package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

    private boolean physState = false;

    @JsonCreator
    public Entity(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        this.type = type;
        this.position = new Vector2f(x, y);
        this.rotation = rotation;
    }

    @JsonCreator
    public Entity(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation, @JsonProperty("phys") boolean physState) {
        this.type = type;
        this.position = new Vector2f(x, y);
        this.rotation = rotation;
        this.physState = physState;
    }

    public String getType() {
        return type;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean getPhysState() { return physState; }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public abstract Texture getColorTexture();

    public abstract Texture getGlowTexture();

}
