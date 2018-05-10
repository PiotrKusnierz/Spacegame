package game.views;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import game.models.BackgroundObject;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import game.models.Enemy;
import game.tools.ImageLoader;

public class BackgroundObjectView {

    private GraphicsContext gc;
    private List<Image> images;

    public BackgroundObjectView (GraphicsContext gc) {
        this.gc = gc;
        loadImages();
    }

    public void loadImages() {
        this.images = ImageLoader.load(Arrays.asList(
                "png/lasers/laserBlue08.png",
                "png/planets/planet12.png",
                "png/planets/planet18.png"
        ));
    }

    /**
     * Draws the Enemy objects on the the canvas.
     * @param backObjects The list of enemies to be drawn on the canvas.
     */
    public void draw(List<BackgroundObject> backObjects) {
        for (BackgroundObject backObj : backObjects) {
            gc.drawImage(images.get(backObj.type),
                    backObj.rect.x, backObj.rect.y,
                    backObj.rect.w, backObj.rect.h
            );
        }
    }

}
