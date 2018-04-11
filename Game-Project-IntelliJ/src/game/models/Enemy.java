package game.models;

import java.util.concurrent.ThreadLocalRandom;  // A random number generator isolated to the current thread
import game.tools.*;

/**
* Class for creating an Enemy object
*/
public class Enemy {
    public Point position;
    public Point velocity;
    public Rect rect;
    public int lives;

    public Enemy(double x, double y, double w, double h, int lives) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, ThreadLocalRandom.current().nextInt(3, 6)); // Returns an integer from and including the lowest up to, but not including, the highest
        this.lives = lives;
    }

    public Enemy(double x, double y, double r) {
        this(x, y, r, r, 1);
    }

    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y;
    }
}
