package neonracer.network;

import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

public class Test {

    void test() {
        Login.LoginRequest.newBuilder().setNickname("test").build();
        Login.LoginResponse.newBuilder().setStatus(Login.LoginStatus.SUCCESS).build();

        Entity.Create.newBuilder().setEntityId(0).build();
        Entity.Update.newBuilder().setEntityId(0).setX(5).setVelocity(0.1f).build();
        Entity.Delete.newBuilder().setEntityId(0).build();

        Race.Prepare.newBuilder().setRemainingMilliseconds(10000).build();
        Race.Join.newBuilder().build();
        Race.Start.newBuilder().setElapsedMilliseconds(-5000).setLapCount(3).build();
    }
}
