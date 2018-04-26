package game.views;


import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *  [P] Class holds variables which belongs to the menues.
 */

public class MenuView {
    public VBox pausedMenuBox; // [P]
    public Text resumeButton; // [P]
    public Text saveButton;   // [P]
    public Text loadButton;  // [P]
    public Text exitButton;  // [P]
    public Text continueButton; // [P]

    public MenuView(Pane root) {
        pausedMenuBox = (VBox) root.lookup("#pausedMenuBox");   // [P]
        resumeButton = (Text) root.lookup("#resumeButton");  // [P]
        saveButton = (Text) root.lookup("#saveButton");  // [P]
        loadButton = (Text) root.lookup("#loadButton");  // [P]
        exitButton = (Text) root.lookup("#exitButton");   // [P]
        continueButton = (Text) root.lookup("#continueButton");  // [P]
    }
}
