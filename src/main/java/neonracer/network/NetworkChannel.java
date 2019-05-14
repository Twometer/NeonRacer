package neonracer.network;

import com.google.protobuf.AbstractMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkChannel implements Closeable {

    private final Socket socket;

    public NetworkChannel(Socket socket) {
        this.socket = socket;
    }

    public boolean read(MessageHandler handler) throws IOException {
        InputStream stream = socket.getInputStream();
        int id = stream.read();
        if (id == -1) return false;
        int length0 = stream.read(), length1 = stream.read();
        if (length0 == -1 || length1 == -1) return false;
        int length = length0 | (length1 << 8);
        byte[] buffer = new byte[length];
        int received = 0;
        do {
            received += stream.read(buffer, received, length - received);
        } while (received < length);
        Message message = Message.get(id);
        message.handle(buffer, handler);
        return true;
    }

    public void send(AbstractMessage message) throws IOException {
        OutputStream stream = socket.getOutputStream();
        stream.write((byte) Message.getId(message.getClass()));
        byte[] buffer = message.toByteArray();
        stream.write((byte) buffer.length);
        stream.write((byte) (buffer.length >> 8));
        stream.write(buffer);
        stream.flush();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
