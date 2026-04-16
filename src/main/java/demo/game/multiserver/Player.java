package demo.game.multiserver;
import java.util.Random;

public class Player {
    private final String name;
    private int x;
    private int y;

    public Player(String name){
        this.name = name;

        // Spawn the player at a random spot
        Random random = new Random();
        this.x = random.nextInt(20);
        this.y = random.nextInt(20);
    }

    // synchronized: only ONE thread can move player at a time
    public synchronized void move(int distX, int distY) {
        // Math.max/min clamps the values between the world
        this.x = Math.clamp(this.x + distX, 0, 19);
        this.y = Math.clamp(this.y, 0, 19);
    }

    public String getName(){
        return name;
    }

    public synchronized String getPosition(){
        return "(" + x + ", " + y + ")";
    }

}
