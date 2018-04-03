package game.tools;

public class Rect {
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

    public boolean intersects(Rect other) {
        if (this.left() < other.right() && this.right() > other.left()) {
            if (this.bottom() < other.top() && this.top() > other.bottom()) {
                return true;
            }
        }
        return false;
    }

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

    public Point center() {
        return new Point(x + w/2, y + h/2);
    }

    @Override
    public String toString() {
        return String.format(
            "Rect(x=%f, y=%f, w=%f, h=%f)", x, y, w, h);
    }
}
