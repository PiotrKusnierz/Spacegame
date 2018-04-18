package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import game.tools.*;
import javafx.scene.layout.Pane;

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

    public GameView(Size canvasSize, Pane root) {
        this.canvasSize = canvasSize;
        canvas = (Canvas) root.lookup("#canvas");
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
