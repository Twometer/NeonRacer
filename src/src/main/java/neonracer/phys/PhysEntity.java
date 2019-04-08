package neonracer.phys;

import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.model.entity.Entity;
import neonracer.render.gl.core.Texture;
import org.joml.Vector2f;

public abstract class PhysEntity extends Entity {

    public PhysEntity(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation)
    {
        super(type, x, y, rotation, true);
    }

    @Override
    public abstract Texture getColorTexture();

    @Override
    public abstract Texture getGlowTexture();

    private Box2DImplementation box2DImplementation ;

    public void initialisePhysics(Box2DImplementation box2DImplementation)
    {
        this.box2DImplementation = box2DImplementation;
        box2DImplementation.createEntity(getPosition().mul(1f / 150f), getEntityWidth() / 150f, getEntityLength() / 150f);
    }

    public void updatePhysState()
    {
        int test = box2DImplementation.testTest();
        //setPosition(getPosition().add(box2DImplementation.currentVelocity().mul(150f)));
        //setRotation(getRotation() + box2DImplementation.currentAngularVelocity());
    }

    public float getEntityWidth() { return 0f; }

    public float getEntityLength() { return 0f; }

}
