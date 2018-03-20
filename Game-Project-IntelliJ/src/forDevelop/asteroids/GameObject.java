//package forDevelop.asteroids;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public class GameObject {                      //  a concept of a game object (includong view and modell)

    private Node view;                         // Node - Base class for scene graph nodes // view - our object
    private Point2D velocity = new Point2D(0, 0);   // creating a new point in default coordinate 0,0 called velocity
                                                        // The Point2D class defines a point representing a location
                                                        // in (x,y) coordinate space.

    private boolean alive = true;      // if our object is alive

    public GameObject(Node view) {     // taking Node view as a parametr  in a constructor
        this.view = view;
    }

    public void update() {         // for changing the x and y position of our object (the view)
        view.setTranslateX(view.getTranslateX() + velocity.getX());  //Translate - This class represents an Affine object
        view.setTranslateY(view.getTranslateY() + velocity.getY()); //that translates coordinates by the specified factors.
    }

    public void setVelocity(Point2D velocity) {        // to set velocity
        this.velocity = velocity;
    }

    public Point2D getVelocity() {                      // to get velocity
        return velocity;
    }

    public Node getView() {         // to display object
        return view;
    }

    public boolean isAlive() {    // if our object is alive
        return alive;
    }

    public boolean isDead() {        // if our object is dead
        return !alive;
    }

    public void setAlive(boolean alive) {      // if our object is alive
        this.alive = alive;
    }

    public double getRotate() {         // a method build in Java for rotetnig player
        return view.getRotate();
    }

    public void rotateRight() {          // making a getRotate method to rotate right
        view.setRotate(view.getRotate() + 10);      // "+" is making it rotating right, the value decide about the speed
                                                    // of the rotation
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
        // to rotate the actual movment of our player
    }

    public void rotateLeft() {          // making a getRotate method to rotate left
        view.setRotate(view.getRotate() - 10);           // "-" is making it rotating right, the value decide about the                                                            // speed of the rotation
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
        // to rotate the actual movment of our player
    }

    public boolean isColliding(GameObject other) {       // method that is checking if sth is colliding with sth
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }          // "getBoundsInParent" - Java sin method wchich checks the bounds of the view
                // intersects - Tests if the interior of the Shape intersects the interior of a specified  area
                // !!!!by using this method, they might be problems if my view arent the size of my model!!!!!!!

}
