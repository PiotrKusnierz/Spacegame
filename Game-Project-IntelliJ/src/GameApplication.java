import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private final int sizeX = 1200;
	private final int sizeY = 800;

	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, Color.MAGENTA);

		Canvas canvas = new Canvas(sizeX, sizeY);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		final long startNanoTime = System.nanoTime();

		new AnimationTimer() {
			double x = 0;
			boolean back = true;
			boolean paused = false;

			public void update() {
				if (x > 200) {
					back = true;
				} else if (x < 0) {
					back = false;
				}
				x += (back ? -0.1 : 0.1);
			}



			public void draw() {
				// gc.clearRect(0, 0, sizeX, sizeY);
				gc.setFill(Color.RED);
				gc.fillRect(x, x, 20, 20);
			}

			@Override
			public void handle(long now) {
				if (!paused) {
					update();
				}
				draw();
			}
		}.start();

		primaryStage.setTitle("Game");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
