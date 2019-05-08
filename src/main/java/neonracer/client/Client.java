package neonracer.client;

import neonracer.core.GameContext;
import neonracer.network.MessageHandler;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;
import neonracer.util.BuildInfo;

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

    }

    @Override
    public void handle(Login.LoginResponse loginResponse) {

    }

    @Override
    public void handle(Race.Prepare prepareRace) {

    }

    @Override
    public void handle(Race.Join joinRace) {

    }

    @Override
    public void handle(Race.Leave leaveRace) {

    }

    @Override
    public void handle(Race.Start startRace) {

    }

    @Override
    public void handle(Race.Finish finishRace) {

    }

    @Override
    public void handle(Entity.Create createEntity) {

    }

    @Override
    public void handle(Entity.Update updateEntity) {

    }

    @Override
    public void handle(Entity.Delete deleteEntity) {

    }
}
