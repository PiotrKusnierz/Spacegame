package game.views;

import java.util.List;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import game.models.Enemy;
import game.ImageLoader;

/**
 * Class that determines how the enemy objects are displayed in the game window.
 * The class does not modify the enemy objects in any way,
 * only the way they are displayed.
 */
public class EnemyView {
    private GraphicsContext gc;
    private List<Image> images;

   /**
    * EnemyView constructor which determines how the enemy is shown on screen.
    * @param gc GraphicsContext (canvas) to draw on.
    */
    public EnemyView(GraphicsContext gc) {
        this.gc = gc;
        loadImages();
    }

    /**
     * Loads images into a List object.
     * @author Sebastian Jarsve
     */
    public void loadImages() {
        this.images = ImageLoader.load(Arrays.asList(
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

    /**
     * Draws the Enemy objects on the the canvas.
     * @param enemies The list of enemies to be drawn on the canvas.
     */
    public void draw(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            gc.drawImage(images.get(enemy.type % images.size()),
                enemy.rect.x, enemy.rect.y,
                enemy.rect.w, enemy.rect.h
            );
        }
    }
}
