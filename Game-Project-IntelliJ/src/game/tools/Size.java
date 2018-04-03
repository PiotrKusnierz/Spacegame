package game.tools;

public class Size {
    public double w;
    public double h;

    public Size(double w, double h) {
        this.w = w;
        this.h = h;
    }

    public Size() {
        this(0, 0);
    }

    @Override
    public String toString() {
        return String.format("Size(w=%.2f, h=%.2f)", this.w, this.h);
    }
}
