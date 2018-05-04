package game.models;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;  // A random number generator isolated to the current thread
import game.tools.*;

/**
* Class for creating an Enemy object
*/
public class Enemy implements Serializable {
    public Point position;
    public Point velocity;
    public Rect rect;
    public int lives;
	public double boost = 1;
	public double step = 0;
	public int type;

    public Enemy(double x, double y, double w, double h, int lives, double boost) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, -ThreadLocalRandom.current().nextInt(3, 6)); // Returns an integer from and including the lowest up to, but not including, the highest
        this.lives = lives;
		this.boost = boost;
    }

    public Enemy(double x, double y, double r, double boost) {
        this(x, y, r, r, 1, boost);
    }

    public Enemy(double x, double y, double r) {
        this(x, y, r, r, 1, 1);
    }

	public void moveSin() {
		this.velocity.x = Math.sin(this.step)*6;
	}

	public void moveCircClockwise() {
		this.velocity.x = Math.sin(this.step)*6;
		this.rect.y -= -Math.cos(this.step)*6;
	}

	public void moveCircCounterClockwise() {
		this.velocity.x = -Math.sin(this.step)*6;
		this.rect.y -= -Math.cos(this.step)*6;
	}

    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y * this.boost;
		this.step = (this.step + 0.1) % (2*Math.PI);
		switch (this.type) {
			case 3: moveSin(); break;
			case 4: moveSin(); break;
			case 5: moveCircClockwise(); break;
			case 6: moveCircCounterClockwise(); break;
		}
    }
}
