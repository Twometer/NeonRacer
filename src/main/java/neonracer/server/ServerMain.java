package neonracer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ServerMain {

    private static final int port = 5000;
    private static boolean running;
    private static ServerSocket listener;
    private static List<RemoteClient> clients;

    public static void main(String[] args) throws IOException {
        listener = new ServerSocket(port);
        clients = Collections.synchronizedList(new LinkedList<>());
        running = true;
        new Thread(ServerMain::listenerThread).start();
    }

    public static boolean isRunning() {
        return running;
    }

    public static List<RemoteClient> getClients() {
        return clients;
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
