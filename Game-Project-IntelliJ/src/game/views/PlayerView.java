package game.views;

import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import game.models.Player;
import game.tools.Point;

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
		this.images = new ArrayList<Image>();
		loadImages();
    }

	private void loadImage(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			images.add(new Image(fis));
			fis.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(System.getProperty("user.dir"));
			System.exit(1);
		} 
	}

	public void loadImages() {
		loadImage("src/game/images/png/playerShip1_blue.png");
	}

    // Method draws player object
    public void draw(Player player) {
        gc.setFill(this.color);
        // gc.fillRect(
        //     player.rect.x, player.rect.y,
        //     player.rect.w, player.rect.h
        // );
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
