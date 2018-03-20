//package forDevelop.asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AsteroidsApp extends Application {

    private Pane root;         // making a root Node (węzeł główny ??)   of a type Pane
                               // Pane - Base class for layout panes which need to expose the children list
                               // as public so that users of the subclass can freely add/remove children.
                               // Roote node stores our grafics (grafics objects)

    private List<GameObject> bullets = new ArrayList<>();    // a list of game objects called bullets
    private List<GameObject> enemies = new ArrayList<>();    // a list of game objects called enemies
                                            //List - (Interface) An ordered collection (also known as a sequence).
                                            //ArrayList - Resizable-array implementation of the List interface.

    private GameObject player;               //player object

    private Parent makeContent() {             // The base class for all nodes that have children in the scene graph
        root = new Pane();                     // creating a new Pane of a root Type
        root.setPrefSize(1280, 650);  // root is calling for a method build in Java for seting its size
                                                       // (the size of the window it this case)
        player = new Player();  // creating our player
        player.setVelocity(new Point2D(1,0));  //setting the initial speed and direction(right after you start game
                                                    // (when the values er 0,0 the object is staying in the same place
                                                    // until we prese LEFT or RIGHT button)

        addGameObject(player, 640, 600);   //the place where we add our player at the beginning of the game start
                                                    //(the location of its start)

        AnimationTimer timer  = new AnimationTimer() {       // Timer which drive our updates , called also as
            @Override                                       // the game loope (main loope of the game)!!!!!
            public void handle(long now) {               // handel ???
                onUpdate();
            }                                      // called back is called every 1/60s
        };
        timer.start();                            // this method starts timer

        return root;         // to display the root
    }

    private void addBullet(GameObject bullet, double x, double y) { // adding bullet  to the root ???
        bullets.add(bullet);
        addGameObject(bullet, x, y);
    }

    private void addEnemy(GameObject enemy, double x, double y) {    // adding enemy  to the root ???
        enemies.add(enemy);
        addGameObject(enemy, x, y);
    }

    private void addGameObject(GameObject object, double x, double y) {   // adding view(our game object to the root)
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void onUpdate() {   //to check if bullets are coliding with enemies
        for (GameObject bullet : bullets) {    // for each bullet
            for (GameObject enemy : enemies) {    // go for each enemy and check if we colide
                if (bullet.isColliding(enemy)) {    //if yes
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    root.getChildren().removeAll(bullet.getView(), enemy.getView()); // updating the view to remove the                                             bullets and the enemy objects  from the view when a bullet hits enemy
                    /*The removeAll() method is used to remove all the elements from a list that are contained in the                           specified collection.*/
                }
            }
        }

        bullets.removeIf(GameObject::isDead);   // remove all the dead bullets from the model
                                                //("removeIf()" Java sin) - It removes all the elements from the List                                                    // which satisfies the given Predicate.
        enemies.removeIf(GameObject::isDead);   // remove all the dead enemies from the model

                                                // after cleaning/removing we should update each object
        bullets.forEach(GameObject::update);    // updating bullets
        enemies.forEach(GameObject::update);    // updating enemies

        player.update();                        //updating player

        if (Math.random() <0.02) {         // creates enemies at random //Math - Java sin class
            //	random()
            //Returns a double value with a positive sign, greater than or equal to 0.0 and less than 1.0.
            // the higher values, the faster enemies will be created
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
            // to create the enemies at the random location
            //	prefWidth(double height)
            //Returns the node's preferred width for use in layout calculations.
            //	prefHeight(double width)
            //Returns the node's preferred height for use in layout calculations.
        }
    }

    private static class Player extends GameObject {    //  we extend (by inheritence) our game objects
        Player() {    //player will not be public       //  so that we can assign views
            super(new Rectangle(50, 25, Color.BLUEVIOLET));   //Rectangle - Java sin class
        }
    }

    private static class Enemy extends GameObject {
        Enemy() {
            super(new Circle(15,15,15, Color.BROWN));      //Circle - Java sin class
        }
    }

    private static class Bullet extends GameObject {
        Bullet() {
            super(new Circle(5,5,5, Color.GREEN));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(makeContent(),Color.BLACK));  //seting scene , calling for a Parent class "makeContent"
        stage.getScene().setOnKeyPressed(e -> {      //for controling the player  ,    e -> (?????)
                                                     /*setOnKeyPressed(EventHandler<? super KeyEvent> value)
                                                            Sets the value of the property onKeyPressed.*/
            if (e.getCode() == KeyCode.LEFT) {        /*	getCode() - The key code associated
                                                      with the key in this key pressed or key released event.*/
                player.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.rotateRight();
            } else if (e.getCode() == KeyCode.SPACE) {
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(10)); // to update bullets position once the                                                   //are created tworwards player, value sets the speed of the bullet
                                                    // normalize ????
                addBullet(bullet, player.getView().getTranslateX() + 25, player.getView().getTranslateY());
                // adding the bullet to the game world, in() are creation position of the bullets
            }
        });
        stage.show();                    // calling for stage
    }

    public static void main(String[] args) {
        launch(args);
    }


}

// Tips:
// - I may think about a method that wil remove the bullets which went outside the screen, cause I don't want to hav
// many of them hanging around!!!!!!!!!!!!
// - Should also add colision between the player and the enemy
// - should keep the player on the screen
