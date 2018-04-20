package game.tools;

import java.util.ArrayList;
import java.io.FileInputStream;
import javafx.scene.image.Image;

/**
 * This class loads images to its images list, which you later can get from
 * its getImage and getAll method.
 */
public class ImageLoader {
	private FileInputStream fis;
	private ArrayList<Image> images = new ArrayList<Image>();

	/**
	 * Loads images into the images array.
	 * @param filePath string value of the image file location.
	 */
	public void load(String filePath) {
		try {
			fis = new FileInputStream(filePath);
			images.add(new Image(fis));
			fis.close();
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Returns an image from the images array.
	 * @param index images gets stored in an array ordered in chronological
	 * order. The index will retrieve one of the images in the array.
	 * @return Image
	 */
	public Image getImage(int index) {
		return images.get(index);
	}

	/**
	 * Returns the whole images array.
	 * @return ArrayList<Image>
	 */
	public ArrayList<Image> getAll() {
		return images;
	}
}
