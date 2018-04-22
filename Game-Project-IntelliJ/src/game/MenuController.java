package game;

import game.views.MenuView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuController extends Application {
    public MenuView menuView;
    public Button startGame;




    public void startGame() {
        //gameState = PLAYING;
        System.out.println("yo");
    }



    @Override
    public void start(Stage stage) throws Exception{
        Pane root = FXMLLoader.load(this.getClass().getResource("MenuInterface.fxml"));
        menuView = new MenuView(root);
        stage.setScene(new Scene(root, Color.YELLOW));
        stage.show();
        stage.setTitle("SPACESHIT");


    }
}
