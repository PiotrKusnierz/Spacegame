package game;

import java.io.*;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import game.models.*;
import game.views.*;
import game.tools.*;

import javafx.scene.Node;

/**                                                               _
 * Masterclass. This is where the magic happens \_( *   )( *   )_/
 *
 */

public class GameController extends Application {
    private AnimationTimer gameLoop;
    private GameView gameView;
    private MenuView menuView;
    private MessageView messageView;
    private Game game;
    private List<Enemy> removedEnemies;
    private List<Point> removedBullets;
	private boolean isShooting = false;

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
    public Size windowSize = new Size(482.0, 581.0);


    // Method that runs the intersects-method from tools.Rect, making colliding objects lose a life.
    public void collisionHandler(Enemy enemy) {
        if (game.player.rect.intersects(enemy.rect)) {
            enemy.lives--;
            game.player.lives--; // [P]
            gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives)); // [P]
        }
    }


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
		game.countFrames();
		if (game.frameCounter % 3600 == 0) {
			game.level++;
			game.frameCounter = 0;
			messageView.showAnimatedMessage(String.format("Level %d", game.level));
		} else if (game.frameCounter % 100 == 0) {
			// There should be a better solution to remove the message.
			messageView.removeMessage();
		}
		if (game.frameCounter % 10 == 0 && this.isShooting) {
			game.player.shoot();
		}
        game.player.update();
        game.player.clampPosition(0, windowSize.w);
        if (game.player.lives <= 0) {
            gameState = GAMEOVER;
            messageView.showPeristantAnimatedMessage("GAME OVER");
            //messageView.showAnimatedMessage("GAME OVER");
            //messageView.removeMessage();

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
                    game.score++;                                                      // [P]
                    gameView.score.setText("SCORE: " + Integer.toString(game.score));  // [P]
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
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.05, windowSize.w*0.1);
        double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
        double y = windowSize.h+r;
        Enemy enemy = new Enemy(x, y, r, boost);
        game.enemies.add(enemy);
    }

    // Resets all the game conditions
    public void newGame() {
        game.player.rect.x = windowSize.w/2-game.player.rect.w/2;
        game.player.lives = 3;
        game.score = 0;
        game.enemies.clear();
        game.player.bullets.clear();
        removedEnemies.clear();
        removedBullets.clear();
        messageView.removeMessage();
        gameState = PLAYING;
        // [P] Sets Lives, Score counter and converts Integer to String
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));  // [P]
        gameView.score.setText("SCORE: " + Integer.toString(game.score));         // [P]
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
                    break;
                case S:
                    saveGame();
                    break;
                case L:
                    if (gameState == PAUSED || gameState == GAMEOVER) {
                        gameState = PAUSED;
                        loadGame();
                        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives)); // [P]
                        gameView.score.setText("SCORE: " + Integer.toString(game.score));        // [P]
                        messageView.removeMessage();
                        messageView.showAnimatedMessage("LOADED");
                    }
                    break;
                case ESCAPE:                                                                 // [P]
                    if (gameState == GAMEOVER) {                                             // [P]
                        menuView.resumeButton.setVisible(false);                             // [P]
                        menuView.saveButton.setVisible(false);
                    }
                    messageView.removeMessage();                                             // [P]
                    gameState = gameState == PLAYING ? PAUSED : PLAYING;

                    //System.out.println(menuView);
                    menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());  // [P]
                    break;
            }
            if (event.getCode() == KeyCode.UP) {
                boost = 3;
                for (Enemy enemy : game.enemies) {
                    enemy.boost = boost;
                }
            }
            if (event.getCode() == KeyCode.SPACE) {
                // game.player.shoot();
				this.isShooting = true;
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
			if (event.getCode() == KeyCode.SPACE) {
				this.isShooting = false;
			}
        });
    }

    // [P] Starts the game on onMouseClicked event when "Start Game" options from the menu is chosen
    public void startGame(MouseEvent mouseEvent) throws Exception{                             // [P]
        Pane root = FXMLLoader.load(this.getClass().getResource("GameInterface.fxml"));  // [P]
        Stage game_stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();     // [P]
        game_stage.setScene(new Scene(root, Color.BLACK));

        gameState = PLAYING;
        menuView = new MenuView(root);
        gameView = new GameView(windowSize, root);
        messageView = new MessageView(root);

		game = new Game();
        game.player = new Player(windowSize.w/2, windowSize.h*0.2, windowSize.w*0.12, windowSize.w*0.12);

        game.enemies = new ArrayList<Enemy>();
        removedEnemies = new ArrayList<Enemy>();
        removedBullets = new ArrayList<Point>();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };

        addEventHandler(game_stage.getScene());
        gameLoop.start();

        game_stage.show();
        game_stage.setTitle("SPACEGAME");
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives)); // [P]
        gameView.score.setText("SCORE: " + Integer.toString(game.score));        // [P]




        // [S] [P] Associates clicked button to the right method - resumeGame
		menuView.resumeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resumeGame(event);
			}
		});

        // [P] Associates clicked button to the right method - menuGame
        menuView.menuButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // [P]
            @Override                                                                                 // [P]
            public void handle(MouseEvent event) {                                                    // [P]
                menuView.pausedMenuBox.setVisible(false);                                             // [P]
                menuView.messageBox.setVisible(true);                                                 // [P]
            }
        });

        // [P] If "No" button is pressed - sends user back to the paused menu
        menuView.noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {   // [P]
            @Override                                                                                 // [P]
            public void handle(MouseEvent event) {                                                    // [P]
                menuView.pausedMenuBox.setVisible(true);                                              // [P]
                menuView.messageBox.setVisible(false);                                                // [P]
            }
        });

		// [S] [P] Associates clicked button to the right method - saveGame
        menuView.saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                saveGame();
                menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
                gameState = PLAYING;
                messageView.showAnimatedMessage("SAVED");
            }
        });

        // [S] [P] Associates clicked button to the right method - loadGame
        menuView.loadButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // [P]
            @Override                                                                                 // [P]
            public void handle(MouseEvent event) {                                                    // [P]
                File f = new File("./game.sav");                                             // [P]
                if(f.exists()) {                                                                      // [P]
                    loadGame();                                                                       // [P]
                    gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));          // [P]
                    gameView.score.setText("SCORE: " + Integer.toString(game.score));                 // [P]
                    menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());           // [P]
                    messageView.removeMessage();                                                      // [P]
                    gameState = PLAYING;                                                              // [P]
                    messageView.showAnimatedMessage("LOADED");                                   // [P]
                    menuView.resumeButton.setVisible(true);                                           // [P]
                    menuView.saveButton.setVisible(true);                                             // [P]
                } else {                                                                              // [P]
                    if (gameState == GAMEOVER) {                                                      // [P]
                        gameState = GAMEOVER;                                                         // [P]
                        System.out.println("File with saves don't exist!");                       // [P]
                    } else {                                                                          // [P]
                        messageView.showAnimatedMessage("ERROR");                                 // [P]
                        resumeGame(event);                                                            // [P]
                        System.out.println("File with saves don't exist!!");                       // [P]
                    }
                }
            }
        });

        // [P] Associates clicked button to the right method - restartGame
        menuView.restartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                restartGame(event);
            }
        });

        // [S] [P] Associates clicked button to the right method  - exitGame
        menuView.exitButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exitGame(event);
            }
        });
    }

    // [P] Restarts the game
    private void restartGame(MouseEvent event) {                                 // [P]
        newGame();                                                               // [P]
        menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());  // [P]
        messageView.showAnimatedMessage("NEW GAME");                         // [P]
        menuView.resumeButton.setVisible(true);                                  // [P]
        menuView.saveButton.setVisible(true);                                    // [P]
    }

    // [P] WIP
    public void goToOptions(MouseEvent mouseEvent) {  // [P]
    }

    // [P] Closes game
    public void exitGame(MouseEvent mouseEvent) {     // [P]
        System.exit(0);                         // [P]
    }

    // [P] Resumes the game
    public void resumeGame(MouseEvent mouseEvent) {                             // [P]
        menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible()); // [P]
		gameState = PLAYING;                                                    // [P]
    }

    // [P] Continues the game from the moment when it was ended when last time playing
    public void continueGame(MouseEvent mouseEvent) throws Exception{              // [P]
        startGame(mouseEvent);                                                     // [P]
        loadGame();                                                                // [P]
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));   // [P]
        gameView.score.setText("SCORE: " + Integer.toString(game.score));          // [P]
        messageView.showAnimatedMessage("LOADED");                             // [P]

    }




    @Override
    public void start(Stage stage) throws Exception{

        Pane root = FXMLLoader.load(this.getClass().getResource("MenuInterface.fxml")); // [P]

        menuView = new MenuView(root);
        System.out.println(menuView);
        stage.setScene(new Scene(root, Color.YELLOW));
        stage.show();
        stage.setTitle("SPACEGAME");

        // [P]  Checks if the game savings exists from before. If that is the case, than it enables
        //      continuing the game from the previous state from the Main Menu.
        File f = new File("./game.sav");        // [P]
        if(f.exists()) {                                 // [P]
            menuView.continueButton.setVisible(true);    // [P]
        } else {                                         // [P]
            menuView.continueButton.setVisible(false);   // [P]
        }
    }
}














