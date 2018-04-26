package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import game.tools.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
* Class for creating the canvas (aka "drawing board") of the game.
* Middle-man class between GameController and Player- and EnemyView
*/
public class GameView {
    private Canvas canvas;
    private GraphicsContext gc;
    private Size canvasSize;
    public PlayerView playerView;
    public EnemyView enemyView;
    public Text lives; // [P]
    public Text score; // [P]

    public VBox pausedMenuBox; // [P]

    public GameView(Size canvasSize, Pane root) {
        canvas = (Canvas) root.lookup("#canvas");
        // [P]  Retrieves lives, score, pausedMenuBox objects   ??
        lives = (Text) root.lookup("#lives");   // [P]
        score = (Text) root.lookup("#score");   // [P]
        pausedMenuBox = (VBox) root.lookup("#pausedMenuBox");   // [P]


        this.canvasSize = canvasSize;
        this.canvas.setScaleY(-1);
        this.gc = canvas.getGraphicsContext2D();
        this.playerView = new PlayerView(this.gc);
        this.enemyView = new EnemyView(this.gc);
    }

    // Clears the screen / game window visually
    public void clearCanvas() {
        this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /*
    public Canvas getCanvas() {
        return canvas;
    }*/
}
