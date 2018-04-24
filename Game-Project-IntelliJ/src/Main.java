import javafx.application.Application;
import game.GameController;

public class Main {
    public static void main(String[] args) {
        /*System.out.println(System.getProperty("java.class.path"));*/
        // This line fixes a bug with linux, which removes the 60fps cap of AnimationTimer.
        System.setProperty("quantum.multithreaded", "false");
        Application.launch(GameController.class, args);
    }
}
