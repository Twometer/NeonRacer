package neonracer.phys;

import neonracer.core.GameContext;
import neonracer.core.GameState;
import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityCar;
import neonracer.phys.Box2DImplementation;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine {

    private GameContext gameContext;

    private Box2DImplementation box2DImplementation;

    private List<Entity> entities;

    private float tickRate = 20;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
        box2DImplementation = new Box2DImplementation();
        entities = gameContext.getGameState().getEntities();
    }

    public void onTick() {
        box2DImplementation.step(1.0f / tickRate,8,10);
        for (Entity entity : entities)
        {
            if(entity instanceof PhysEntity)
            {
                ((PhysEntity) entity).updatePhysState();
            }
        }
    }

    public float getTickrate() { return tickRate; }

    public Box2DImplementation getBox2DImplementation() { return box2DImplementation; }

    public void setTickrate(float tickrate) { this.tickRate = tickrate; }
}
