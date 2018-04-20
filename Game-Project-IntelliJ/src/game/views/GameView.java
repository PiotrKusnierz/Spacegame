package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import game.tools.*;
/**
* Class for creating the canvas (aka "drawing board") of the game.
* Middle-man class between GameController and Player- and EnemyView
*/
public class GameView {
    public Canvas canvas;
    private GraphicsContext gc;
    private Size canvasSize;
    public PlayerView playerView;
    public EnemyView enemyView;
	public Text lives;
	public Text score;

    public GameView(Size canvasSize, Pane root) {
        this.canvas = (Canvas) root.lookup("#canvas");
        this.lives = (Text) root.lookup("#lives");
        this.score = (Text) root.lookup("#score");
        this.canvasSize = canvasSize;
		this.canvas.setScaleY(-1);
        this.gc = canvas.getGraphicsContext2D();
        this.playerView = new PlayerView(this.gc);
        this.enemyView = new EnemyView(this.gc);
    }

    // Clears the screen / game window visually
    public void clearCanvas() {
        this.gc.clearRect(0, 0, canvasSize.w, canvasSize.h);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
