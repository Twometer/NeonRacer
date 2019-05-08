package neonracer.client;

import neonracer.core.GameContext;
import neonracer.network.MessageHandler;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;
import neonracer.util.BuildInfo;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements MessageHandler {

    private GameContext gameContext;

    private Socket socket;

    private NetworkChannel networkChannel;

    private boolean connected;

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
            return false;
        }
    }

    private void startReceiveLoop() {
        connected = true;
        new Thread(() -> {
            while (connected) {
                try {
                    networkChannel.read(this);
                } catch (IOException e) {
                    connected = false;
                    break;
                }
            }
        }).start();
    }

    @Override
    public void handle(Login.LoginRequest loginRequest) {
        throw new UnsupportedOperationException("Client does not handle login requests");
    }

    @Override
    public void handle(Login.LoginResponse loginResponse) {
        EventBus.getDefault().post(loginResponse);
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
    }

    @Override
    public void handle(Entity.Update updateEntity) {
        EventBus.getDefault().post(updateEntity);
    }

    @Override
    public void handle(Entity.Delete deleteEntity) {
        EventBus.getDefault().post(deleteEntity);
    }

}
