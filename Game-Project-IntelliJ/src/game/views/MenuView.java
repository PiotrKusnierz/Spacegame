package game.views;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MenuView {
    public ImageView menuImageView;


    public MenuView(Pane root) {
        menuImageView = (ImageView) root.lookup("#menuImgView");
    }


}
