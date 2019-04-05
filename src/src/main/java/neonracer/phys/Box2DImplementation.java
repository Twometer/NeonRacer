package neonracer.phys;

import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.collision.shapes.*;
import org.joml.Vector2f;

public class Box2DImplementation {

    private World world;

    private BodyDef bd;

    private Body body;

    private PolygonShape ps;

    private FixtureDef fd;

    public Box2DImplementation()
    {
        world = new World(new Vec2(0,1));
        world.setWarmStarting(true);
        world.setContinuousPhysics(true);
        bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(0,0);
        ps = new PolygonShape();
        ps.setAsBox(1,1);
        fd = new FixtureDef();
        fd.shape = ps;
    }

    public void createEntity()
    {
        body = world.createBody(bd);
        body.createFixture(fd);
    }

    public void step(float timeStep, int i, int i1) {
        world.step(timeStep,i,i1);
        world.clearForces();
    }

    public Vector2f Vec2toVector2f(Vec2 v)
    {
        Vector2f vf = new Vector2f(v.x,v.y);
        return vf;
    }

    public Vec2 Vector2f2toVec2(Vector2f vf)
    {
        Vec2 v = new Vec2(vf.x,vf.y);
        return v;
    }

    public Vector2f currentPosition()
    {
        Transform xf = body.getTransform();
        return Vec2toVector2f(xf.p);
    }
}
