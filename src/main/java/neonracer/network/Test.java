package neonracer.network;

import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;

public class Test {

    void test() {
        Login.Request.newBuilder().setNickname("test").build();
        Login.Response.newBuilder().setStatus(Login.Status.SUCCESS).build();

        Entity.Create.newBuilder().setEntityId(0).build();
        Entity.Update.newBuilder().setEntityId(0).setPositionX(5).setVelocityX(0.1f).build();
        Entity.Delete.newBuilder().setEntityId(0).build();
    }
}
