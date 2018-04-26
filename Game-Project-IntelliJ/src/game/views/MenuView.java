package game.views;


import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuView {
    public VBox pausedMenuBox;
    public Text resumeButton;
    public Text saveButton;
    public Text loadButton;
    public Text exitButton;




    public Text continueButton;


    public MenuView(Pane root) {
        pausedMenuBox = (VBox) root.lookup("#pausedMenuBox");   // [P]
        resumeButton = (Text) root.lookup("#resumeButton");
        saveButton = (Text) root.lookup("#saveButton");
        loadButton = (Text) root.lookup("#loadButton");
        exitButton = (Text) root.lookup("#exitButton");





        continueButton = (Text) root.lookup("#continueButton");
    }


}
