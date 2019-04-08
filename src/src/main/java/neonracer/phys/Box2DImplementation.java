package neonracer.phys;

import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.Body;
//import org.jbox2d.dynamics.joints.JointDef;
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
        world = new World(new Vec2(0,-0.1f));
        world.setWarmStarting(true);
        world.setContinuousPhysics(true);
    }

    public void createEntity(Vector2f pos, float width, float length)
    {
        bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(Vector2f2toVec2(pos));
        ps = new PolygonShape();
        ps.setAsBox(width, length);
        fd = new FixtureDef();
        fd.shape = ps;
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

    public Vector2f currentVelocity()
    {
        Vector2f vec = new Vector2f(1,1);
        //Transform xf = body.getTransform();
        return vec;//Vec2toVector2f(xf.p);
    }

    public float currentAngularVelocity()
    {
        return body.getAngle();
    }

    public int testTest() {return 0;};
}
