//package forLearning;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameApplicationTest extends Application {

    private AnimationTimer timer;      // timer

    private Pane root;

    private List<Node> cars = new ArrayList<>();   // list of nodes fo moving objects called cars
    private Node frog;                           // reference to a frog ( is going to be a simple node )

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(1280, 650);

        frog = initFrog();

        root.getChildren().add(frog);

        timer = new AnimationTimer() {       // timer
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();   // starting the process of creating enemies objects

        return root;
    }

    private Node initFrog() {           // method wich generates a frog
        Rectangle rect = new Rectangle(25, 50, Color.BLUEVIOLET);
        rect.setTranslateY(650 - 39);       // initial point of spawing a frog player
        rect.setTranslateX(650 - 39);       // spawing frog player in center

        return rect;
    }

    private Node spawnCar() {          // TIP!: I could add a few sprites and then use image view
        Circle cir = new Circle(20,20,20, Color.BROWN);
        cir.setTranslateX((int)(Math.random() * 30) * 40);  //values decides on position and range of created
        // enemies objects
        root.getChildren().add(cir);     // adding rectangle objects to the root node
        return cir;
    }

    private void onUpdate() {    // updates all game objects that we have, this method is executed every 0.016s
        for (Node car : cars)
            car.setTranslateY(car.getTranslateY() + Math.random() * 5);   // to move the enemies form left to right
        // value decides about the speed
        // !!!!! if I change it to "Y" than it will be from up to down
        if (Math.random() < 0.075) {     // value decides about the amount and the space between enemies
            cars.add(spawnCar());      // ads cars list so its going to be updated
        }

        checkState();
    }

    private void checkState() {       // checking if the frog is colliding with enemy
        for (Node car : cars) {        // for all the cars
            if (car.getBoundsInParent().intersects(frog.getBoundsInParent())) {
                frog.setTranslateX(0);        // reset the frog
                frog.setTranslateY(650 - 39);   // set it to  initial spawn position
                frog.setTranslateX(650 - 39);
                return;
            }
        }
        // if the frog hasn't been hit be the enemy than
        if (frog.getTranslateY() <= 0) {         // if the frog reched the top end than
            timer.stop();          // to stop updating things
            String win = "YOU WIN";

            //we need layout for the letters, so we use
            HBox hBox = new HBox();
            hBox.setTranslateX(530);    // by chenging these values I change the positioning of "YOU WIN" text
            hBox.setTranslateY(250);
            root.getChildren().add(hBox);      //adds the box to root node

            /*The java string toCharArray() method converts this string into character array. It returns a newly created character array, its length is similar to this string and its contents are initialized with the characters of this string.*/
            for (int i = 0; i < win.toCharArray().length; i++) {
                char letter = win.charAt(i); //The method charAt(int index) returns the character at the specified index.

                Text text = new Text(String.valueOf(letter)); //create an "IUY" ?? object and convert the letter into it
                text.setFont(Font.font(48));
                text.setFill(Color.WHITE);    // seting colour of the subtitles
                text.setOpacity(0);  //making sure that animation its not visible ????(opacity - nieprzezroczystość)
                // when the value is 1 the animation is not visible

                hBox.getChildren().add(text);    // horisontal box

                // making an animation of the "YOU WIN" text
                /*Class FadeTransition creates a fade effect animation that spans its duration. This is done by updating the opacity variable of the node at regular interval.*/
                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);    // value 1 makes the txt 100 % visible, eg. value 0.5 makes it less visible
                ft.setDelay(Duration.seconds(i * 0.15));   // tempo of animation
                ft.play(); // animation play
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(), Color.BLACK));

        stage.getScene().setOnKeyPressed(event -> {     //input
            switch (event.getCode()) {
                case W:
                    frog.setTranslateY(frog.getTranslateY() - 40);
                    break;
                case S:
                    frog.setTranslateY(frog.getTranslateY() + 40);
                    break;
                case A:
                    frog.setTranslateX(frog.getTranslateX() - 40);
                    break;
                case D:
                    frog.setTranslateX(frog.getTranslateX() + 40);
                    break;
                default:
                    break;
            }
        });

        stage.show();
        stage.setTitle("Awsome Asteroid Game yo!");
    }

    public static void main(String[] args) { // we need to have it if we are not usnig an IDE that doesn't see a
        launch(args);                        // a javaFx application as a entry point
    }
}

// TIPS!:
// - play around with difficulty, add more enemies, higher speed of them at higher levels
// - lives of player which will get reduced after colliding with enemies
// - txt "game over" if you lose all of your lives, and a question "would you like to start a new game?"
// - some element in the middle of the screen like "river" at the higher levels
