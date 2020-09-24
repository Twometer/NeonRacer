package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.model.entity.EntityCar;
import neonracer.network.MessageHandler;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;
import neonracer.util.IdConversion;
import org.greenrobot.eventbus.EventBus;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public abstract class Client implements MessageHandler {

    protected final Map<Integer, String> connectedUsers = new HashMap<>();

    protected GameContext gameContext;

    protected int clientId;

    protected int entityCounter;

    public final void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public abstract boolean connect(String ip);

    public abstract void disconnect();

    public abstract void send(AbstractMessage message);

    public final long newEntityId() {
        return (long) clientId << 32 | (long) entityCounter++;
    }

    @Override
    public final void handle(Login.LoginRequest loginRequest) {
        throw new UnsupportedOperationException("NetworkClient does not handle login requests");
    }

    @Override
    public final void handle(Login.LoginResponse loginResponse) {
        EventBus.getDefault().post(loginResponse);
        clientId = loginResponse.getClientId();
    }

    @Override
    public final void handle(Race.Prepare prepareRace) {
        EventBus.getDefault().post(prepareRace);
    }

    @Override
    public final void handle(Race.Join joinRace) {
        EventBus.getDefault().post(joinRace);
        connectedUsers.put(joinRace.getClientId(), joinRace.getNickname());
    }

    @Override
    public final void handle(Race.Leave leaveRace) {
        EventBus.getDefault().post(leaveRace);
        connectedUsers.remove(leaveRace.getClientId());
        gameContext.getGameState().getEntities().removeIf(entity -> IdConversion.toClientId(entity.getEntityId()) == leaveRace.getClientId());
    }

    @Override
    public final void handle(Race.Start startRace) {
        EventBus.getDefault().post(startRace);
        gameContext.getGameState().setCurrentTotalLaps(startRace.getLapCount());
    }

    @Override
    public final void handle(Race.Finish finishRace) {
        EventBus.getDefault().post(finishRace);
    }

    @Override
    public final void handle(Entity.Create createEntity) {
        EventBus.getDefault().post(createEntity);
        neonracer.model.entity.Entity entity = null;

        switch (createEntity.getType()) {
            case "rocketcar":
            case "sportscar":
            case "kart":
                Car car = gameContext.getAssetProvider().getCar(createEntity.getType());
                String user = connectedUsers.get(IdConversion.toClientId(createEntity.getEntityId()));
                entity = new EntityCar(createEntity.getEntityId(), createEntity.getX(), createEntity.getY(), createEntity.getRotation(), car, user);
                break;
        }


        if (entity != null)
            gameContext.getGameState().getEntities().add(entity);
    }

    @Override
    public final void handle(Entity.Update updateEntity) {
        EventBus.getDefault().post(updateEntity);

        for (neonracer.model.entity.Entity entity : gameContext.getGameState().getEntities())
            if (entity instanceof EntityCar && entity.getEntityId() == updateEntity.getEntityId()) {
                EntityCar car = (EntityCar) entity;
                car.getCarStats().setLapsPassed(updateEntity.getLapsPassed());
                car.setPosition(new Vector2f(updateEntity.getX(), updateEntity.getY()));
                car.setRotation(updateEntity.getRotation());
            }
    }

    @Override
    public final void handle(Entity.Delete deleteEntity) {
        EventBus.getDefault().post(deleteEntity);
        for (neonracer.model.entity.Entity entity : gameContext.getGameState().getEntities())
            if (entity.getEntityId() == deleteEntity.getEntityId()) {
                gameContext.getGameState().getEntities().remove(entity);
                break;
            }
    }
}
