package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.EntityCar;
import neonracer.phys.Box2DImplementation;
import org.joml.Vector2f;

public class PhysicsEngine {

    private GameContext gameContext;

    private Box2DImplementation box2DImplementation;

    private float x = 0.01f;
    private float y = 0.005f;
    private Vector2f v = new Vector2f(x,y);;

    private float omega = 1;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
        box2DImplementation = new Box2DImplementation();
        box2DImplementation.createEntity();
    }

    public void onTick() {
        GameState gameState = gameContext.getGameState();
        EntityCar playerEntity = gameState.getPlayerEntity();
        playerEntity.setPosition(updatePosition(playerEntity.getPosition()));//updatePosition(playerEntity.getPosition()));
        playerEntity.setRotation(updateRotation(playerEntity.getRotation()));
        box2DImplementation.step(1.0f / 60f,8,10);
    }

    private Vector2f updatePosition(Vector2f curr)
    {
        Vector2f vel = new Vector2f(box2DImplementation.currentVelocity());
        vel.mul(150f/600f);
        curr.add(vel);
        return curr;
    }

    private float updateRotation(float curr)
    {
        return curr-box2DImplementation.currentAngularVelocity();
    }

}
