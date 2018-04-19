package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import game.tools.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
* Class for creating the canvas (aka "drawing board") of the game.
* Middle-man class between GameController and Player- and EnemyView
*/
public class GameView {
    private Canvas canvas;
    //public ImageView menuImageView;
    private GraphicsContext gc;
    private Size canvasSize;
    public PlayerView playerView;
    public EnemyView enemyView;
    public Text lives;
    public Text score;

    public GameView(Size canvasSize, Pane root) {
        canvas = (Canvas) root.lookup("#canvas");
       // menuImageView = (ImageView) root.lookup("#menuImgView");
        lives = (Text) root.lookup("#lives");
        score = (Text) root.lookup("#score");
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
