package neonracer.server;

import com.google.protobuf.AbstractMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMain {

    private static final int port = 5000;
    private static boolean running;
    private static AtomicInteger connectionCounter;
    private static ServerSocket listener;
    private static List<RemoteClient> clients;
    private static List<RemoteClient> participants;
    private static List<RemoteEntity> entities;

    public static void main(String[] args) throws IOException {
        listener = new ServerSocket(port);
        clients = Collections.synchronizedList(new LinkedList<>());
        connectionCounter = new AtomicInteger();
        participants = Collections.synchronizedList(new LinkedList<>());
        entities = Collections.synchronizedList(new LinkedList<>());
        running = true;
        new Thread(ServerMain::listenerThread).start();
    }

    static boolean isRunning() {
        return running;
    }

    static List<RemoteClient> getClients() {
        return clients;
    }

    static AtomicInteger getConnectionCounter() {
        return connectionCounter;
    }

    static List<RemoteClient> getParticipants() {
        return participants;
    }

    static List<RemoteEntity> getEntities() {
        return entities;
    }

    static void sendExcept(AbstractMessage message, RemoteClient exclude) throws IOException {
        for (RemoteClient client : clients) {
            if (client != exclude) {
                client.send(message);
            }
        }
    }

    private static void listenerThread() {
        try {
            while (running) {
                RemoteClient client = new RemoteClient(listener.accept());
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
