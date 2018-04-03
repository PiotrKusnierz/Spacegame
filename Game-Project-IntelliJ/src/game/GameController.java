package game;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import game.views.GameView;
import game.tools.*;


public class GameController extends Application {
    private Pane root;
    private AnimationTimer gameLoop;
    private GameView gameView;

    public Size screenSize = new Size(
        Screen.getPrimary().getVisualBounds().getWidth(),
        Screen.getPrimary().getVisualBounds().getHeight()-10
    );

    public Size windowSize = new Size(screenSize.h*0.75, screenSize.h*0.9);

    public void update() {

    }

    public void draw() {

    }

    @Override
    public void start(Stage stage) {
        root = new Pane();
        root.setPrefSize(windowSize.w, windowSize.h);
        stage.setScene(new Scene(root, Color.BLACK));

        gameView = new GameView(windowSize);
        root.getChildren().add(gameView.getCanvas());

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };

        gameLoop.start();

        stage.show();
        stage.setTitle("SPACESHIT");

    }
}
