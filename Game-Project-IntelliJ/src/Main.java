import javafx.application.Application;
import game.GameController;

/**
 * This is the Main class, which starts the Application.
 * It turns of quantum.multithreaded, to work on various Linux distributions.
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("quantum.multithreaded", "false");
        Application.launch(GameController.class, args);
    }
}
