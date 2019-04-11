package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.phys.EntityPhysics;
import neonracer.render.gl.core.Texture;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class EntityCar extends Entity {

    private Car car;

    public EntityCar(float x, float y, float rotation, Car car) {
        super("car", x, y, rotation);
        this.car = car;
    }

    @JsonCreator
    public EntityCar(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        super(type, x, y, rotation);
    }

    public Car getCar() {
        return car;
    }

    @Override
    public Texture getColorTexture() {
        return car.getColorTexture();
    }

    @Override
    public Texture getGlowTexture() {
        return car.getGlowTexture();
    }

    @Override
    public void onInitialize(GameContext gameContext) {
        super.onInitialize(gameContext);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(new Vec2(getPosition().x, getPosition().y));
        bodyDef.angle = this.getRotation();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Body body = gameContext.getPhysicsEngine().getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        setPhysics(new EntityPhysics(body));
    }
}
