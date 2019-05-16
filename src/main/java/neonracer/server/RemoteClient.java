package neonracer.server;

import com.google.protobuf.AbstractMessage;
import neonracer.network.MessageHandler;
import neonracer.network.NetworkChannel;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

import java.io.Closeable;
import java.io.IOException;

class RemoteClient implements MessageHandler, Closeable {

    private static final String CANNOT_HANDLE = "Server cannot handle LoginResponse message.";

    private final NetworkChannel channel;
    private final GameServer parent;

    private int id;
    private String nickname;

    RemoteClient(NetworkChannel channel, GameServer parent) {
        this.channel = channel;
        this.parent = parent;
    }

    int getId() {
        return id;
    }

    void send(AbstractMessage message) throws IOException {
        channel.send(message);
    }

    boolean trySend(AbstractMessage message) {
        try {
            channel.send(message);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void run() {
        try {
            while (true) {
                if (!channel.read(this))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        leaveRace();
        if (!parent.getClients().remove(this))
            System.out.println("Failed to remove disconnected client from client list.");
        else if (nickname != null)
            System.out.println("Client disconnected (" + nickname + ")");
        else
            System.out.println("Anonymous client disconnected");
    }

    @Override
    public void handle(Login.LoginRequest loginRequest) throws IOException {
        for (RemoteClient client : parent.getClients()) {
            if (client.nickname != null && client.nickname.equalsIgnoreCase(loginRequest.getNickname())) {
                channel.send(Login.LoginResponse.newBuilder().setStatus(Login.LoginStatus.NICKNAME_TAKEN).build());
                return;
            }
        }

        id = parent.getClientIdCounter().incrementAndGet();
        nickname = loginRequest.getNickname();
        channel.send(Login.LoginResponse.newBuilder().setStatus(Login.LoginStatus.SUCCESS).setClientId(id).build());

        System.out.println(nickname + " logged in");

        // Send all entities
        RaceManager race = parent.getRace();
        if (race != null) {
            for (RemoteClient client : race.getParticipants()) {
                send(Race.Join.newBuilder().setClientId(client.id).setNickname(client.nickname).build());
            }
            for (Entity entity : race.getEntities()) {
                send(neonracer.network.proto.Entity.Create.newBuilder()
                        .setEntityId(entity.getId())
                        .setX(entity.getX())
                        .setY(entity.getY())
                        .setRotation(entity.getRotation())
                        .setType(entity.getType())
                        .build());
            }
        }
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
        RaceManager race = parent.getRace();
        if (race == null) {
            race = new RaceManager(parent);
            parent.setRace(race);
        }
        if (race.isOpen()) {
            race.getParticipants().add(this);
            parent.sendExcept(Race.Join.newBuilder(joinRace).setClientId(id).setNickname(nickname).build(), this);
            System.out.println(nickname + " joined the upcoming race");
        } else {
            // Client cannot join anymore, notify with Race.Leave message
            send(Race.Leave.newBuilder().setClientId(id).build());
        }
    }

    @Override
    public void handle(Race.Leave leaveRace) {
        leaveRace();
    }

    private void leaveRace() {
        RaceManager race = parent.getRace();
        if (race != null) {
            boolean found = race.getParticipants().remove(this);
            race.getEntities().removeIf(entity -> entity.getOwnerId() == id);
            if (found) {
                Race.Leave message = Race.Leave.newBuilder().setClientId(id).build();
                parent.sendExcept(message, this);
            }
            // Delete race when all clients have left
            if (race.getParticipants().isEmpty()) {
                parent.setRace(null);
            }
        }
    }

    @Override
    public void handle(Race.Start startRace) {
        throw new UnsupportedOperationException(CANNOT_HANDLE);
    }

    @Override
    public void handle(Race.Finish finishRace) {
        RaceManager race = parent.getRace();
        if (race != null) {
            boolean found = race.getParticipants().remove(this);
            race.getEntities().removeIf(entity -> entity.getOwnerId() == id);
            if (found) {
                Race.Finish message = Race.Finish.newBuilder(finishRace).setClientId(id).build();
                parent.sendExcept(message, this);
            }
            // Delete race when all clients have left
            if (race.getParticipants().isEmpty()) {
                parent.setRace(null);
            }
        }
    }

    @Override
    public void handle(neonracer.network.proto.Entity.Create createEntity) {
        RaceManager race = parent.getRace();
        if (race != null) {
            race.getEntities().add(new Entity(
                    createEntity.getEntityId(),
                    createEntity.getX(),
                    createEntity.getY(),
                    createEntity.getRotation(),
                    createEntity.getType()));
            parent.sendExcept(createEntity, this);
        }
    }

    @Override
    public void handle(neonracer.network.proto.Entity.Update updateEntity) {
        RaceManager race = parent.getRace();
        if (race != null) {
            for (Entity entity : race.getEntities()) {
                if (entity.getId() == updateEntity.getEntityId()) {
                    entity.setX(updateEntity.getX());
                    entity.setY(updateEntity.getY());
                    entity.setRotation(updateEntity.getRotation());
                }
            }
            parent.sendExcept(updateEntity, this);
        }
    }

    @Override
    public void handle(neonracer.network.proto.Entity.Delete deleteEntity) {
        RaceManager race = parent.getRace();
        if (race != null) {
            race.getEntities().removeIf(entity -> entity.getId() == deleteEntity.getEntityId());
            parent.sendExcept(deleteEntity, this);
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
