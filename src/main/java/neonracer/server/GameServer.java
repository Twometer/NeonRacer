package neonracer.server;

import com.google.protobuf.AbstractMessage;
import neonracer.network.NetworkChannel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class GameServer {

    private final ServerSocket listener;
    private final AtomicInteger clientIdCounter;
    private final List<RemoteClient> clients;
    private RaceManager race;

    GameServer(ServerSocket listener) {
        this.listener = listener;
        this.clientIdCounter = new AtomicInteger();
        clients = new CopyOnWriteArrayList<>();
    }

    AtomicInteger getClientIdCounter() {
        return clientIdCounter;
    }

    List<RemoteClient> getClients() {
        return clients;
    }

    void run() {
        try {
            while (!listener.isClosed()) {
                Socket socket = listener.accept();
                RemoteClient client = new RemoteClient(new NetworkChannel(socket), this);
                clients.add(client);
                new Thread(client::run).start();
                System.out.println("New connection from " + socket.getInetAddress().toString());
            }
        } catch (SocketException e) {
            System.out.println("Shutting down server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendExcept(AbstractMessage message, RemoteClient exclude) {
        for (RemoteClient client : clients) {
            if (client != exclude) {
                System.out.println("Sending " + message.toString() + " to " + client.getId());
                client.trySend(message);
            }
        }
    }

    RaceManager getRace() {
        return race;
    }

    void setRace(RaceManager value) {
        race = value;
    }

}
