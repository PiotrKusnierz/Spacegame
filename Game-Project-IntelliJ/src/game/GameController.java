package game;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import game.models.*;
import game.views.*;
import game.tools.*;


public class GameController extends Application {
    private Pane root;
    private AnimationTimer gameLoop;
    private GameView gameView;
    private MessageView messageView;
    private Player player;
    private List<Enemy> enemies;
    private List<Enemy> removedEnemies;
    private boolean paused;

    public Size screenSize = new Size(
        Screen.getPrimary().getVisualBounds().getWidth(),
        Screen.getPrimary().getVisualBounds().getHeight()-10
    );

    public Size windowSize = new Size(screenSize.h*0.75, screenSize.h*0.9);

    public void collisionHandler(Enemy enemy) {
        if (player.rect.intersects(enemy.rect)) {
            enemy.lives--;
            player.lives--;
        }
    }

    public void update() {
        if (paused) {
            return;
        }
        player.update();
        player.clampPosition(0, windowSize.w);
        if (player.lives <= 0) {
            gameLoop.stop();
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

        if (Math.random() < 0.05) {
            addEnemy();
        }
        enemies.removeAll(removedEnemies);
        removedEnemies.clear();
    }

    public void draw() {
        gameView.draw();
        gameView.playerView.draw(player);
        gameView.enemyView.draw(enemies);
    }

    public void addEnemy() {
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.01, windowSize.w*0.1);
        double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
        Enemy enemy = new Enemy(x, -r, r);
        enemies.add(enemy);
    }

    public void addEventHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.velocity.x = -6;
                    break;
                case RIGHT:
                    player.velocity.x = 6;
                    break;
                case P:
                    paused = paused ? false : true;
                    if (paused) {
                        messageView.showAnimatedMessage("PAUSED");
                    } else {
                        messageView.removeMessage();
                    }
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.velocity.x = 0;
                    break;
                case RIGHT:
                    player.velocity.x = 0;
                    break;
            }
        });
    }

    @Override
    public void start(Stage stage) {
        root = new Pane();
        root.setPrefSize(windowSize.w, windowSize.h);
        stage.setScene(new Scene(root, Color.BLACK));
        paused = false;

        gameView = new GameView(windowSize);
        messageView = new MessageView(root);
        player = new Player(windowSize.w/2, windowSize.h*0.8, windowSize.w*0.1, windowSize.w*0.1);
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

    }
}
