package neonracer.network;

import neonracer.network.proto.Login;

public class Test {

    void test() {
        Login.Request.newBuilder().setNickname("test").build();
        Login.Response.newBuilder().setStatus(Login.Status.SUCCESS);
    }
}
