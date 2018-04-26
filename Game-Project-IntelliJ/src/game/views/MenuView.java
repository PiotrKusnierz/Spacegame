package game.views;


import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MenuView {
    public Text continueButton;


    public MenuView(Pane root) {
        continueButton = (Text) root.lookup("#continueButton");
    }


}
