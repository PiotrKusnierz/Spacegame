package game.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import game.tools.*;
/**
* Class for creating a Player object
*/
public class Player implements Serializable {
    public Rect rect;
    public Point velocity;
    public int lives;
	public List<Point> bullets;

    public Player(double x, double y, double w, double h) {
        this.rect = new Rect(x-w/2, y, w, h);
        this.velocity = new Point(0, 0);
        this.lives = 3;
		this.bullets = new ArrayList<Point>();
    }

    // Method to prevent the player object from going out-of-bounds (out of the game-window)
    public void clampPosition(double min, double max) {
        this.rect.x = Math.min(max-rect.w, Math.max(min, this.rect.x));
    }

	public void shoot() {
		Point bullet = new Point();
		bullet.x = this.rect.center().x - 5;
		bullet.y = this.rect.top();
		this.bullets.add(bullet);
	}

    // Everytime update() is called on, the player object is moved as specified by current velocity
    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y;
		for (Point bullet : bullets) {
			bullet.y += 5;
		}
    }
}
