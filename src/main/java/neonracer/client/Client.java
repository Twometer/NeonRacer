package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import neonracer.network.MessageHandler;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;
import neonracer.util.BuildInfo;
import org.greenrobot.eventbus.EventBus;
import org.joml.Vector2f;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements MessageHandler {

    private GameContext gameContext;

    private Socket socket;

    private NetworkChannel networkChannel;

    private boolean connected;

    private int clientId;

    private int entityCounter;

    public void initialize(GameContext gameContext) {
        this.gameContext = gameContext;
    }

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

    public void send(AbstractMessage message) {
        try {
            networkChannel.send(message);
        } catch (IOException e) {
            connected = false;
            e.printStackTrace();
        }
    }

    public long newEntityId() {
        return (long) clientId << 32 | (long) entityCounter++;
    }

    @Override
    public void handle(Login.LoginRequest loginRequest) {
        throw new UnsupportedOperationException("Client does not handle login requests");
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
    }

    @Override
    public void handle(Race.Leave leaveRace) {
        EventBus.getDefault().post(leaveRace);
        List<neonracer.model.entity.Entity> removeEntities = new ArrayList<>();
        for (neonracer.model.entity.Entity entity : gameContext.getGameState().getEntities()) {
            if (entity.getEntityId() >> 32 == leaveRace.getClientId()) {
                removeEntities.add(entity);
            }
        }
        gameContext.getGameState().getEntities().removeAll(removeEntities);
    }

    @Override
    public void handle(Race.Start startRace) {
        EventBus.getDefault().post(startRace);
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
                entity = new EntityCar(createEntity.getEntityId(), createEntity.getX(), createEntity.getY(), createEntity.getRotation(), gameContext.getDataManager().getCar(createEntity.getType()));
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
