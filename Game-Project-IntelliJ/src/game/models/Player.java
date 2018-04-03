package game.models;

import game.tools.*;

public class Player {
    public Rect rect;
    public Point velocity;

    public Player(double x, double y, double w, double h) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, 0);
    }

    public void clampPosition(double min, double max) {
        this.rect.x = Math.min(max, Math.max(min, this.rect.x));
    }

    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y;
    }
}
