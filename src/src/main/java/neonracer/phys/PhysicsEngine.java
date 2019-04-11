package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.Entity;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsEngine {

    private World world;

    private GameContext gameContext;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
        this.world = new World(new Vec2(0, 0));
        for (Entity entity : gameContext.getGameState().getEntities()) {
            entity.onInitialize(gameContext);
        }
    }

    public void onTick() {
        GameState gameState = gameContext.getGameState();

        float tps = gameContext.getTimer().getTicksPerSecond();
        world.step(1f / tps, 6, 2);

        for (Entity entity : gameState.getEntities()) {
            EntityPhysics physics = entity.getPhysics();
            if (physics != null) {
                entity.setPosition(Box2dHelper.toVector2f(physics.getBody().getPosition()));
                entity.setRotation(physics.getBody().getAngle());
            }
        }
    }

    public World getWorld() {
        return world;
    }
}
