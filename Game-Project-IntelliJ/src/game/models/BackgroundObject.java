package game.models;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import game.tools.*;

public class BackgroundObject implements Serializable {
    public Point velocity;
    public Rect rect;
    public double boost = 1;
    public int type;

    public BackgroundObject(double x, double y, double w, double h, double boost) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, -ThreadLocalRandom.current().nextDouble(0.32, 1));
        this.boost = boost;
    }

    public BackgroundObject(double x, double y, double w, double h) {
        this(x, y, w, h, 1);
    }

    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y * this.boost;
    }
}