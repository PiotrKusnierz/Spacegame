package game.views;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import game.models.Player;
import game.tools.Point;
import game.tools.ImageLoader;

/**
* Class that determines how the player object is displayed in the game window.
* The class does not modify the player object in any way,
* only the way it is displayed.
*/
public class PlayerView {
    public Color color;
    private GraphicsContext gc;
	private List<Image> images;

    public PlayerView(GraphicsContext gc) {
        this.gc = gc;
        this.color = Color.MAGENTA;
		loadImages();
    }

	public void loadImages() {
		ImageLoader loader = new ImageLoader();
		loader.load("game/images/png/playerShip1_blue.png");
		images = loader.getAll();
	}

    // Method draws player object
    public void draw(Player player) {
        gc.setFill(this.color);
        gc.drawImage(images.get(0),
            player.rect.x, player.rect.y,
            player.rect.w, player.rect.h
        );
		gc.setFill(Color.BLUE);
		for (Point bullet : player.bullets) {
			gc.fillOval(bullet.x, bullet.y, 10, 10);
		}
    }
}
