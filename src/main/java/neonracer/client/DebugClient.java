package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.core.GameContext;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

import java.io.IOException;

public class DebugClient implements Client {

    private int entityCounter;

    @Override
    public void initialize(GameContext gameContext) {

    }

    @Override
    public boolean connect(String ip) {
        return false;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void send(AbstractMessage message) {
    }

    @Override
    public long newEntityId() {
        return (long) entityCounter++;
    }

    @Override
    public void handle(Login.LoginRequest loginRequest) throws IOException {

    }

    @Override
    public void handle(Login.LoginResponse loginResponse) throws IOException {

    }

    @Override
    public void handle(Race.Prepare prepareRace) throws IOException {

    }

    @Override
    public void handle(Race.Join joinRace) throws IOException {

    }

    @Override
    public void handle(Race.Leave leaveRace) throws IOException {

    }

    @Override
    public void handle(Race.Start startRace) throws IOException {

    }

    @Override
    public void handle(Race.Finish finishRace) throws IOException {

    }

    @Override
    public void handle(Entity.Create createEntity) throws IOException {

    }

    @Override
    public void handle(Entity.Update updateEntity) throws IOException {

    }

    @Override
    public void handle(Entity.Delete deleteEntity) throws IOException {

    }
}
