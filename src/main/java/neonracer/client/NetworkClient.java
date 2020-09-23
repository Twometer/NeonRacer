package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.model.entity.EntityCar;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;
import neonracer.util.BuildInfo;
import neonracer.util.IdConversion;
import org.greenrobot.eventbus.EventBus;
import org.joml.Vector2f;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NetworkClient implements Client {

    private final Map<Integer, String> connectedUsers = new HashMap<>();

    private GameContext gameContext;

    private Socket socket;

    private NetworkChannel networkChannel;

    private boolean connected;

    private int clientId;

    private int entityCounter;

    @Override
    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public boolean connect(String ip) {
        try {
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(ip, BuildInfo.getNetworkPort()));
            this.networkChannel = new NetworkChannel(socket);
            startReceiveLoop();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceiveLoop() {
        connected = true;
        Thread thread = new Thread(() -> {
            while (connected) {
                try {
                    networkChannel.read(this);
                } catch (IOException e) {
                    connected = false;
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void send(AbstractMessage message) {
        try {
            networkChannel.send(message);
        } catch (IOException e) {
            connected = false;
            e.printStackTrace();
        }
    }

    @Override
    public long newEntityId() {
        return (long) clientId << 32 | (long) entityCounter++;
    }

    @Override
    public void handle(Login.LoginRequest loginRequest) {
        throw new UnsupportedOperationException("NetworkClient does not handle login requests");
    }

    @Override
    public void handle(Login.LoginResponse loginResponse) {
        EventBus.getDefault().post(loginResponse);
        clientId = loginResponse.getClientId();
    }

    @Override
    public void handle(Race.Prepare prepareRace) {
        EventBus.getDefault().post(prepareRace);
    }

    @Override
    public void handle(Race.Join joinRace) {
        EventBus.getDefault().post(joinRace);
        connectedUsers.put(joinRace.getClientId(), joinRace.getNickname());
    }

    @Override
    public void handle(Race.Leave leaveRace) {
        EventBus.getDefault().post(leaveRace);
        connectedUsers.remove(leaveRace.getClientId());
        gameContext.getGameState().getEntities().removeIf(entity -> IdConversion.toClientId(entity.getEntityId()) == leaveRace.getClientId());
    }

    @Override
    public void handle(Race.Start startRace) {
        EventBus.getDefault().post(startRace);
        gameContext.getGameState().setCurrentTotalLaps(startRace.getLapCount());
    }

    @Override
    public void handle(Race.Finish finishRace) {
        EventBus.getDefault().post(finishRace);
    }

    @Override
    public void handle(Entity.Create createEntity) {
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
            case "rocket":
                // TODO
                break;
        }


        if (entity != null)
            gameContext.getGameState().getEntities().add(entity);
    }

    @Override
    public void handle(Entity.Update updateEntity) {
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
    public void handle(Entity.Delete deleteEntity) {
        EventBus.getDefault().post(deleteEntity);
        for (neonracer.model.entity.Entity entity : gameContext.getGameState().getEntities())
            if (entity.getEntityId() == deleteEntity.getEntityId()) {
                gameContext.getGameState().getEntities().remove(entity);
                break;
            }
    }

}
