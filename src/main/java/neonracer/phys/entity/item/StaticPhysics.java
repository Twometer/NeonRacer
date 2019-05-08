package neonracer.phys.entity.item;

import neonracer.core.GameContext;
import neonracer.model.entity.EntityStatic;
import neonracer.phys.Box2dHelper;
import neonracer.phys.entity.EntityPhysics;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

public class StaticPhysics implements EntityPhysics {

    private Body body;

    private StaticPhysics(Body body) {
        this.body = body;
    }

    public static StaticPhysics create(EntityStatic entity, GameContext context) {
        World world = context.getPhysicsEngine().getWorld();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(Box2dHelper.toVec2(entity.getPosition()));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(entity.getWidth() / 2, entity.getHeight() / 2, new Vec2(0f, entity.getHeight() / 2), 0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return new StaticPhysics(body);
    }

    @Override
    public Vector2f getPosition() {
        return Box2dHelper.toVector2f(body.getPosition());
    }

    @Override
    public float getRotation() {
        return body.getAngle();
    }

    @Override
    public void update() {

    }
}
