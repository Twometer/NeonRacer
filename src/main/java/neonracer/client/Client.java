package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.core.GameContext;
import neonracer.network.MessageHandler;

public interface Client extends MessageHandler {
    void initialize(GameContext gameContext);
    boolean connect(String ip);
    void disconnect();
    void send(AbstractMessage message);
    long newEntityId();
}
