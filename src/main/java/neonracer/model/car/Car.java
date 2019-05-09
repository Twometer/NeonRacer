package neonracer.model.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.render.gl.core.Texture;
import neonracer.resource.IData;

/**
 * Defines car properties. There are different car
 * models the user can choose from, and each one has
 * different properties.
 */
public class Car implements IData {

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

    @JsonProperty("traction_coefficient")
    private float tractionCoefficient;

    @JsonProperty("forward_force")
    private float forwardForce;

    @JsonProperty("reverse_force")
    private float reverseForce;

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

    public float getDragCoefficient() { return dragCoefficient; }

    public float getRollCoefficient() { return rollCoefficient; }

    public float getTractionCoefficient() { return tractionCoefficient; }

    public float getForwardForce() { return forwardForce; }

    public float getReverseForce() { return reverseForce; }
}
