package neonracer.model.car;

import neonracer.render.gl.Texture;

/**
 * Defines car properties. There are different car
 * models the user can choose from, and each one has
 * different properties.
 */
public class CarModel {

    private String id;

    private String name;

    private String description;

    private Texture colorTexture;

    private Texture glowTexture;

    private float acceleration;

    private float brakeStrength;

    private float aerodynamics;

    private float friction;

    private float horsepower;

    private float weight;

    private Ability primaryAbility;

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

    public Ability getPrimaryAbility() {
        return primaryAbility;
    }
}
