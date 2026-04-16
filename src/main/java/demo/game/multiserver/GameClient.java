package demo.game.multiserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) throws IOException {

        // Connect to the server running on this machine
        Socket socket = new Socket("localhost", 8080);
        System.out.println("Connected! Type commands and press Enter.");

        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

        // Background thread: prints anything the server sends
        Thread listener = new Thread(() -> {
            try {
                String line;
                while ((line = fromServer.readLine()) != null) {
                    System.out.println("Server> " + line);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from the server.");
            }
        });
        listener.setDaemon(true);
        listener.start();

        // Main thread: sends keyboard input to the server
        Scanner keyboard = new Scanner(System.in);
        while (keyboard.hasNextLine()) {
            toServer.println(keyboard.nextLine());
        }
        // keyboard.close();
        socket.close();
    }
}
