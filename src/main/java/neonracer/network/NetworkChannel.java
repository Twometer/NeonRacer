package neonracer.network;

import com.google.protobuf.AbstractMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkChannel implements Closeable {

    private static byte readByte(InputStream stream) throws IOException {
        int value = stream.read();
        if (value == -1) throw new IOException("End of stream");
        return (byte) value;
    }

    private final Socket socket;

    public NetworkChannel(Socket socket) {
        this.socket = socket;
    }

    public void read(MessageHandler handler) throws IOException {
        InputStream stream = socket.getInputStream();
        byte id = readByte(stream);
        int length = readByte(stream) | (readByte(stream) << 8);
        byte[] buffer = new byte[length];
        int received = 0;
        do {
            received += stream.read(buffer, received, length - received);
        } while (received < length);
        Message message = Message.get(id);
        message.handle(buffer, handler);
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
