package game.views;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class holds variables which belongs to the menus.
 * @author Piotr Kusnierz
 */
public class MenuView {
    public VBox pausedMenuBox;
    public VBox mainMenuBox;
    public VBox controlsMenuBox;
    public Text resumeButton;
    public Text menuButton;
    public Text saveButton;
    public Text loadButton;
    public Text restartButton;
    public Text continueButton;
    public Text helpButton;
    public Text goBackButton;
    public VBox messageBox;
    public Text yesButton;
    public Text noButton;

    /**
     * Constructor sets the elements from FXML to the variables.
     * @param root main node of the game.
     */
    public MenuView(Pane root) {
        pausedMenuBox = (VBox) root.lookup("#pausedMenuBox");
        mainMenuBox = (VBox) root.lookup("#mainMenuBox");
        controlsMenuBox = (VBox) root.lookup("#controlsMenuBox");
        resumeButton = (Text) root.lookup("#resumeButton");
        menuButton = (Text) root.lookup("#menuButton");
        saveButton = (Text) root.lookup("#saveButton");
        loadButton = (Text) root.lookup("#loadButton");
        restartButton = (Text) root.lookup("#restartButton");
        continueButton = (Text) root.lookup("#continueButton");
        helpButton = (Text) root.lookup("#helpButton");
        goBackButton = (Text) root.lookup("#goBackButton");
        messageBox = (VBox) root.lookup("#messageBox");
        yesButton = (Text) root.lookup("#yesButton");
        noButton = (Text) root.lookup("#noButton");
    }
}
