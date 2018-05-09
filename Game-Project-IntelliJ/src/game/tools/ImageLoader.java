package game.tools;

import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import javafx.scene.image.Image;

/**
 * This is a static class used to load images. Either from a single String file
 * path, or fram a List of file paths.
 * @author Sebastian Jarsve
 */
public class ImageLoader {
    /**
     * Loads an image and returns it as a Image object.
     * @param file string value of the image file location.
     * @return Image
     */
    public static Image load(String file) {
        try {
            file = ImageLoader.class.getResource("../images/"+file).getPath();
            FileInputStream fis = new FileInputStream(file);
            Image image = new Image(fis);
            fis.close();
            return image;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns an List of Image objects.
     * @param files List<String> with image file paths.
     * @return List<Image>
     */
    public static List<Image> load(List<String> files) {
        List<Image> images = new ArrayList<Image>();
        for (String file : files) {
            images.add(load(file));
        }
        return images;
    }
}
