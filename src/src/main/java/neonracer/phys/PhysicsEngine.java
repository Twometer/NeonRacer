package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.EntityCar;
import org.joml.Vector2f;

public class PhysicsEngine {

    private GameContext gameContext;

    private float x = 0.01f;
    private float y = 0.005f;
    private Vector2f v = new Vector2f(x,y);;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void onTick() {
        GameState gameState = gameContext.getGameState();
        EntityCar playerEntity = gameState.getPlayerEntity();
        Vector2f player = new Vector2f(playerEntity.getPosition());;
        playerEntity.setPosition(player.add(v));
        float rot = playerEntity.getRotation();
        playerEntity.setRotation(rot+1f);
    }

}
