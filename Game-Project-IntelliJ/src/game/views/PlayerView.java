package game.views;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import game.models.Player;
import game.tools.Point;
import game.ImageLoader;

/**
* Class that determines how the player object is displayed in the game window.
* The class does not modify the player object in any way,
* only the way it is displayed.
*/
public class PlayerView{
    private GraphicsContext gc;
    private List<Image> images;

    /**
     * PlayerView constructor.
     * @param gc The GraphicsContext is where the player is drawn.
     */
    public PlayerView(GraphicsContext gc) {
        this.gc = gc;
        this.images = new ArrayList<Image>();
        loadImages();
    }

    /**
     * This method loads images into a private List, to use in the draw method.
     * @author Sebastian Jarsve
     */
    private void loadImages() {
        this.images = ImageLoader.load(Arrays.asList(
            "png/playerShip1_blue.png",
            "png/lasers/laserBlue08.png"
        ));
    }

    /**
     * This method draws the player object on the canvas.
     * @param player Player object to be drawn.
     */
    public void draw(Player player) {
        gc.drawImage(images.get(0),
            player.rect.x, player.rect.y,
            player.rect.w, player.rect.h
        );
        gc.setFill(Color.YELLOW);
        for (Point bullet : player.bullets) {
            gc.drawImage(
            images.get(1),
            bullet.x, bullet.y, 10, 10
            );
        }
    }
}
