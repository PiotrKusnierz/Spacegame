package game.tools;

import java.io.Serializable;
/**
* Simple representation of a rectangle, with position and size.
*/
public class Rect implements Serializable {
    public double x, y, w, h;

  /**
   * Rect constructor which stores its position and size.
   * @param x double value of the rect's x-position.
   * @param y double value of the rect's y-position.
   * @param w double value of the rect's width.
   * @param h double value of the rect's height.
   */
    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Returns the maximum y-value of the Rect object.
     * @return double
     */
    public double top() {
        return this.y+this.h;
    }

    /**
     * Returns the minimum y-value of the Rect object.
     * @return double
     */
    public double bottom() {
        return this.y;
    }

    /**
     * Returns the minimum x-value of the Rect object.
     * @return double
     */
    public double left() {
        return this.x;
    }

    /**
     * Returns the maximum x-value of the Rect object.
     * @return double
     */
    public double right() {
        return this.x+this.w;
    }

    /**
     * Checking for intersection between two Rect objects.
     * @param other The other Rect object to check intersection with.
     * @return boolean
     */
    public boolean intersects(Rect other) {
        if (this.left() < other.right() && this.right() > other.left()) {
            if (this.bottom() < other.top() && this.top() > other.bottom()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given Point is in the Rect object.
     * @param p The Point to check.
     * @return boolean
     */
    public boolean contains(Point p) {
        if (p.x > this.left() && p.x < this.right()) {
            if (p.y > this.bottom() && p.y < this.top()) {
            return true;
            }
        }
        return false;
    }

    /**
     * Finds the center for Rect object.
     * @return Point
     */
    public Point center() {
        return new Point(x + w/2, y + h/2);
    }

    /**
    * Overrids Object's toString method, to represent the Rect with its
    * x, y, w, h values.
    * @return String
    */
    @Override
    public String toString() {
        return String.format(
            "Rect(x=%.2f, y=%.2f, w=%.2f, h=%.2f)", x, y, w, h
        );
    }
}
