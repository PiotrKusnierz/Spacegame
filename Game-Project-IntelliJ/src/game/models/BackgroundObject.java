package game.models;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import game.tools.*;

/**
 * Class for creating background objects / scenery like stars and planets.
 * @author Inge Brochmann
 */
public class BackgroundObject implements Serializable {
    public Point velocity;
    public Rect rect;
    public double boost = 1;
    public int type;

    /**
     * Constructor that creates a background object with the given position and size,
     * with a randomized velocity. Gives the illusion of being in the background by being
     * drawn before other objects and having no interaction with any other objects.
     *
     * @param x double value of the background object's x-position.
     * @param y double value of the background object's y-position.
     * @param w double value of the background object's width.
     * @param h double value of the background object's height.
     * @param boost double value of the background object's boost value
     * (when the player presses the up-arrow). Acts as a temporary modification to velocity.
     */
    public BackgroundObject(double x, double y, double w, double h, double boost) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, -ThreadLocalRandom.current().nextDouble(0.32, 1));
        this.boost = boost;
    }

    /**
     * Constructor that creates a background object with the given position and size,
     * with a randomized velocity. Gives the illusion of being in the background by being
     * drawn before other objects and having no interaction with any other objects.
     *
     * @param x double value of the background object's x-position.
     * @param y double value of the background object's y-position.
     * @param w double value of the background object's width.
     * @param h double value of the background object's height.
     */
    public BackgroundObject(double x, double y, double w, double h) {
        this(x, y, w, h, 1);
    }

    /**
     * Updates the position of the background object based on its current velocity.
     */
    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y * this.boost;
    }
}