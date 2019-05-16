package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import neonracer.core.GameContext;
import neonracer.phys.entity.EntityPhysics;
import neonracer.render.gl.core.Texture;
import org.joml.Vector2f;

import java.util.Map;

/**
 * An entity is an object in the world the user
 * can interact with, such as a car they can drive or
 * items they can collect
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityItem.class, name = "item"),
        @JsonSubTypes.Type(value = EntityStatic.class, name = "static")
})
public abstract class Entity {

    private static final float SCALE_FACTOR = 150.f;

    long entityId;

    private String type;

    private Vector2f position;

    private float rotation;

    private EntityPhysics physics;

    private Map<String, String> params;

    private boolean inFrustum = true;

    @JsonCreator
    Entity(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation, @JsonProperty("params") Map<String, String> params) {
        this.type = type;
        this.position = new Vector2f(x, y);
        this.rotation = rotation;
        this.params = params;
    }

    public long getEntityId() {
        return entityId;
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

    public boolean isInFrustum() {
        return inFrustum;
    }

    public void setInFrustum(boolean inFrustum) {
        this.inFrustum = inFrustum;
    }

    public abstract Texture getColorTexture();

    public abstract Texture getGlowTexture();

    public EntityPhysics getPhysics() {
        return physics;
    }

    public void setPhysics(EntityPhysics physics) {
        this.physics = physics;
    }

    public void onInitialize(GameContext gameContext) {

    }

    public float getWidth() {
        return getColorTexture().getWidth() / SCALE_FACTOR;
    }

    public float getHeight() {
        return getColorTexture().getHeight() / SCALE_FACTOR;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
