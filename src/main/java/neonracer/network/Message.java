package neonracer.network;

import com.google.protobuf.InvalidProtocolBufferException;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

public class Message {

    private static final Message[] messages = {
            new Message(Login.LoginRequest.class, (buffer, handler) -> handler.handle(Login.LoginRequest.parseFrom(buffer))),
            new Message(Login.LoginResponse.class, (buffer, handler) -> handler.handle(Login.LoginResponse.parseFrom(buffer))),
            new Message(Race.Prepare.class, (buffer, handler) -> handler.handle(Race.Prepare.parseFrom(buffer))),
            new Message(Race.Join.class, (buffer, handler) -> handler.handle(Race.Join.parseFrom(buffer))),
            new Message(Race.Leave.class, (buffer, handler) -> handler.handle(Race.Leave.parseFrom(buffer))),
            new Message(Race.Start.class, (buffer, handler) -> handler.handle(Race.Start.parseFrom(buffer))),
            new Message(Race.Finish.class, (buffer, handler) -> handler.handle(Race.Finish.parseFrom(buffer))),
            new Message(Entity.Create.class, (buffer, handler) -> handler.handle(Entity.Create.parseFrom(buffer))),
            new Message(Entity.Update.class, (buffer, handler) -> handler.handle(Entity.Update.parseFrom(buffer))),
            new Message(Entity.Delete.class, (buffer, handler) -> handler.handle(Entity.Delete.parseFrom(buffer)))
    };

    public static Message get(int id) {
        return messages[id];
    }

    private final Class<?> clazz;
    private final MessageParser parser;

    private Message(Class<?> clazz, MessageParser parser) {
        this.clazz = clazz;
        this.parser = parser;
    }

    public void handle(byte[] buffer, MessageHandler handler) throws InvalidProtocolBufferException {
        parser.invoke(buffer, handler);
    }

}
