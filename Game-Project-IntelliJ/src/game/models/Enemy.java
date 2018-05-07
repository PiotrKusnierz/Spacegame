package game.models;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import game.tools.*;

/**
 * Class for creating an Enemy object.
 */
public class Enemy implements Serializable {
    public Point velocity;
    public Rect rect;
    public int lives;
    public double boost = 1;
    public double step = 0;
    public int type;

    /**
     * Enemy constructor which creates an Enemy object with the given position
     * size, and lives.
     * @param x double value of the enemy's x-position.
     * @param y double value of the enemy's y-position.
     * @param w double value of the enemy's width.
     * @param h double value of the enemy's height.
     * @param lives int value of the enemy's lives.
     * @param boost double value of the enemy's boost value (when the player presses the up-arrow).
     */
    public Enemy(double x, double y, double w, double h, int lives, double boost) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, -ThreadLocalRandom.current().nextInt(3, 6));
        this.lives = lives;
        this.boost = boost;
    }

    /**
    * Enemy constructor which creates an Enemy object with the given position,
    * radius and boost.
    * @param x double value of the enemy's x-position.
    * @param y double value of the enemy's y-position.
    * @param r double value of the enemy's radius.
    * @param boost double value of the enemy's boost value (when the player presses the up-arrow).
    */
    public Enemy(double x, double y, double r, double boost) {
        this(x, y, r, r, 1, boost);
    }

    /**
     * Enemy constructor which creates an Enemy object with the given position
     * and radius.
     * @param x double value of the enemy's x-position.
     * @param y double value of the enemy's y-position.
     * @param r double value of the enemy's radius.
     */
    public Enemy(double x, double y, double r) {
        this(x, y, r, r, 1, 1);
    }

    /**
     * Makes the enemy move in a sinus curve.
     * @author Sebastian Jarsve
     */
    public void moveSin() {
        this.velocity.x = Math.sin(this.step)*6;
    }

    /**
     * Makes the enemy move in a clockwise circlular path.
     * @author Sebastian Jarsve
     */
    public void moveCircClockwise() {
        this.velocity.x = Math.sin(this.step)*6;
        this.rect.y -= -Math.cos(this.step)*6;
    }

    /**
     * Makes the enemy move in a counter clockwise circlular path.
     * @author Sebastian Jarsve
     */
    public void moveCircCounterClockwise() {
        this.velocity.x = -Math.sin(this.step)*6;
        this.rect.y -= -Math.cos(this.step)*6;
    }

    /**
     * Updates the enemy's position based on its velocity.
     * Also adds different movements according to its type.
     */
    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y * this.boost;
        this.step = (this.step + 0.1) % (2*Math.PI);
        switch (this.type) {
            case 3: moveSin(); break;
            case 4: moveSin(); break;
            case 5: moveCircClockwise(); break;
            case 6: moveCircCounterClockwise(); break;
            case 8: moveSin(); break;                                        // S P
        }
    }
}
