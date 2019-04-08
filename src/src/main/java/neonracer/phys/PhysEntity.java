package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.render.gl.core.Texture;
import org.joml.Vector2f;

public abstract class PhysEntity extends Entity {

    private PhysicsEngine physicsEngine;

    private Box2DImplementation box2DImplementation ;

    public PhysEntity(String type, float x, float y, float rotation)
    {
        super(type, x, y, rotation);
    }

    public PhysEntity(String type, float x, float y, float rotation, GameContext gameContext)
    {
        super(type, x, y, rotation, gameContext);
        this.physicsEngine = gameContext.getPhysicsEngine();
        this.box2DImplementation = physicsEngine.getBox2DImplementation();
        box2DImplementation.createEntity(getPosition(), getEntityWidth(), getEntityLength());
    }

    public abstract Texture getColorTexture();

    public abstract Texture getGlowTexture();

    public void updatePhysState()
    {
        Vector2f newPos = new Vector2f();
        getPosition().add(box2DImplementation.currentVelocity(),newPos);
        setPosition(newPos);
        setRotation(getRotation() + box2DImplementation.currentAngularVelocity());
    }

    public abstract float getEntityWidth();

    public abstract float getEntityLength();

}
