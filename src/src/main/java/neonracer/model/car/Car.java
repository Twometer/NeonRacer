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

    @JsonProperty("max_forward_speed")
    private float maxForwardSpeed;

    @JsonProperty("max_reverse_speed")
    private float maxReverseSpeed;

    @JsonProperty("max_force_front")
    private float maxFrontForce;

    @JsonProperty("max_force_back")
    private float maxBackForce;

    @JsonProperty("max_lateral_force")
    private float maxLateralForce;

    @JsonProperty("primary_ability")
    private String primaryAbilityId;

    private Ability primaryAbility;

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

    public float getMaxForwardSpeed() {
        return maxForwardSpeed;
    }

    public float getMaxReverseSpeed() {
        return maxReverseSpeed;
    }

    public float getMaxFrontForce() {
        return maxFrontForce;
    }

    public float getMaxBackForce() {
        return maxBackForce;
    }

    public float getMaxLateralForce() {
        return maxLateralForce;
    }

    public Ability getPrimaryAbility() {
        return primaryAbility;
    }
}
