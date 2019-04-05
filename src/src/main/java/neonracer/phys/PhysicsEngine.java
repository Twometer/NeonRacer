package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.EntityCar;

public class PhysicsEngine {

    private GameContext gameContext;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void onTick() {
        GameState gameState = gameContext.getGameState();
        EntityCar playerEntity = gameState.getPlayerEntity();
        // TODO
    }

}
