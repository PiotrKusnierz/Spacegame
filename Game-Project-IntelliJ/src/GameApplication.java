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

import java.util.concurrent.ThreadLocalRandom;
// import javafx.animation.FadeTransition;
// import javafx.scene.layout.HBox;
// import javafx.scene.text.Font;
// import javafx.scene.text.Text;
// import javafx.util.Duration;

import java.util.ArrayList;

public class GameApplication extends Application {

	private AnimationTimer timer;
	private Pane root;
	private ArrayList<Node> enemies = new ArrayList<Node>();

	private Node player;
	private int playerLives = 3;

	private final int sizeY = (int)(Screen.getPrimary().getVisualBounds().getHeight()*0.95);
	private final int sizeX = (int)(sizeY*0.75);
	public int frameCounter = 0;

	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(sizeX, sizeY);

		player = initPlayer();
		root.getChildren().add(player);


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

	private Node initPlayer() {
		Rectangle rect = new Rectangle(sizeX/40, sizeY/40, Color.MAGENTA);
		resetPlayerPosition(rect);
		return rect;
	}

	private void resetPlayerPosition(Node player) {
		player.setTranslateX(sizeX/2-sizeX/40);
		player.setTranslateY(sizeY - (sizeY/4));
	}


	private Node initEnemy() {
		int enemySize = ThreadLocalRandom.current().nextInt(10, 120);
        Circle cir = new Circle(enemySize, Color.ORANGE);

        cir.setTranslateX((int)(Math.random() * 30) * 40);
        root.getChildren().add(cir);
        return cir;
	}

	private void update() {
		// Example, stopping the game to show that you lost the game
		if (playerLives == 0) {
			return;
		}
		for (Node enemy : enemies) {
			enemy.setTranslateY(enemy.getTranslateY() + Math.random() * 6);
		}

		ifColiding();

		if (frameCounter % 100 == 0) {
			enemies.add(initEnemy());
		}
		// if (Math.random() < 0.05) {
		// 	enemies.add(initEnemy());
		// }
	}

	private void ifColiding() {
        for (Node enemy : enemies) {
            if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())) {
				resetPlayerPosition(player);
				playerLives--;
                return;
            }
        }
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(createContent(), Color.BLACK));

		stage.getScene().setOnKeyPressed(event -> {     //input
			switch (event.getCode()) {
				case LEFT:
					player.setTranslateX(player.getTranslateX() - 40);
					break;
				case RIGHT:
					player.setTranslateX(player.getTranslateX() + 40);
					break;
				default:
					break;
			}
		});

			stage.show();
			stage.setTitle("Awesome, Awesome, Awesome!");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
