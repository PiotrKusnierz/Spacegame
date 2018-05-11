package game;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.INDEFINITE;
import javafx.scene.Node;
import javafx.stage.Stage;
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
    private List<BackgroundObject> removedBackObjects;
    private List<Point> removedBullets;
    private boolean isShooting = false;
    private double boost = 1;
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
		new AudioClip(GameController.class.getResource(filename).toString()).play();
    }

    /**
     * Plays and constantly repeats music.
     * @param filename The file path of the music, relative to the src/game/ folder.
     * @author Piotr Kusnierz
     */
    public void playMusic(String filename) {
		Media music = new Media(GameController.class.getResource(filename).toString());
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
            updateLives(-1);
        }
    }

    /**
     * Updates the score to the screen.
     * @author Sebastian Jarsve
     */
    public void updateScore() {
        gameView.score.setText("SCORE: " + Integer.toString(game.score));
    }

    /**
     * Adds a value to the score and updates it on the screen.
     * @param value what will be added to the score.
     * @author Sebastian Jarsve
     */
    public void updateScore(int value) {
        game.score += value;
        updateScore();
    }

    /**
     * Updates the score to the screen.
     * @author Sebastian Jarsve, Piotr Kusnierz
     */
    public void updateLives() {
        gameView.lives.setText("LIVES: " + Integer.toString(game.player.lives));
    }

    /**
     * Adds a value to the life and updates it on the screen.
     * @param value what will be added to the life.
     * @author Sebastian Jarsve, Piotr Kusnierz
     */
    public void updateLives(int value) {
        game.player.lives += value;
        updateLives();
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
                // mediaPlayer.stop();
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

        // NEW |||||||||||||||||||||||||||||||||||||||||||||||
        for (BackgroundObject backObj : game.backgroundPlanet) {
            backObj.update();
            if (backObj.rect.top() < 0) {
                removedBackObjects.add(backObj);
            }
        }

        for (BackgroundObject backObj : game.backgroundStar) {
            backObj.update();
            if (backObj.rect.top() < 0) {
                removedBackObjects.add(backObj);
            }
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

        if (ThreadLocalRandom.current().nextInt(0, 10000) < (6 * boost)) {
            addBackObj(ThreadLocalRandom.current().nextInt(1, 8));
        }

        if (Math.random() < 0.035 * boost) {
            addBackObj(0);
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

        // NEW ||||||||||||||||
        game.backgroundPlanet.removeAll(removedBackObjects);
        game.backgroundStar.removeAll(removedBackObjects);

        game.enemies.removeAll(removedEnemies);
        game.player.bullets.removeAll(removedBullets);
        //NEW |||||||||||||||
        removedBackObjects.clear();

        removedEnemies.clear();
        removedBullets.clear();
    }

    /**
     * This runs every frame to update the game graphics.
     */
    public void draw() {
        gameView.clearCanvas();
        // NEW |||||||||||||||||||
        gameView.backObjView.draw(game.backgroundStar);
        gameView.backObjView.draw(game.backgroundPlanet);
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

    // NEW ||||||||||||||||||||||||||||||||
    public void addBackObj (int type) {
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.005, windowSize.w*0.02);
        if (type != 0) {
            if (Math.random() < 0.15) {
                r = windowSize.w*1.2;
            } else {
                r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.15, windowSize.w*0.45);
            }
        }
        double x = ThreadLocalRandom.current().nextDouble(0-2*r/3, windowSize.w-(r/3));
        double y = windowSize.h+r;
        BackgroundObject backObj = new BackgroundObject(x, y, r, r, boost);
        backObj.type = type;
        if (type == 0){
            game.backgroundStar.add(backObj);
        } else {
            game.backgroundPlanet.add(backObj);
        }
    }

    public void addInitialBackObj(){
        for (int i = 0; i < 20; i++){
            double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.005, windowSize.w*0.02);
            double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
            double y = ThreadLocalRandom.current().nextDouble(0,windowSize.h-r);
            BackgroundObject backObj = new BackgroundObject(x, y, r, r);
            backObj.type = 0;
            game.backgroundStar.add(backObj);
        }
    }

    /**
     * Spawns small enemies from the boss, and draws them underneath it.
     * @author Sebastian Jarsve, Piotr Kusnierz
     */
    public void addEnemyChildren() {
        double r = 20;
        Point position = game.boss.rect.center();
        Enemy enemy = new Enemy(position.x, position.y, r, boost);
        enemy.type = 8;
        game.enemies.remove(game.boss);
        game.enemies.add(enemy);
        game.enemies.add(game.boss);
        playMusic("sound/Ambience_AlienPlanet_00.mp3");
    }

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

        // NEW ||||||||||||||
        removedBackObjects.clear();

        removedEnemies.clear();
        removedBullets.clear();
        messageView.removeMessage();
        gameState = PLAYING;
        updateLives();
        updateScore();
        // NEW |||||||||||
        addInitialBackObj();
    }

    /**
     * Adds a keyboard input event handler to the scene.
     * @param scene The scene to add the event handler.
     */
    public void addEventHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (gameState == GAMEOVER || gameState == GAMEWON) {
                    menuView.resumeButton.setVisible(false);
                    menuView.saveButton.setVisible(false);
                } else {
                    gameState = gameState == PLAYING ? PAUSED : PLAYING;
                }
                messageView.removeMessage();
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
				if (boost != 3) {
					playSound("sound/engine_takeoff.mp3");
				}
                boost = 3;
                for (Enemy enemy : game.enemies) {
                    enemy.boost = boost;
                }
                // NEW |||||||||||||
                for (BackgroundObject backObj : game.backgroundPlanet) {
                    backObj.boost = boost;
                }
                for (BackgroundObject backObj : game.backgroundStar) {
                    backObj.boost = boost;
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
                for (BackgroundObject backObj : game.backgroundPlanet) {
                    backObj.boost = boost;
                }
                for (BackgroundObject backObj : game.backgroundStar) {
                    backObj.boost = boost;
                }
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
     * @param mouseEvent Gets the mouseEvent and send it to the startGame method.
     * @author Piotr Kusnierz
     * @throws Exception throws an Exception (if any) to handle it elsewhere.
     */
    public void continueGame(MouseEvent mouseEvent) throws Exception{
        musicPlayer.stop();
        startGame(mouseEvent);
        loadGame();
        updateLives();
        updateScore();
        messageView.showAnimatedMessage("LOADED");
    }

    /**
     * This method loads the game interface from FXML, handles menu button
     * actions, sets the initial game variables and starts the game.
     * @param mouseEvent Used to find the Application Stage.
     * @throws Exception throws an Exception (if any) to handle it elsewhere.
     */
    public void startGame(MouseEvent mouseEvent) throws Exception{
        musicPlayer.stop();
        Pane root = FXMLLoader.load(GameController.class.getResource("GameInterface.fxml"));
        Stage game_stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        game_stage.setScene(new Scene(root, Color.BLACK));

        playSound("sound/ambient_techno1.mp3");

        gameState = PLAYING;
        menuView = new MenuView(root);
        gameView = new GameView(root);
        windowSize = new Size(gameView.canvas.getWidth(), gameView.canvas.getHeight());
        messageView = new MessageView(root);

        game = new Game();
        game.player = new Player(windowSize.w/2, windowSize.h*0.2, windowSize.w*0.12, windowSize.w*0.12);

        // NEW |||||||||||||||||||||||||||||||||||||||||||
        game.backgroundPlanet = new ArrayList<BackgroundObject>();
        game.backgroundStar = new ArrayList<BackgroundObject>();
        addInitialBackObj();

        game.enemies = new ArrayList<Enemy>();

        // NEW |||||||||||||||
        removedBackObjects = new ArrayList<BackgroundObject>();
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
        updateLives();
        updateLives();
        updateScore();

        /**
         * Once "Resume Game" is clicked calls for the resumeGame method.
         * @author Sebastian Jarsve, Piotr Kusnierz
         */
        menuView.resumeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            resumeGame();
            playSound("sound/ambient_techno1.mp3");
        });

        /**
         * Once "Main Menu" is clicked, hides VBox containing paused menu and shows VBox which contains
         * question to the user, if he really wants to leave the game.
         * @author Piotr Kusnierz
         */
        menuView.menuButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
              playSound("sound/Flashpoint001b.mp3");
              menuView.pausedMenuBox.setVisible(false);
              menuView.messageBox.setVisible(true);
              if (gameState == GAMEOVER) {
                  messageView.removeMessage();
              }
        });

        /**
         * Once "No" button is pressed, hides VBox containing question to the user and shows VBox
         * containing paused menu.
         * @author Piotr Kusnierz
         */
        menuView.noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            playSound("sound/Flashpoint001b.mp3");
            menuView.pausedMenuBox.setVisible(true);
            menuView.messageBox.setVisible(false);
        });

        /**
         * Once "Yes" button is pressed calls for the start method.
         * @author Piotr Kusnierz
         */
        menuView.yesButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            playSound("sound/Flashpoint001b.mp3");
            try {
                start(game_stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /**
         * Once "Save Game" is clicked calls for the saveGame method, resumes the game and
         * shows animated message "SAVED".
         * @author Sebastian Jarsve, Piotr Kusnierz
         */
        menuView.saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            playSound("sound/Flashpoint001b.mp3");
            saveGame();
            menuView.pausedMenuBox.setVisible(!menuView.pausedMenuBox.isVisible());
            gameState = PLAYING;
            messageView.showAnimatedMessage("SAVED");
        });

        /**
         * Once "Load Game" is clicked checks if the file with saves exists. If that is the case
         * calls for the loadGame method and resumes the game showing animated message "LOADED".
         * Otherwise prints out message to the user which says that file don't exists and also
         * shows animated message "ERROR".
         * @author Piotr Kusnierz
         */
        menuView.loadButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            playSound("sound/Flashpoint001b.mp3");
            File f = new File("./game.sav");
            if(f.exists()) {
                loadGame();
                updateLives();
                updateScore();
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
        });

        /**
         * Once "Restart Game" is clicked calls for the restartGame method.
         * @author Piotr Kusnierz
         */
        menuView.restartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            playSound("sound/ambient_techno1.mp3");
            restartGame();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws Exception{
        playMusic("sound/Mercury.mp3");
        Pane root = FXMLLoader.load(GameController.class.getResource("MenuInterface.fxml"));
        menuView = new MenuView(root);
        stage.setScene(new Scene(root, Color.BLACK));
        stage.show();
        stage.setTitle("SPACEGAME");


        //  Checks if the file with game saves exists from before. If that is the case, than it shows
        //  "CONTINUE" button at the main menu. Otherwise hides it.
        //  @author Piotr Kusnierz
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
        menuView.helpButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                playSound("sound/Flashpoint001a.mp3");
                menuView.mainMenuBox.setVisible(false);
                menuView.controlsMenuBox.setVisible(true);
        });

        /**
         * Once "GO BACK" is clicked, hides VBox containing information about the controls
         * and shows VBox containing main menu.
         * @author Piotr Kusnierz
         */
        menuView.goBackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                playSound("sound/Flashpoint001a.mp3");
                menuView.controlsMenuBox.setVisible(false);
                menuView.mainMenuBox.setVisible(true);
        });
    }
}
