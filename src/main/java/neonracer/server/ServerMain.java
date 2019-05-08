package neonracer.server;

import neonracer.util.BuildInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Neon Racer game server...");
        ServerSocket listener = new ServerSocket(BuildInfo.getNetworkPort());
        GameServer server = new GameServer(listener);
        new Thread(server::run).start();

        System.out.println("Server is running. Press enter to exit...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();

        listener.close();
    }

}
