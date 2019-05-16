package neonracer.network;

import com.google.protobuf.AbstractMessage;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Login;
import neonracer.network.proto.Race;

import java.io.IOException;

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
        if (id >= messages.length)
            throw new IllegalArgumentException("The message ID must be smaller than " + messages.length);
        return messages[id];
    }

    public static int getId(Class<? extends AbstractMessage> clazz) {
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].clazz == clazz)
                return i;
        }
        throw new IllegalArgumentException("Undefined message " + clazz.getName());
    }

    private final Class<? extends AbstractMessage> clazz;
    private final Parser parser;

    private Message(Class<? extends AbstractMessage> clazz, Parser parser) {
        this.clazz = clazz;
        this.parser = parser;
    }

    void handle(byte[] buffer, MessageHandler handler) throws IOException {
        parser.invoke(buffer, handler);
    }

    private interface Parser {
        void invoke(byte[] buffer, MessageHandler handler) throws IOException;
    }

}
