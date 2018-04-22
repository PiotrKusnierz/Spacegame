package game;

import game.views.MenuView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class MenuController extends Application {
    public MenuView menuView;
    public Button startGame;


    public void startGame(javafx.event.ActionEvent actionEvent) throws Exception {
        System.out.println("yo");
        Parent game_page_parent = FXMLLoader.load(getClass().getResource("GameInterface.fxml"));
        Scene game_page_scene = new Scene(game_page_parent);
        Stage game_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        game_stage.setScene(game_page_scene);
        game_stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = FXMLLoader.load(this.getClass().getResource("MenuInterface.fxml"));
        menuView = new MenuView(root);
        stage.setScene(new Scene(root, Color.YELLOW));
        stage.show();
        stage.setTitle("SPACESHIT");
    }
}
