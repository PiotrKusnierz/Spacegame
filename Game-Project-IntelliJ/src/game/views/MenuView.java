package game.views;


import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *  [P] Class holds variables which belongs to the menues.
 */

public class MenuView {
    public VBox pausedMenuBox;   // [P]
    public Text resumeButton;    // [P]
    public Text menuButton;      // [P]
    public Text saveButton;      // [P]
    public Text loadButton;      // [P]
    public Text exitButton;      // [P]
    public Text restartButton;   // [P]
    public Text continueButton;  // [P]
    public VBox messageBox;      // [P]
    public Text yesButton;       // [P]
    public Text noButton;        // [P]

    public MenuView(Pane root) {
        pausedMenuBox = (VBox) root.lookup("#pausedMenuBox");    // [P]
        resumeButton = (Text) root.lookup("#resumeButton");      // [P]
        menuButton = (Text) root.lookup("#menuButton");          // [P]
        saveButton = (Text) root.lookup("#saveButton");          // [P]
        loadButton = (Text) root.lookup("#loadButton");          // [P]
        exitButton = (Text) root.lookup("#exitButton");          // [P]
        restartButton = (Text) root.lookup("#restartButton");    // [P]
        continueButton = (Text) root.lookup("#continueButton");  // [P]
        messageBox = (VBox) root.lookup("#messageBox");          // [P]
        yesButton = (Text) root.lookup("#yesButton");            // [P]
        noButton = (Text) root.lookup("#noButton");              // [P]
    }
}
