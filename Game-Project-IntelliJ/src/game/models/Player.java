package game.models;

import game.tools.*;

public class Player {
    public Rect rect;
    public Point velocity;
    public int lives;

    public Player(double x, double y, double w, double h) {
        this.rect = new Rect(x-w/2, y, w, h);
        this.velocity = new Point(0, 0);
        this.lives = 3;
    }

    public void clampPosition(double min, double max) {
        this.rect.x = Math.min(max-rect.w, Math.max(min, this.rect.x));
    }

    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y;
    }
}
