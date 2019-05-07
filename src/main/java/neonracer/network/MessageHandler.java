package neonracer.network;

import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

public interface MessageHandler {

    void handle(Login.LoginRequest loginRequest);
    void handle(Login.LoginResponse loginResponse);

    void handle(Race.Prepare prepareRace);
    void handle(Race.Join joinRace);
    void handle(Race.Leave leaveRace);
    void handle(Race.Start startRace);
    void handle(Race.Finish finishRace);

    void handle(Entity.Create createEntity);
    void handle(Entity.Update updateEntity);
    void handle(Entity.Delete deleteEntity);
}
