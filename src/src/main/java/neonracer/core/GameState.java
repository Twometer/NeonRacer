package neonracer.core;

import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityCar;
import neonracer.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private Track currentTrack;

    private EntityCar playerEntity;

    private List<Entity> entities = new ArrayList<>();

    public EntityCar getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(EntityCar playerEntity) {
        this.playerEntity = playerEntity;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}