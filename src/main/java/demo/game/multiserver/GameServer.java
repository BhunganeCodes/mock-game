package demo.game.multiserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        // One shared world - all threads share this object
        GameWorld world = new GameWorld();

        // Open the server's front door on port 8080
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Game server started on port " + PORT);
        System.out.println("Waiting for players...");

        // This loop runs forever - the server never stops running on its own
        while (true) {

            // accept() BLOCKS here until a player connects
            Socket playerSocket = serverSocket.accept();
            System.out.println("New connection from: " + playerSocket.getInetAddress());

            // Hand the socket to a new PlayerHandler thread
            // The main thread immediately loops back to accept()
            Thread playerThread = new Thread(new PlayerHandler(playerSocket, world));
            playerThread.setDaemon(true);
            playerThread.start();
        }
    }
}
