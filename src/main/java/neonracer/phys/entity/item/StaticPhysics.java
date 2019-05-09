package neonracer.phys.entity.item;

import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityStatic;
import neonracer.phys.Box2dHelper;
import neonracer.phys.entity.EntityPhysics;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

public class StaticPhysics implements EntityPhysics {

    private Entity entity;

    private Body body;

    public StaticPhysics(Entity entity, Body body) {
        this.entity = entity;
        this.body = body;
    }

    public static StaticPhysics create(EntityStatic entity, GameContext context) {
        World world = context.getPhysicsEngine().getWorld();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(Box2dHelper.toVec2(entity.getPosition()));
        bodyDef.angle = entity.getRotation();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(entity.getWidth() / 2, entity.getHeight() / 2, new Vec2(0f, entity.getHeight() / 2), 0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return new StaticPhysics(entity, body);
    }

    @Override
    public Vector2f getPosition() {
        return Box2dHelper.toVector2f(body.getPosition());
    }

    @Override
    public float getRotation() {
        return entity.getRotation();
    }

    @Override
    public void update() {

    }
}
