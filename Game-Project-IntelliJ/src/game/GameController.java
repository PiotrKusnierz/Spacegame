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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

import static javafx.scene.media.MediaPlayer.INDEFINITE;

/**                                                               
 * This class controls the logic behind the game and calls the games draw
 * functions.
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
    private double boost = 1;

    private int gameState;
    private final int PAUSED = 1;
    private final int PLAYING = 2;
    private final int GAMEOVER = 3;
    private final int GAMEWON = 4;

    private static MediaPlayer mediaPlayer;   // [P]

    // Defines the screenSize variable based on the user's screen size
    public Size screenSize = new Size(
            Screen.getPrimary().getVisualBounds().getWidth(),
            Screen.getPrimary().getVisualBounds().getHeight()
    );

    // Defines the window size we will use for the game
    // public Size windowSize = new Size(screenSize.h*0.75, screenSize.h*0.9);
    public Size windowSize = new Size(482.0, 581.0);

    // [P] Method creates one object of type Media which downloads sound files, and one object of type
    //     MediaPlayer which plays those sound files one time.
    public void playSound(String filename) {                                              // [P]
        Media sound  = new Media(getClass().getResource(filename).toString());            // [P]
        mediaPlayer = new MediaPlayer(sound);                                             // [P]
        mediaPlayer.setVolume(0.3);                                                       // [P]
        mediaPlayer.play();                                                               // [P]
    }

    // [P] Method plays and constantly repeats music.
    public void playMusic(String filename) {                                              // [P]
        Media music  = new Media(getClass().getResource(filename).toString());            // [P]
        mediaPlayer = new MediaPlayer(music);                                             // [P]
        mediaPlayer.setCycleCount(INDEFINITE);                                            // [P]
        mediaPlayer.setVolume(0.6);                                                       // [P]
        mediaPlayer.play();                                                               // [P]
    }

	/**
	 * Saves the game.
	 */
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

	/**
	 * Loads the game.
	 */
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

	/**
	 * Handles collision detection between the enemies and the player, and
	 * removes a life from both the player and the enemy if a collision is
	 * detected.
	 * @param enemy an enemy that the player might collide with.
	 */
    public void collisionHandler(Enemy enemy) {
        if (game.player.rect.intersects(enemy.rect)) {
            playSound("sound/boom7.mp3");      // [P]
            enemy.lives--;
            game.player.lives--; // [P]
            gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));    // [P]
        }
    }

	/**
	 * Adds a value to the score and updates it on the screen.
	 * @param value what will be added to the score.
	 * @author Sebastian
	 */
	public void updateScore(int value) {
		game.score += value;
		gameView.score.setText("SCORE: " + Integer.toString(game.score));         // [P]
	}

	/**
	 * This runs every frame, and contains the logic part of the game.
	 */
    public void update() {
        if (gameState != PLAYING) {
            return;
        }
		game.countFrames();
        //playSound("sound/enginehum.mp3");    // [P]    slows down the game !!!!!!!!!!!!!!
		if (game.frameCounter % 1800 == 0 && game.level < 4) {
			game.level++;
			game.frameCounter = 0;
			if (game.level < 4) {
				messageView.showAnimatedMessage(String.format("Level %d", game.level));
			} else {
				messageView.showAnimatedMessage("Final Level");                          // S P
				game.boss = new Enemy(windowSize.w/2 - 50,800,100);                   // S P
				game.boss.type = 7;                                                          // S P
				game.boss.velocity.y = -0.5;                                                 // S P
				game.boss.lives = 50;                                                        // S P
				game.enemies.add(game.boss);                                                 // S P
			}
			playSound("sound/Upper01.mp3");               // [P]
		} else if (game.frameCounter % 100 == 0) {
			// There should be a better solution to remove the message.
			messageView.removeMessage();
		}
		if (game.level == 4) {
			if (game.boss.lives <= 0) {
				messageView.showAnimatedMessage("YOU WON!");
				gameState = GAMEWON;
			}
		}
		if (game.frameCounter % 50 == 0) {
			updateScore(1);
		}
		if (game.frameCounter % 10 == 0 && this.isShooting) {
			game.player.shoot();
			playSound("sound/laserfire01.mp3");           //  [P]
		}
        game.player.update();
        game.player.clampPosition(0, windowSize.w);
        if (game.player.lives <= 0) {
            gameState = GAMEOVER;
            playSound("sound/GAMEOVER.mp3");   //????????????????????????????????????????????????
            messageView.showPeristantAnimatedMessage("GAME OVER");
            return;
        }
        for (Enemy enemy : game.enemies) {
            enemy.update();
            collisionHandler(enemy);
            if (enemy.type == 7) {                              // S P
                if(enemy.rect.y <= 400) {                       // S P
                    enemy.velocity.y = 0;                       // S P
                }
            }
            for (Point bullet : game.player.bullets) {
                if (bullet.y > windowSize.h) {
                    removedBullets.add(bullet);
                }
                if (enemy.rect.contains(bullet)) {
                    enemy.lives--;
                    removedBullets.add(bullet);
					updateScore(10);
					playSound("sound/8bit_bomb_explosion.mp3");                  // [P]
                }
            }
            if (enemy.lives < 1 || enemy.rect.top() < 0) {
                removedEnemies.add(enemy);
            }
        }
        // Generates enemies
        if (Math.random() < 0.02 * boost) {
			switch (game.level) {
				case 1:
					addEnemy(ThreadLocalRandom.current().nextInt(0, 3));
					playSound("sound/burnfire.mp3");   // [P]
					break;
				case 2: 
					addEnemy(ThreadLocalRandom.current().nextInt(3, 5));
					playSound("sound/space2.mp3");     // [P]
					break;
				case 3: 
					addEnemy(ThreadLocalRandom.current().nextInt(5, 7));
					playSound("sound/burnfire.mp3");   // [P]
					playSound("sound/space2.mp3");     // [P]
					break;
                case 4:                                      // S P
                    addEnemyChildren();                      // S P
			}
        }

        // Uses the removeAll method from ArrayList to remove dead/inactive enemies from the enemies list
        game.enemies.removeAll(removedEnemies);
        game.player.bullets.removeAll(removedBullets);
        removedEnemies.clear();
        removedBullets.clear();
    }

	/**
	 * This runs every frame to update the game graphics.
	 */
    public void draw() {
        gameView.clearCanvas();
        gameView.playerView.draw(game.player);
        gameView.enemyView.draw(game.enemies);
    }

	/**
	 * Creates a new enemy with a random size and position and adds it to the
	 * games enemies list.
	 * @param type value for the enemy type which is sent to the Enemy
	 * constructor.
	 */
    public void addEnemy(int type) {
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.08, windowSize.w*0.13);
        double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
        double y = windowSize.h+r;
        Enemy enemy = new Enemy(x, y, r, boost);
		enemy.type = type;
        game.enemies.add(enemy);
    }

    public void addEnemyChildren() {                                       // S P
        double r = 20;                                                     // S P
        Point position = game.boss.rect.center();                          // S P
        Enemy enemy = new Enemy(position.x, position.y, r, boost);         // S P
        enemy.type = 8;                                                    // S P
        game.enemies.remove(game.boss);                                    // S P
        game.enemies.add(enemy);                                           // S P
        game.enemies.add(game.boss);                                       // S P

    }

    // Resets all the game conditions
	/**
	 * Resets all the game conditions to get ready for a new game.
	 */
    public void newGame() {
        game.player.rect.x = windowSize.w/2-game.player.rect.w/2;
        game.player.lives = 3;
        game.score = 0;
        game.enemies.clear();
        game.player.bullets.clear();
		game.frameCounter = 0;
		game.level = 1;
		game.enemies.clear();
        removedEnemies.clear();
        removedBullets.clear();
        messageView.removeMessage();
        gameState = PLAYING;
        // [P] Sets Lives, Score counter and converts Integer to String
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));           // [P]
        gameView.score.setText("SCORE: " + Integer.toString(game.score));                  // [P]
    }

	/**
	 * Adds a keyboard input event handler to the scene.
	 */
    public void addEventHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
				mediaPlayer.stop();                                                      // [P]
				if (gameState == GAMEOVER || gameState == GAMEWON) {
					menuView.resumeButton.setVisible(false);                             // [P]
					menuView.saveButton.setVisible(false);                               // [P]
				} else {
					gameState = gameState == PLAYING ? PAUSED : PLAYING;
				}
				messageView.removeMessage();                                             // [P]
				playMusic("sound/theme_menu.mp3");                                // [P]
				menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());  // [P]
			}
			if (gameState == GAMEOVER || gameState == PAUSED) {
				return;
			}
            if (event.getCode() == KeyCode.LEFT) {
				game.player.velocity.x = -6;
			}
            if (event.getCode() == KeyCode.RIGHT) {
				game.player.velocity.x = 6;
			}
            if (event.getCode() == KeyCode.UP && game.level < 4) {
                boost = 3;
                playSound("sound/warpengine.mp3");        // [P]
                for (Enemy enemy : game.enemies) {
                    enemy.boost = boost;
                }
            }
            if (event.getCode() == KeyCode.SPACE) {
                if (!this.isShooting) {
                	game.player.shoot();
                    playSound("sound/laserfire01.mp3");  //  [P]
                }
				this.isShooting = true;
            }
        });

        // To make sure the game.player does not continue moving after the key is released
        scene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
				game.player.velocity.x = 0;
			}
            if (event.getCode() == KeyCode.UP) {
                boost = 1;
                for (Enemy enemy : game.enemies) {
                    enemy.boost = boost;
                }
                mediaPlayer.stop();              // [P]
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

        mediaPlayer.stop();                                                                    // [P]
        playSound("sound/ambient_techno1.mp3");                                         // [P]
        //playSound("sound/StarCommander1.mp3");    // [P]    not working yet

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
                // playSound("sound/enginehum.mp3");    // [P]    makes sound not commplete!!!!!!!!!!!!!!
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
                mediaPlayer.stop();                                                                    // [P]
				resumeGame(event);
				playSound("sound/ambient_techno1.mp3");   // [P]
			}
		});

        // [P] If "Main Menu" button is pressed - asks question to the user if he really wants to leave the game
        menuView.menuButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // [P]
            @Override                                                                                 // [P]
            public void handle(MouseEvent event) {                                                    // [P]
                playSound("sound/Flashpoint001b.mp3");                                         // [P]
                playMusic("sound/theme_menu.mp3");                                             // [P]
                menuView.pausedMenuBox.setVisible(false);                                             // [P]
                menuView.messageBox.setVisible(true);                                                 // [P]
                if (gameState == GAMEOVER) {                                                          // [P]
                    messageView.removeMessage();                                                      // [P]
                }

            }
        });

        // [P] If "No" button is pressed - sends user back to the paused menu
        menuView.noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {   // [P]
            @Override                                                                                 // [P]
            public void handle(MouseEvent event) {                                                    // [P]
                playSound("sound/Flashpoint001b.mp3");                                         // [P]
                playMusic("sound/theme_menu.mp3");                                             // [P]
                menuView.pausedMenuBox.setVisible(true);                                              // [P]
                menuView.messageBox.setVisible(false);                                                // [P]
            }
        });

        // [P] If "Yes" button is pressed - saves game and sends user to the main menu
        menuView.yesButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {  // [P]
            @Override                                                                                 // [P]
            public void handle(MouseEvent event) {                                                    // [P]
                mediaPlayer.stop();                                                                   // [P]
                playSound("sound/Flashpoint001b.mp3");                                         // [P]
                try {                                                                                 // [P]
                    start(game_stage);                                                                // [P]
                } catch (Exception e) {                                                               // [P]
                    e.printStackTrace();                                                              // [P]
                }
            }
        });

		// [S] [P] Associates clicked button to the right method - saveGame
        menuView.saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.stop();                                           // [P]
                playSound("sound/Flashpoint001b.mp3");                 // [P]
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
                mediaPlayer.stop();                                                                   // [P]
                playSound("sound/Flashpoint001b.mp3");                                        // [P]
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
                        System.out.println("File with saves don't exist!");                           // [P]
                    } else {                                                                          // [P]
                        messageView.showAnimatedMessage("ERROR");                                 // [P]
                        resumeGame(event);                                                            // [P]
                        System.out.println("File with saves don't exist!!");                          // [P]
                    }
                }
            }
        });

        // [P] Associates clicked button to the right method - restartGame
        menuView.restartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // [P]
            @Override                                                                                    // [P]
            public void handle(MouseEvent event) {                                                       // [P]
                mediaPlayer.stop();                                                                      // [P]
                playSound("sound/ambient_techno1.mp3");                                          // [P]
                restartGame(event);                                                                      // [P]
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
        System.out.println(menuView);
    }


    // [P] Closes game
    public void exitGame(MouseEvent mouseEvent) {                                 // [P]
        System.exit(0);                                                     // [P]

    }

    // [P] Resumes the game
    public void resumeGame(MouseEvent mouseEvent) {                             // [P]
        menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible()); // [P]
		gameState = PLAYING;                                                    // [P]
    }

    // [P] Continues the game from the moment when it was ended when last time playing
    public void continueGame(MouseEvent mouseEvent) throws Exception{              // [P]
        mediaPlayer.stop();                                                        // [P]
        startGame(mouseEvent);                                                     // [P]
        loadGame();                                                                // [P]
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));   // [P]
        gameView.score.setText("SCORE: " + Integer.toString(game.score));          // [P]
        messageView.showAnimatedMessage("LOADED");                             // [P]

    }

    @Override
    public void start(Stage stage) throws Exception{
        playMusic("sound/Mercury.mp3");                                               // [P]
        Pane root = FXMLLoader.load(this.getClass().getResource("MenuInterface.fxml")); // [P]

        menuView = new MenuView(root);
        stage.setScene(new Scene(root, Color.BLACK));
        stage.show();
        stage.setTitle("SPACEGAME");

        // [P]  Checks if the game savings exists from before. If that is the case, than it enables
        //      continuing the game from the previous state from the Main Menu.
        File f = new File("./game.sav");        // [P]
        if(f.exists()) {                                 // [P]
            menuView.continueButton.setVisible(true);    // [P]
        } else {                                         // [P]
            menuView.continueButton.setVisible(false);   // [P]
        }                                                // [P]

        // [P] Once button "HELP" is clicked - hides main menu and shows controls menu
        menuView.helpButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {    // [P]
            @Override                                                                                    // [P]
            public void handle(MouseEvent event) {                                                       // [P]
                playSound("sound/Flashpoint001a.mp3");                                           // [P]
                menuView.mainMenuBox.setVisible(false);                                                  // [P]
                menuView.controlsMenuBox.setVisible(true);                                               // [P]
            }
        });

        // [P] Once button "GO BACK" is clicked - hides controls menu and shows main menu
        menuView.goBackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {  // [P]
            @Override                                                                                    // [P]
            public void handle(MouseEvent event) {                                                       // [P]
                playSound("sound/Flashpoint001a.mp3");                                           // [P]
                menuView.controlsMenuBox.setVisible(false);                                              // [P]
                menuView.mainMenuBox.setVisible(true);                                                   // [P]
            }
        });
    }
}
