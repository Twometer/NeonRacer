package neonracer.model.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.render.gl.core.Texture;
import neonracer.resource.IAsset;

/**
 * Defines car properties. There are different car
 * models the user can choose from, and each one has
 * different properties.
 */
public class Car implements IAsset {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("color_texture")
    private String colorTexturePath;

    private Texture colorTexture;

    @JsonProperty("glow_texture")
    private String glowTexturePath;

    private Texture glowTexture;

    @JsonProperty("drag_coefficient")
    private float dragCoefficient;

    @JsonProperty("roll_coefficient")
    private float rollCoefficient;

    @JsonProperty("linear_traction")
    private float linearTraction;

    @JsonProperty("lateral_traction")
    private float lateralTraction;

    @JsonProperty("forward_force")
    private float forwardForce;

    @JsonProperty("reverse_force")
    private float reverseForce;

    @JsonProperty("steering_lock_angle")
    private float steeringLockAngle;

    @JsonProperty("steering_angular_velocity")
    private float steeringAngularVelocity;

    @Override
    public void initialize(GameContext context) {
        colorTexture = context.getTextureProvider().getTexture(colorTexturePath);
        glowTexture = context.getTextureProvider().getTexture(glowTexturePath);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Texture getColorTexture() {
        return colorTexture;
    }

    public Texture getGlowTexture() {
        return glowTexture;
    }

    public float getDragCoefficient() {
        return dragCoefficient;
    }

    public float getRollCoefficient() {
        return rollCoefficient;
    }

    public float getLinearTraction() {
        return linearTraction;
    }

    public float getLateralTraction() {
        return lateralTraction;
    }

    public float getForwardForce() {
        return forwardForce;
    }

    public float getReverseForce() {
        return reverseForce;
    }

    /**
     * Gets the steering's lock angle in degrees.
     */
    public float getSteeringLockAngle() {
        return steeringLockAngle;
    }

    /**
     * Gets the steering's angular velocity in degrees per seconds.
     */
    public float getSteeringAngularVelocity() {
        return steeringAngularVelocity;
    }
}
