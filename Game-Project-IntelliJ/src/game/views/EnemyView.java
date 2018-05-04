package game.views;

import java.util.concurrent.ThreadLocalRandom;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

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
		this.images = new ArrayList<Image>();
		loadImages();
    }

	/**
	 * Loads images into a List.
	 * @author Sebastian
	 */
	public void loadImages() {
		images = ImageLoader.load(Arrays.asList(
			"png/meteors/meteorBrown_big1.png",
			"png/meteors/meteorBrown_big2.png",
			"png/meteors/meteorBrown_big3.png",
			"png/enemies/enemyGreen2.png",
			"png/enemies/enemyRed1.png",
			"png/enemies/enemyRed4.png",
			"png/enemies/enemyGreen4.png",
			"png/ufoRed.png",
			"png/ufoGreen.png"
		));
	}

    // Method draws enemy objects
    public void draw(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            gc.drawImage(images.get(enemy.type % images.size()),
                enemy.rect.x, enemy.rect.y,
                enemy.rect.w, enemy.rect.h
            );
        }
    }
}
