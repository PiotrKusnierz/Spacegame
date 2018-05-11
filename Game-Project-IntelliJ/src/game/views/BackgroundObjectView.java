package game.views;

import java.util.List;
import java.util.Arrays;

import game.models.BackgroundObject;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import game.ImageLoader;

/**
 * Class that determines how the background objects are displayed in the game window.
 * The class does not modify the background objects in any way,
 * only the way they are displayed.
 * @author Inge Brochmann
 */
public class BackgroundObjectView {
    private GraphicsContext gc;
    private List<Image> images;

    public BackgroundObjectView (GraphicsContext gc) {
        this.gc = gc;
        loadImages();
    }

    public void loadImages() {
        this.images = ImageLoader.load(Arrays.asList(
                "png/effects/star2.png",
                "png/planets_lowres/planet_01.png",
                "png/planets_lowres/planet_03.png",
                "png/planets_lowres/planet_06.png",
                "png/planets_lowres/planet_07.png",
                "png/planets_lowres/planet_08.png",
                "png/planets_lowres/planet_09.png",
                "png/planets_lowres/planet_16.png"
        ));
    }

    public void draw(List<BackgroundObject> backObjects) {
        for (BackgroundObject backObj : backObjects) {
            gc.drawImage(images.get(backObj.type),
                    backObj.rect.x, backObj.rect.y,
                    backObj.rect.w, backObj.rect.h
            );
        }
    }
}