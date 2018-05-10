package game.tools;

import java.io.Serializable;

/**
 * Class used for storing position or velocity of an object.
 */
public class Point implements Serializable {
    public double x, y;

    /**
     * Point constructor which stores its position.
     * @param x double value of the point's x-position.
     * @param y double value of the point's y-position.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a Point object with x and y defaults to 0 when no parameter is
     * given.
     */
    public Point() {
        this(0, 0);
    }

    /**
     * Adds x and y value of another Point object to the current Point object.
     * @param p Point object to add to current Point object.
     */
    public void add(Point p) {
        this.x += p.x;
        this.y += p.y;
    }

    /**
     * Overrids Object's toString method, to represent the Point with its
     * x and y values.
     * @return String
     */
    @Override
    public String toString() {
        return String.format("Point(x=%.2f, y=%.2f)", this.x, this.y);
    }
}
