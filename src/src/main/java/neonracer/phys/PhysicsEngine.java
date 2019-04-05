package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.EntityCar;
import neonracer.phys.Box2DImplementation;
import org.joml.Vector2f;

public class PhysicsEngine {

    private GameContext gameContext;

    private float x = 0.01f;
    private float y = 0.005f;
    private Vector2f v = new Vector2f(x,y);;

    private float omega = 1;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void onTick() {
        GameState gameState = gameContext.getGameState();
        EntityCar playerEntity = gameState.getPlayerEntity();
        playerEntity.setPosition(updatePosition(playerEntity.getPosition()));
        playerEntity.setRotation(updateRotation(playerEntity.getRotation()));
    }

    private Vector2f updatePosition(Vector2f curr)
    {
        return curr.add(v);
    }

    private float updateRotation(float curr)
    {
        return curr+omega;
    }

}
