package game.views;

import java.util.concurrent.ThreadLocalRandom;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import game.models.Enemy;
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
		this.images = new ArrayList<Image>();
		loadImages();
    }

	private void loadImage(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			images.add(new Image(fis));
			fis.close();
			System.out.println(getClass().getResource("../images").toString().split(":")[1]);
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

	public void loadImages() {
		loadImage(getClass().getResource("../images/png/enemies/enemyRed1.png").toString().substring(6));
		loadImage(getClass().getResource("../images/png/enemies/enemyGreen2.png").toString().substring(6));
	}

    // Method draws enemy objects
    public void draw(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            gc.setFill(this.color);
            // gc.fillOval(
            //     enemy.rect.x, enemy.rect.y,
            //     enemy.rect.w, enemy.rect.h
            // );
            gc.drawImage(images.get(enemy.type),
                enemy.rect.x, enemy.rect.y,
                enemy.rect.w, enemy.rect.h
            );
        }
    }
}
