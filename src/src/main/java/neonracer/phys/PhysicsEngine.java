package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.Entity;
import neonracer.phys.entity.CarPhysics;
import neonracer.phys.entity.EntityPhysics;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsEngine {

    private World world;

    private GameContext gameContext;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
        this.world = new World(new Vec2(0, 0f));
        this.world.setContinuousPhysics(true);
        this.world.setWarmStarting(true);
        for (Entity entity : gameContext.getGameState().getEntities()) {
            entity.onInitialize(gameContext);
        }
    }

    public void onTick() {
        GameState gameState = gameContext.getGameState();

        float tps = gameContext.getTimer().getTicksPerSecond();
        world.step(1f / tps, 8, 3);

        for (Entity entity : gameState.getEntities()) {
            EntityPhysics physics = entity.getPhysics();
            if (physics != null) {
                entity.setPosition(physics.getPosition());
                entity.setRotation(physics.getRotation());
            }
        }

        ((CarPhysics) gameState.getPlayerEntity().getPhysics()).update(gameContext.getControlState());

    }

    public World getWorld() {
        return world;
    }
}
