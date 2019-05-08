package neonracer.server;

import com.google.protobuf.AbstractMessage;
import neonracer.network.MessageHandler;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

import java.io.IOException;
import java.net.Socket;

class RemoteClient implements MessageHandler {

    private static final String CANNOT_HANDLE = "Server cannot handle LoginResponse message.";

    private final Socket socket;
    private final NetworkChannel channel;

    private int id;
    private String nickname;

    RemoteClient(Socket socket) {
        this.socket = socket;
        this.channel = new NetworkChannel(socket);
    }

    int getId() {
        return id;
    }

    String getNickname() {
        return nickname;
    }

    void start() {
        new Thread(this::receiveLoop).start();
    }

    void send(AbstractMessage message) throws IOException {
        channel.send(message);
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
    public void handle(Login.LoginRequest loginRequest) throws IOException {
        for (RemoteClient client : ServerMain.getClients()) {
            if (client.nickname.equalsIgnoreCase(loginRequest.getNickname())) {
                channel.send(Login.LoginResponse.newBuilder().setStatus(Login.LoginStatus.NICKNAME_TAKEN).build());
                return;
            }
        }

        id = ServerMain.getConnectionCounter().incrementAndGet();
        nickname = loginRequest.getNickname();
        ServerMain.getClients().add(this);
        channel.send(Login.LoginResponse.newBuilder().setStatus(Login.LoginStatus.SUCCESS).build());
    }

    @Override
    public void handle(Login.LoginResponse loginResponse) {
        throw new UnsupportedOperationException(CANNOT_HANDLE);
    }

    @Override
    public void handle(Race.Prepare prepareRace) {
        throw new UnsupportedOperationException(CANNOT_HANDLE);
    }

    @Override
    public void handle(Race.Join joinRace) throws IOException {
        ServerMain.getParticipants().add(this);
        Race.Join message = Race.Join.newBuilder(joinRace).setClientId(id).setNickname(nickname).build();
        ServerMain.sendExcept(message, this);
    }

    @Override
    public void handle(Race.Leave leaveRace) throws IOException {
        ServerMain.getParticipants().remove(this);
        ServerMain.getEntities().removeIf(entity -> entity.getOwnerId() == id);
        Race.Leave message = Race.Leave.newBuilder(leaveRace).setClientId(id).build();
        ServerMain.sendExcept(message, this);
    }

    @Override
    public void handle(Race.Start startRace) {
        throw new UnsupportedOperationException(CANNOT_HANDLE);
    }

    @Override
    public void handle(Race.Finish finishRace) {

    }

    @Override
    public void handle(Entity.Create createEntity) throws IOException {
        ServerMain.getEntities().add(new RemoteEntity(
                createEntity.getEntityId(),
                createEntity.getX(),
                createEntity.getY(),
                createEntity.getRotation(),
                createEntity.getType()));
        ServerMain.sendExcept(createEntity, this);
    }

    @Override
    public void handle(Entity.Update updateEntity) throws IOException {
        for (RemoteEntity entity : ServerMain.getEntities()) {
            if (entity.getId() == updateEntity.getEntityId()) {
                // TODO: Update values
            }
        }
        ServerMain.sendExcept(updateEntity, this);
    }

    @Override
    public void handle(Entity.Delete deleteEntity) throws IOException {
        ServerMain.getEntities().removeIf(entity -> entity.getId() == deleteEntity.getEntityId());
        ServerMain.sendExcept(deleteEntity, this);
    }
}
