package neonracer.server;

import neonracer.util.BuildInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Neon Racer game server...");
        ServerSocket listener = new ServerSocket();
        listener.bind(new InetSocketAddress(InetAddress.getLocalHost(), BuildInfo.getNetworkPort()));
        GameServer server = new GameServer(listener);
        new Thread(server::run).start();

        System.out.println("Server is running. Press enter to exit...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();

        listener.close();
    }

}
