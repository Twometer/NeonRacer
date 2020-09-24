package neonracer.client;

import com.google.protobuf.AbstractMessage;
import neonracer.network.NetworkChannel;
import neonracer.util.BuildInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkClient extends Client {

    private Socket socket;

    private NetworkChannel networkChannel;

    private boolean connected;

    @Override
    public boolean connect(String ip) {
        try {
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(ip, BuildInfo.getNetworkPort()));
            this.networkChannel = new NetworkChannel(socket);
            startReceiveLoop();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceiveLoop() {
        connected = true;
        Thread thread = new Thread(() -> {
            while (connected) {
                try {
                    networkChannel.read(this);
                } catch (IOException e) {
                    connected = false;
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void send(AbstractMessage message) {
        try {
            networkChannel.send(message);
        } catch (IOException e) {
            connected = false;
            e.printStackTrace();
        }
    }
}
