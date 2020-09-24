package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

public class DebugClient extends Client {

    @Override
    public boolean connect(String ip) {
        return true;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void send(AbstractMessage message) {
    }
}
