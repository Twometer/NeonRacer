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
        if (message instanceof Login.LoginRequest) {
            Login.LoginResponse response = Login.LoginResponse.newBuilder().setStatus(Login.LoginStatus.SUCCESS).build();
            handle(response);
        } else if (message instanceof Race.Join) {
            Race.Start start = Race.Start.newBuilder().setElapsedMilliseconds(-3000).setLapCount(2).build();
            handle(start);
        }
    }
}
