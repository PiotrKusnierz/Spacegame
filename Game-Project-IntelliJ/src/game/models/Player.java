package game.models;

import java.io.Serializable;
import game.tools.*;
/**
* Class for creating a Player object
*/
public class Player implements Serializable {
    public Rect rect;
    public Point velocity;
    public int lives;

    public Player(double x, double y, double w, double h) {
        this.rect = new Rect(x-w/2, y, w, h);
        this.velocity = new Point(0, 0);
        this.lives = 3;
    }

    // Method to prevent the player object from going out-of-bounds (out of the game-window)
    public void clampPosition(double min, double max) {
        this.rect.x = Math.min(max-rect.w, Math.max(min, this.rect.x));
    }

    // Everytime update() is called on, the player object is moved as specified by current velocity
    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y;
    }
}
