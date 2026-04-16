package demo.game.multiserver;

import java.util.concurrent.ConcurrentHashMap;
import java.io.PrintWriter;

public class GameWorld {
    // Concurrent Hashmap = thread-safe hash map
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    // Keeping track of each players output stream for BROADCASTING
    private final ConcurrentHashMap<String, PrintWriter> outputs = new ConcurrentHashMap<>();

    public void addPlayer(String name, Player player, PrintWriter out) {
        players.put(name, player);
        outputs.put(name, out);
    }

    public void removePlayer(String name) {
        players.remove(name);
        outputs.remove(name);
    }

    public Player getPlayer(String name){
        return players.get(name);
    }

    // Send message to EVERY connected player = BROADCAST
    public synchronized void broadcast(String message) {
        System.out.println("[SERVER] " + message);

        for (PrintWriter out : outputs.values()) {
            out.println(message);
        }
    }

    // Summary for all player positions
    public String getWorldState() {
        StringBuilder sb = new StringBuilder("=== PLAYERS ===\n");

        for (Player player : players.values()){
            sb.append(player.getName()).append(" is at ").append(player.getPosition()).append("\n");
        }

        return sb.toString();
    }

}
