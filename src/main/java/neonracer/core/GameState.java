package neonracer.core;

import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityCar;
import neonracer.model.track.Track;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {

    private GameContext gameContext;

    private int currentTotalLaps;

    private Track currentTrack;

    private EntityCar playerEntity;

    private List<Entity> entities = new CopyOnWriteArrayList<>();

    public EntityCar getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(EntityCar playerEntity) {
        this.playerEntity = playerEntity;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public int getCurrentTotalLaps() {
        return currentTotalLaps;
    }

    public void setCurrentTotalLaps(int currentTotalLaps) {
        this.currentTotalLaps = currentTotalLaps;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
        for (Entity trackEntity : currentTrack.getEntities())
            addEntity(trackEntity);
    }

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        entity.onInitialize(gameContext);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public boolean isRacing() {
        return playerEntity != null;
    }

}
