package game.tools;

/**
*  Simple class for storing object dimensions (width and height).
*/
public class Size {
    public double w, h;

    /**
     * Size constructor.
     * @param w double value of the size width.
     * @param h double value of the size height.
     */
    public Size(double w, double h) {
        this.w = w;
        this.h = h;
    }

    /**
     * Overrids Object's toString method, to represent the Size with its
     * w, h values.
     * @return String
     */
    @Override
    public String toString() {
        return String.format("Size(w=%.2f, h=%.2f)", this.w, this.h);
    }
}
