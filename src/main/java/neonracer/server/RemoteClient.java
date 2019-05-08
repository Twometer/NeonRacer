package neonracer.server;

import neonracer.network.MessageHandler;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

import java.io.IOException;
import java.net.Socket;

public class RemoteClient implements MessageHandler {

    private static final String CANNOT_HANDLE = "Server cannot handle LoginResponse message.";

    private final Socket socket;
    private final NetworkChannel channel;

    RemoteClient(Socket socket) {
        this.socket = socket;
        this.channel = new NetworkChannel(socket);
    }

    void start() {
        new Thread(this::receiveLoop).start();
    }

    private void receiveLoop() {
        while (ServerMain.isRunning()) {
            try {
                channel.read(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(Login.LoginRequest loginRequest) {

    }

    @Override
    public void handle(Login.LoginResponse loginResponse) {
        throw new IllegalArgumentException(CANNOT_HANDLE);
    }

    @Override
    public void handle(Race.Prepare prepareRace) {
        throw new IllegalArgumentException(CANNOT_HANDLE);
    }

    @Override
    public void handle(Race.Join joinRace) {

    }

    @Override
    public void handle(Race.Leave leaveRace) {

    }

    @Override
    public void handle(Race.Start startRace) {
        throw new IllegalArgumentException(CANNOT_HANDLE);
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
