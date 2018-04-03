package game.tools;

public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public void add(Point p) {
        this.x += p.x;
        this.y += p.y;
    }

    @Override
    public String toString() {
        return String.format("Point(x=%.2f, y=%.2f)", this.x, this.y);
    }
}
