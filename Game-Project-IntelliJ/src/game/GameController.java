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
import static javafx.scene.media.MediaPlayer.INDEFINITE;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import game.models.*;
import game.views.*;
import game.tools.*;


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
    public static MediaPlayer mediaPlayer;
    public static MediaPlayer musicPlayer;
    public Size windowSize;

    private int gameState;
    private final int PAUSED = 1;
    private final int PLAYING = 2;
    private final int GAMEOVER = 3;
    private final int GAMEWON = 4;

    /**
     * Plays sound effects.
     * @param filename The file path of the sound, relative to the src/game/ folder.
     * @author Piotr Kusnierz
     */
    public void playSound(String filename) {
        Media sound  = new Media(getClass().getResource(filename).toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.3);
        mediaPlayer.play();
    }

    /**
     * Plays and constantly repeats music.
     * @param filename The file path of the music, relative to the src/game/ folder.
     * @author Piotr Kusnierz
     */
    public void playMusic(String filename) {
        Media music  = new Media(getClass().getResource(filename).toString());
        musicPlayer = new MediaPlayer(music);
        musicPlayer.setCycleCount(INDEFINITE);
        musicPlayer.setVolume(0.6);
        musicPlayer.play();
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
            playSound("sound/boom7.mp3");
            enemy.lives--;
            game.player.lives--;
            gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));
        }
    }

    /**
     * Adds a value to the score and updates it on the screen.
     * @param value what will be added to the score.
     * @author Sebastian Jarsve
     */
    public void updateScore(int value) {
    game.score += value;
    gameView.score.setText("SCORE: " + Integer.toString(game.score));
    }

  /**
   * Adds the enemy boss.
   * @author Sebastian Jarsve, Piotr Kusnierz
   */
    private void addBoss() {
        playSound("sound/Jingle_Achievement_00.mp3");
        messageView.showAnimatedMessage("Final Level");
        game.boss = new Enemy(windowSize.w/2 - 50,800,100);
        game.boss.type = 7;
        game.boss.velocity.y = -0.5;
        game.boss.lives = 50;
        game.enemies.add(game.boss);
    }

    /**
     * This runs every frame, and contains the logic part of the game.
     */
    public void update() {
        if (gameState != PLAYING) {
            return;
        }
    game.frameCounter++;
    if (game.frameCounter % 1800 == 0 && game.level < 4) {
        game.level++;
        game.frameCounter = 0;
        if (game.level < 4) {
            messageView.showAnimatedMessage(String.format("Level %d", game.level));
        } else {
            addBoss();
        }
        playSound("sound/Upper01.mp3");
    } else if (game.frameCounter % 100 == 0) {
        messageView.removeMessage();
    }
    if (game.level == 4) {
        if (game.boss.lives <= 0) {
            mediaPlayer.stop();
            playSound("sound/Jingle_Win_00.mp3");
            messageView.showAnimatedMessage("YOU WON!");
            gameState = GAMEWON;
        }
    }
    if (game.frameCounter % 50 == 0) {
        updateScore(1);
    }
    if (game.frameCounter % 10 == 0 && this.isShooting) {
        game.player.shoot();
        playSound("sound/laserfire01.mp3");
    }
        game.player.update();
        game.player.clampPosition(0, windowSize.w);
        if (game.player.lives <= 0) {
            gameState = GAMEOVER;
            playSound("sound/Jingle_Lose_00.mp3");
            messageView.showPeristantAnimatedMessage("GAME OVER");
            return;
        }
        for (Enemy enemy : game.enemies) {
            enemy.update();
            collisionHandler(enemy);
            if (enemy.type == 7) {
                if(enemy.rect.y <= 400) {
                    enemy.velocity.y = 0;
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
                    playSound("sound/8bit_bomb_explosion.mp3");
                }
            }
            if (enemy.lives < 1 || enemy.rect.top() < 0) {
                removedEnemies.add(enemy);
            }
        }
        if (Math.random() < 0.02 * boost) {
            switch (game.level) {
                case 1:
                    addEnemy(ThreadLocalRandom.current().nextInt(0, 3));
                    break;
                case 2:
                    addEnemy(ThreadLocalRandom.current().nextInt(3, 5));
                    playSound("sound/space2.mp3");
                    break;
                case 3:
                    addEnemy(ThreadLocalRandom.current().nextInt(5, 7));
                    playSound("sound/space2.mp3");
                    break;
                case 4:
                    addEnemyChildren();
                    playSound("sound/19.wav");
                    break;
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
        playMusic("sound/Ambience_AlienPlanet_00.mp3");            // [P]
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
                mediaPlayer.stop();
                if (gameState == GAMEOVER || gameState == GAMEWON) {
                    menuView.resumeButton.setVisible(false);
                    menuView.saveButton.setVisible(false);
                } else {
                    gameState = gameState == PLAYING ? PAUSED : PLAYING;
                }
                messageView.removeMessage();
                playMusic("sound/theme_menu.mp3");
                menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
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
                playSound("sound/engine_takeoff.mp3");
                for (Enemy enemy : game.enemies) {
                    enemy.boost = boost;
                }
            }
            if (event.getCode() == KeyCode.SPACE) {
                if (!this.isShooting) {
                	game.player.shoot();
                    playSound("sound/laserfire01.mp3");
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
                mediaPlayer.stop();
            }
            if (event.getCode() == KeyCode.SPACE) {
                this.isShooting = false;
            }
        });
    }

    /**
     * Restarts the game.
     * @author Piotr Kusnierz
     */
    private void restartGame() {
        newGame();
        menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
        messageView.showAnimatedMessage("NEW GAME");
        menuView.resumeButton.setVisible(true);
        menuView.saveButton.setVisible(true);
    }

    /**
     * Exits game.
     * @author Piotr Kusnierz
     */
    public void exitGame() {
        System.exit(0);
    }

    /**
     * Resumes the game.
     * @author Piotr Kusnierz
     */
    public void resumeGame() {
        menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
        gameState = PLAYING;
    }

    /**
     * Continues the game from the moment when it was ended when last time playing.
     * @param mouseEvent
     * @author Piotr Kusnierz
     */
    public void continueGame(MouseEvent mouseEvent) throws Exception{
        musicPlayer.stop();
        startGame(mouseEvent);
        loadGame();
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));
        gameView.score.setText("SCORE: " + Integer.toString(game.score));
        messageView.showAnimatedMessage("LOADED");
    }

    // [P] Starts the game on onMouseClicked event when "Start Game" options from the menu is chosen
    /**
     * This method loads the game interface from FXML, sets the initial game variables and start the game.
     * @param mouseEvent
     */
    public void startGame(MouseEvent mouseEvent) throws Exception{                             // [P]
        musicPlayer.stop();                                                                    // [P]
        Pane root = FXMLLoader.load(this.getClass().getResource("GameInterface.fxml"));  // [P]
        Stage game_stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();     // [P]
        game_stage.setScene(new Scene(root, Color.BLACK));

        playSound("sound/ambient_techno1.mp3");                                         // [P]

        gameState = PLAYING;
        menuView = new MenuView(root);
        gameView = new GameView(root);
        windowSize = new Size(gameView.canvas.getWidth(), gameView.canvas.getHeight());
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

        /**
         * Once "Resume Game" is clicked calls for the resumeGame method.
         * @author Sebastian Jarsve, Piotr Kusnierz
         */
        menuView.resumeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            mediaPlayer.stop();
            resumeGame();
            playSound("sound/ambient_techno1.mp3");
        }
    });

        /**
         * Once "Main Menu" is clicked, hides VBox containing paused menu and shows VBox which contains
         * question to the user, if he really wants to leave the game.
         * @author Piotr Kusnierz
         */
        menuView.menuButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playSound("sound/Flashpoint001b.mp3");
                playMusic("sound/theme_menu.mp3");
                menuView.pausedMenuBox.setVisible(false);
                menuView.messageBox.setVisible(true);
                if (gameState == GAMEOVER) {
                    messageView.removeMessage();
                }
            }
        });

        /**
         * Once "No" button is pressed, hides VBox containing question to the user and shows VBox
         * containing paused menu.
         * @author Piotr Kusnierz
         */
        menuView.noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playSound("sound/Flashpoint001b.mp3");
                playMusic("sound/theme_menu.mp3");
                menuView.pausedMenuBox.setVisible(true);
                menuView.messageBox.setVisible(false);
            }
        });

        /**
         * Once "Yes" button is pressed calls for the start method.
         * @author Piotr Kusnierz
         */
        menuView.yesButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.stop();
                playSound("sound/Flashpoint001b.mp3");
                try {
                    start(game_stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Once "Save Game" is clicked calls for the saveGame method, resumes the game and
         * shows animated message "SAVED".
         * @author Sebastian Jarsve, Piotr Kusnierz
         */
        menuView.saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.stop();
                playSound("sound/Flashpoint001b.mp3");
                saveGame();
                menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
                gameState = PLAYING;
                messageView.showAnimatedMessage("SAVED");
            }
        });

        /**
         * Once "Load Game" is clicked checks if the file with saves exists. If that is the case
         * calls for the loadGame method and resumes the game showing animated message "LOADED".
         * Otherwise prints out message to the user which says that file don't exists and also
         * shows animated message "ERROR".
         * @author Piotr Kusnierz
         */
        menuView.loadButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.stop();
                playSound("sound/Flashpoint001b.mp3");
                File f = new File("./game.sav");
                if(f.exists()) {
                    loadGame();
                    gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));
                    gameView.score.setText("SCORE: " + Integer.toString(game.score));
                    menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
                    messageView.removeMessage();
                    gameState = PLAYING;
                    messageView.showAnimatedMessage("LOADED");
                    menuView.resumeButton.setVisible(true);
                    menuView.saveButton.setVisible(true);
                } else {
                    if (gameState == GAMEOVER) {
                        gameState = GAMEOVER;
                        System.out.println("File with saves don't exist!");
                    } else {
                        messageView.showAnimatedMessage("ERROR");
                        resumeGame();
                        System.out.println("File with saves don't exist!!");
                    }
                }
            }
        });

        /**
         * Once "Restart Game" is clicked calls for the restartGame method.
         * @author Piotr Kusnierz
         */
        menuView.restartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.stop();
                playSound("sound/ambient_techno1.mp3");
                restartGame();
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception{
        playMusic("sound/Mercury.mp3");                                               // [P]
        Pane root = FXMLLoader.load(this.getClass().getResource("MenuInterface.fxml")); // [P]
        menuView = new MenuView(root);
        stage.setScene(new Scene(root, Color.BLACK));
        stage.show();
        stage.setTitle("SPACEGAME");

        /**
         * Checks if the file with game saves exists from before. If that is the case, than it shows
         * "CONTINUE" button at the main menu. Otherwise hides it.
         * @author Piotr Kusnierz
         */
        File f = new File("./game.sav");
        if(f.exists()) {
            menuView.continueButton.setVisible(true);
        } else {
            menuView.continueButton.setVisible(false);
        }

        /**
         * Once "HELP" is clicked, hides VBox containing main menu and shows VBox containing
         * information about the controls.
         * @author Piotr Kusnierz
         */
        menuView.helpButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playSound("sound/Flashpoint001a.mp3");
                menuView.mainMenuBox.setVisible(false);
                menuView.controlsMenuBox.setVisible(true);
            }
        });

        /**
         * Once "GO BACK" is clicked, hides VBox containing information about the controls
         * and shows VBox containing main menu.
         * @author Piotr Kusnierz
         */
        menuView.goBackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playSound("sound/Flashpoint001a.mp3");
                menuView.controlsMenuBox.setVisible(false);
                menuView.mainMenuBox.setVisible(true);
            }
        });
    }
}
