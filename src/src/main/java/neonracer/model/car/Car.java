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

    @JsonProperty
    private float acceleration;

    @JsonProperty("brake_strength")
    private float brakeStrength;

    @JsonProperty
    private float aerodynamics;

    @JsonProperty
    private float friction;

    @JsonProperty
    private float horsepower;

    @JsonProperty
    private float weight;

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

    public float getAcceleration() {
        return acceleration;
    }

    public float getBrakeStrength() {
        return brakeStrength;
    }

    public float getAerodynamics() {
        return aerodynamics;
    }

    public float getFriction() {
        return friction;
    }

    public float getHorsepower() {
        return horsepower;
    }

    public float getWeight() {
        return weight;
    }

    public float getCarLength() { return 1; }//colorTexture.getWidth()/150;} //TODO

    public float getCarWidth() { return 1; }//colorTexture.getHeight()/150; } //TODO

    public Ability getPrimaryAbility() {
        return primaryAbility;
    }
}
