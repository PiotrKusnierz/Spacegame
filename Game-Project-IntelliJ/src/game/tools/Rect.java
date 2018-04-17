package game.tools;

import java.io.Serializable;
/**
* Simple representation of a rectangle, with position and size.
*/
public class Rect implements Serializable {
    public double x, y, w, h;

    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rect(Point p, Size s) {
        this.x = p.x;
        this.y = p.y;
        this.w = s.w;
        this.h = s.h;
    }

    public void move(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    // Finds the y-coordinate of the rect-object. Default y value is the bottom of the object.
    public double top() {
        return this.y+this.h;
    }

    public double bottom() {
        return this.y;
    }

    public double left() {
        return this.x;
    }

    public double right() {
        return this.x+this.w;
    }

    /**
    * Method checking for collision between two objects. First if-statement checks for horizontal collision, the second for vertical
    */
    public boolean intersects(Rect other) {
        if (this.left() < other.right() && this.right() > other.left()) {
            if (this.bottom() < other.top() && this.top() > other.bottom()) {
                return true;
            }
        }
        return false;
    }

    public Point center() {
        return new Point(x + w/2, y + h/2);
    }

    // Text representation of rectangle object.
    @Override
    public String toString() {
        return String.format(
            "Rect(x=%.2f, y=%.2f, w=%.2f, h=%.2f)", x, y, w, h);
    }
}
