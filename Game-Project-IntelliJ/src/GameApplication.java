import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Screen;

import javafx.geometry.Point2D;

import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

// For å lage javadoc: javadoc -private -d docs/ GameApplication.java

/**
* <h1>THE SPACESHIT - Pure Awesome</h1>
* GameApplication opens a game window of a Space Shooter kind of game.
* <p>
* All methods have been tested and adjusted through trial and error (gametesting)
*
* BY: Piotr Kusnierz, Sebastian Jarsve, Inge Brochmann
* @author Piotr Kusnierz, Sebastian Jarsve, Inge Brochmann
* @version 0.1a
* @since 2018
*
*/
public class GameApplication extends Application {

	private AnimationTimer timer;
	private Pane root;
	private ArrayList<GameObject> enemies = new ArrayList<GameObject>();

	private GameObject player;
	private boolean paused = false;

	private final int sizeY = (int)(Screen.getPrimary().getVisualBounds().getHeight()*0.95);
	private final int sizeX = (int)(sizeY*0.75);
	public int frameCounter = 0;

	/**
	* This method creates the root window, initializes the player object and creates the game loop.
	*/
	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(sizeX, sizeY);

		player = new GameObject(initPlayer(), 3);
		root.getChildren().add(player.getView());


		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				frameCounter++;

				if (frameCounter % 1000 == 0) {
					frameCounter = 0;
				}
			}
		};
		timer.start();

		return root;
	}

	/**
	* Method for initializing the player object.
	* @return Node
	*/
	private Node initPlayer() {
		Rectangle rect = new Rectangle(sizeX/40, sizeY/40, Color.MAGENTA);
		resetPlayerPosition(rect);
		return rect;
	}

	/**
	* Setting the player position to its initial location.
	* @param player: Player instance of type Object
	*/
	private void resetPlayerPosition(Node player) {
		player.setTranslateX(sizeX/2-sizeX/40);
		player.setTranslateY(sizeY - (sizeY/4));
	}


	/**
	* Creates an enemy object with a random size and location.
	* @return Node
	*/
	private Node initEnemy() {
		int enemySize = ThreadLocalRandom.current().nextInt(10, 120);
		Circle cir = new Circle(enemySize, Color.ORANGE);
		cir.setTranslateX(ThreadLocalRandom.current().nextInt(0, sizeX));
		root.getChildren().add(cir);
		return cir;
	}

	/**
	* Creates an animated message to the user.
	* @param msg: Type of String, text to be shown.
	*/
	private void showAnimatedMessage(String msg) {
		HBox hBox = new HBox();
		root.getChildren().add(hBox);

		for (int i = 0; i < msg.toCharArray().length; i++) {
			char letter = msg.charAt(i);

			Text text = new Text(String.valueOf(letter));
			text.setFont(Font.font(48));
			text.setOpacity(0);
			text.setFill(Color.WHITE);

			hBox.getChildren().add(text);

			FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
			ft.setToValue(1);
			ft.setDelay(Duration.seconds(i * 0.15));
			ft.play();
		}
		hBox.autosize();
		hBox.setTranslateX(sizeX/2-hBox.getWidth()/2);
		hBox.setTranslateY(sizeY/2-hBox.getHeight()/2);
	}

	/**
	* This methods runs every frame to update game state.
	*/
	private void update() {
		if (paused) {
			return;
		}


		player.setX(Math.min(sizeX - sizeX/40, Math.max(0, player.getX())));

		// if (player.getView().getTranslateX() <= 0) {
		// 	player.setVelocity(new Point2D(0 , 0));
		// }

		player.update();

		ArrayList<GameObject> removedEnemies = new ArrayList<GameObject>();

		if (!player.isAlive()) {
			timer.stop();
			showAnimatedMessage("GAME OVER");
		}

		for (GameObject enemy : enemies) {
			enemy.getView().setTranslateY(enemy.getView().getTranslateY() + Math.random() * 6);
			if (enemy.getView().getTranslateY() > sizeY*2) {
				removedEnemies.add(enemy);
			}
		}

		for (GameObject enemy : removedEnemies) {
			enemies.remove(enemy);
			root.getChildren().remove(enemy.getView());
		}

		ifColliding();

		if (frameCounter % 100 == 0) {
			enemies.add(new GameObject(initEnemy(), 1));
		}
	}

	/**
	* If the player collides with an enemy, the enemy should be removed and
	* you will lose one of your lives.
	*/
	private void ifColliding() {
		ArrayList<GameObject> removedEnemies = new ArrayList<GameObject>();
		for (GameObject enemy : enemies) {
			if (enemy.getView().getBoundsInParent().intersects(player.getView().getBoundsInParent())) {
				resetPlayerPosition(player.getView());
				removedEnemies.add(enemy);
				player.lives--;
				break;
			}
		}
		for (GameObject enemy : removedEnemies) {
			enemies.remove(enemy);
			root.getChildren().remove(enemy.getView());
		}
	}

	/**
	* An overriden method of the Application.
	* This method runs once at application startup, and sets the stage and
	* scene of the game, as well as adding a keyboard handler for input.
	* @param stage: A Stage object which acts as the main container.
	*/
	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(createContent(), Color.BLACK));

		stage.getScene().setOnKeyPressed(event -> {     //input
			switch (event.getCode()) {
				case LEFT:
					// player.getView().setTranslateX(player.getView().getTranslateX() - 40);
					player.setVelocity(new Point2D(-3, 0));
					break;
				case RIGHT:
					// player.getView().setTranslateX(player.getView().getTranslateX() + 40);
					player.setVelocity(new Point2D(3, 0));
					break;
				case P:
					paused = paused ? false : true;
					break;
				default:
					break;
			}
		});

		stage.getScene().setOnKeyReleased(event -> {
			switch (event.getCode()) {
				case LEFT:
					player.setVelocity(new Point2D(0, 0));
					break;
				case RIGHT:
					player.setVelocity(new Point2D(0, 0));
					break;
			}
		});

		stage.show();
		stage.setTitle("THE SPACESHIT - Pure Awesome");
	}

	/**
	* The main method to run the Application.
	*/
	public static void main(String[] args) {
		launch(args);
	}
}
