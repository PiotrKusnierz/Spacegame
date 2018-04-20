package game.views;

import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import game.models.Enemy;
import game.tools.ImageLoader;
/**
* Class that determines how the enemy objects are displayed in the game window.
* The class does not modify the enemy objects in any way,
* only the way they are displayed.
*/
public class EnemyView {
    public Color color;
    private GraphicsContext gc;
	private List<Image> images;

    public EnemyView(GraphicsContext gc) {
        this.gc = gc;
        this.color = Color.ORANGE;
		loadImages();
    }

	public void loadImages() {
		ImageLoader loader = new ImageLoader();
		loader.load("game/images/png/enemies/enemyGreen1.png");
		loader.load("game/images/png/enemies/enemyBlack3.png");
		images = loader.getAll();
	}

    // Method draws enemy objects
    public void draw(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            gc.setFill(this.color);
            gc.fillOval(
                enemy.rect.x, enemy.rect.y,
                enemy.rect.w, enemy.rect.h
            );
        }
    }
}
