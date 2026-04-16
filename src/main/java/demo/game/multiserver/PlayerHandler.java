package demo.game.multiserver;

import java.io.*;
import java.net.Socket;

public class PlayerHandler implements Runnable {

    private final Socket socket;
    private final GameWorld world;
    private String playerName;

    public PlayerHandler(Socket socket, GameWorld world) {
        this.socket = socket;
        this.world = world;
    }

    @Override
    public void run() {
        // try-with-resources closes streams automatically when done
        try (
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        ) {
            // 1. Ask for name
            output.println("Welcome! Enter your name: ");
            playerName = input.readLine();

            if (playerName == null || playerName.isBlank()) return;

            // 2. Create the player and register in the world
            Player player = new Player(playerName);
            world.addPlayer(playerName, player, output);
            world.broadcast(playerName + " joined at " + player.getPosition());

            output.println("Commands: north, south, east, west, look, quit");

            // 3. Main command loop - runs until player disconnects
            String command;
            while ((command = input.readLine()) != null) {
                handleCommand(command.trim().toLowerCase(), player, output);
            }
        } catch (IOException e) {
            System.out.println(playerName + " lost connection.");
        } finally {
            // 4. Clean up when player leaves
            if (playerName != null) {
                world.removePlayer(playerName);
                world.broadcast(playerName + " left the game.");
            }
            try { socket.close();} catch (IOException ignored) {}
        }
    }
}
