package game;

import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
// import java.io.Scanner;

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
import javafx.scene.input.KeyCode;

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
	private Game game;
    private List<Enemy> removedEnemies;
    private List<Point> removedBullets;

    private int gameState;
    private final int PAUSED = 1;
    private final int PLAYING = 2;
    private final int GAMEOVER = 3;

	public double boost = 1;
    // Defines the screenSize variable based on the user's screen size
    public Size screenSize = new Size(
        Screen.getPrimary().getVisualBounds().getWidth(),
        Screen.getPrimary().getVisualBounds().getHeight()
    );

    // Defines the window size we will use for the game
    // public Size windowSize = new Size(screenSize.h*0.75, screenSize.h*0.9);
    public Size windowSize = new Size(320, 480);

    // Method that runs the intersects-method from tools.Rect, making colliding objects lose a life.
    public void collisionHandler(Enemy enemy) {
        if (game.player.rect.intersects(enemy.rect)) {
            enemy.lives--;
            game.player.lives--;
        }
    }

    // WIP
    public void saveGame() {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream("./game.sav");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(game);
			if (fout != null) {
				fout.close();
			}
			if (oos != null) {
				oos.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
    }

    // WIP
    public void loadGame() {
		try {
			FileInputStream fis = new FileInputStream("./game.sav");
			ObjectInputStream ois = new ObjectInputStream(fis);
			game = (Game)ois.readObject();
			fis.close();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
    }

    // Runs every frame as it is called on in the gameloop/AnimationTimer
    // Does nothing if the game is not running, i.e it's paused or game over.
    public void update() {
        if (gameState != PLAYING) {
            return;
        }
        game.player.update();
        game.player.clampPosition(0, windowSize.w);
        if (game.player.lives <= 0) {
            gameState = GAMEOVER;
            messageView.showAnimatedMessage("GAME OVER");
            return;
        }
        for (Enemy enemy : game.enemies) {
            enemy.update();
            collisionHandler(enemy);
			for (Point bullet : game.player.bullets) {
				if (bullet.y > windowSize.h) {
					removedBullets.add(bullet);
				}
				if (enemy.rect.contains(bullet)) {
					enemy.lives--;
					removedBullets.add(bullet);
				}
			}
            if (enemy.lives == 0 || enemy.rect.y < 0) {
                removedEnemies.add(enemy);
            }
        }
        // Generates enemies
        if (Math.random() < 0.05 * boost) {
            addEnemy();
        }
        // Uses the removeAll method from ArrayList to remove dead/inactive enemies from the enemies list
        game.enemies.removeAll(removedEnemies);
        game.player.bullets.removeAll(removedBullets);
        removedEnemies.clear();
        removedBullets.clear();
    }

    // Runs every frame as it is called on in the gameloop/AnimationTimer
    // Updates the visuals of the game
    public void draw() {
        gameView.clearCanvas();
        gameView.playerView.draw(game.player);
        gameView.enemyView.draw(game.enemies);
    }

    // Creates a new Enemy object with a random size and position
    public void addEnemy() {
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.01, windowSize.w*0.1);
        double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
        double y = windowSize.h+r;
        Enemy enemy = new Enemy(x, y, r, boost);
        game.enemies.add(enemy);
    }

    // Resets all the game conditions
    public void newGame() {
        game.player.rect.x = windowSize.w/2-game.player.rect.w/2;
        game.player.lives = 3;
        game.enemies.clear();
        game.player.bullets.clear();
        removedEnemies.clear();
        removedBullets.clear();
        messageView.removeMessage();
        gameState = PLAYING;
    }

    // Recognizes user input and acts accordingly
    public void addEventHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    game.player.velocity.x = -6;
                    break;
                case RIGHT:
                    game.player.velocity.x = 6;
                    break;
                case R:
					newGame();
                    // if (gameState == GAMEOVER) {
                    // }
                    break;
				case S:
					saveGame();
					break;
				case L:
					if (gameState == PAUSED || gameState == GAMEOVER) {
						break;
					}
					gameState = PAUSED;
					loadGame();
                    messageView.showAnimatedMessage(String.format("Lives: %d", game.player.lives));
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
					break;
            }
			if (event.getCode() == KeyCode.UP) {
				boost = 3;
				for (Enemy enemy : game.enemies) {
					enemy.boost = boost;
				}
			}
			if (event.getCode() == KeyCode.SPACE) {
				game.player.shoot();
			}
        });

        // To make sure the game.player does not continue moving after the key is released
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    game.player.velocity.x = 0;
                    break;
                case RIGHT:
                    game.player.velocity.x = 0;
                    break;
            }
			if (event.getCode() == KeyCode.UP) {
				boost = 1;
				for (Enemy enemy : game.enemies) {
					enemy.boost = boost;
				}
			}
        });
    }

    @Override
    public void start(Stage stage) {
        root = new Pane();
        root.setPrefSize(windowSize.w, windowSize.h);
        stage.setScene(new Scene(root, Color.BLACK));
        gameState = PLAYING;

        gameView = new GameView(windowSize);
        messageView = new MessageView(root);
		game = new Game();
        game.player = new Player(windowSize.w/2, windowSize.h*0.2, windowSize.w*0.05, windowSize.w*0.08);
        game.enemies = new ArrayList<Enemy>();
        removedEnemies = new ArrayList<Enemy>();
        removedBullets = new ArrayList<Point>();
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
