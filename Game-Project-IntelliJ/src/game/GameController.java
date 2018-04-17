package game;

//import java.io.IOExeption;
import java.io.FileNotFoundException;
import java.io.File;
//import java.io.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import game.models.*;
import game.views.*;
import game.tools.*;

/**                                                               _
* Masterclass. This is where the magic happens \_( *   )( *   )_/
*
*/
public class GameController extends Application {
    private Pane root;
    private AnimationTimer gameLoop;
    private GameView gameView;
    private MessageView messageView;
    private Player player;
    private List<Enemy> enemies;
    private List<Enemy> removedEnemies;

    private int gameState;
    private final int PAUSED = 1;
    private final int PLAYING = 2;
    private final int GAMEOVER = 3;

    // Defines the screenSize variable based on the user's screen size
    public Size screenSize = new Size(
        Screen.getPrimary().getVisualBounds().getWidth(),
        Screen.getPrimary().getVisualBounds().getHeight()
    );

    // Defines the window size we will use for the game
    public Size windowSize = new Size(screenSize.h*0.75, screenSize.h*0.9);

    // Method that runs the intersects-method from tools.Rect, making colliding objects lose a life.
    public void collisionHandler(Enemy enemy) {
        if (player.rect.intersects(enemy.rect)) {
            enemy.lives--;
            player.lives--;
        }
    }

    // WIP
    public void saveGame() {

    }

    // WIP
    public void loadGame() {

    }

    // Runs every frame as it is called on in the gameloop/AnimationTimer
    // Does nothing if the game is not running, i.e it's paused or game over.
    public void update() {
        if (gameState != PLAYING) {
            return;
        }
        player.update();
        player.clampPosition(0, windowSize.w);
        if (player.lives <= 0) {
            gameState = GAMEOVER;
            messageView.showAnimatedMessage("GAME OVER");
            return;
        }
        for (Enemy enemy : enemies) {
            enemy.update();
            collisionHandler(enemy);
            if (enemy.lives == 0 || enemy.rect.y > windowSize.h) {
                removedEnemies.add(enemy);
            }
        }
        // Generates enemies
        if (Math.random() < 0.05) {
            addEnemy();
        }
        // Uses the removeAll method from ArrayList to remove dead/inactive enemies from the enemies list
        enemies.removeAll(removedEnemies);
        removedEnemies.clear();
    }

    // Runs every frame as it is called on in the gameloop/AnimationTimer
    // Updates the visuals of the game
    public void draw() {
        gameView.clearCanvas();
        gameView.playerView.draw(player);
        gameView.enemyView.draw(enemies);
    }

    // Creates a new Enemy object with a random size and position
    public void addEnemy() {
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.01, windowSize.w*0.1);
        double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
        double y = -r;
        Enemy enemy = new Enemy(x, y, r);
        enemies.add(enemy);
    }

    // Resets all the game conditions
    public void newGame() {
        player.rect.x = windowSize.w/2-player.rect.w/2;
        player.lives = 3;
        enemies.clear();
        removedEnemies.clear();
        messageView.removeMessage();
        gameState = PLAYING;
    }

    // Recognizes user input and acts accordingly
    public void addEventHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.velocity.x = -6;
                    break;
                case RIGHT:
                    player.velocity.x = 6;
                    break;
                case R:
                    if (gameState == GAMEOVER) {
                        newGame();
                    }
                    break;
                case P:
                    if (gameState == GAMEOVER) {
                        break;
                    }
                    gameState = gameState == PLAYING ? PAUSED : PLAYING;

                    if (gameState == PAUSED) {
                        messageView.showAnimatedMessage("PAUSED");
                    } else {
                        messageView.removeMessage();
                    }
                // Trying to give a "boost" functionality by doubling the speed of enemies when key is pressed
                // PROBLEM: Fungerer, MEN speed ser ut til å maxe ut på under 2x. Gjør ingen forskjell å gange med mer.
                // Er også ikke smooth: burde funke samtidig som man kan svinge f.eks
                case UP:
                    for (Enemy enemy : enemies) {
                        enemy.velocity.x = enemy.velocity.x * 20;
                        enemy.update();
                    }
                    break;
            }
        });

        // To make sure the player does not continue moving after the key is released
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.velocity.x = 0;
                    break;
                case RIGHT:
                    player.velocity.x = 0;
                    break;
                case UP:
                    for (Enemy enemy : enemies) {
                        enemy.velocity.x = enemy.velocity.x / 20;
                        enemy.update();
                    }
                    break;
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception{
        Pane root = (Pane) FXMLLoader.load(this.getClass().getResource("UserInterface.fxml"));
        stage.setScene(new Scene(root, Color.BLACK));
        stage.show();

        /*
        root = new Pane();
        root.setPrefSize(windowSize.w, windowSize.h);
        stage.setScene(new Scene(root, Color.BLACK));
        gameState = PLAYING;

        gameView = new GameView(windowSize);
        messageView = new MessageView(root);
        player = new Player(windowSize.w/2, windowSize.h*0.8, windowSize.w*0.05, windowSize.w*0.08);
        enemies = new ArrayList<Enemy>();
        removedEnemies = new ArrayList<Enemy>();
        root.getChildren().add(gameView.getCanvas());

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };

        addEventHandler(stage.getScene());
        gameLoop.start();

        stage.show();
        stage.setTitle("SPACESHIT");
        */

    }
}
