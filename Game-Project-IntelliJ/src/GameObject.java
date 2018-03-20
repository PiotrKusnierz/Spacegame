import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameObject {

    private Node view;
    private Point2D velocity = new Point2D(0, 0);
    public int lives;

    public GameObject(){

    }

    public GameObject(Node view, int lives) {
        // super(image, size, lives);
		this.view = view;
        this.lives = lives;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public void setVelocity(Point2D newVelocity) {
        this.velocity = newVelocity;
    }

    public Node getView() {
        return view;
    }

    public boolean isAlive() {
        return lives > 0 ? true : false;
    }

    public void setX(double x) {
        view.setTranslateX(x);
    }

    public void setY(double y) {
        view.setTranslateY(y);
    }

    public double getX() {
        return view.getTranslateX();
    }

    public double getY() {
        return view.getTranslateY();
    }

    // private Node initPlayer() {
	// 	Rectangle rect = new Rectangle(sizeX/40, sizeY/40, Color.MAGENTA);
	// 	resetPlayerPosition(rect);
	// 	return rect;
	// }


}
