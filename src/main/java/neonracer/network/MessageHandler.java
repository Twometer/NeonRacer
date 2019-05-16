package neonracer.network;

import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

import java.io.IOException;

public interface MessageHandler {

    void handle(Login.LoginRequest loginRequest) throws IOException;
    void handle(Login.LoginResponse loginResponse) throws IOException;

    void handle(Race.Prepare prepareRace) throws IOException;
    void handle(Race.Join joinRace) throws IOException;
    void handle(Race.Leave leaveRace) throws IOException;
    void handle(Race.Start startRace) throws IOException;
    void handle(Race.Finish finishRace) throws IOException;

    void handle(Entity.Create createEntity) throws IOException;
    void handle(Entity.Update updateEntity) throws IOException;
    void handle(Entity.Delete deleteEntity) throws IOException;
}
