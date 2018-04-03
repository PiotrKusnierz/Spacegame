package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import game.tools.*;

public class GameView {
    private Canvas canvas;
    private GraphicsContext gc;
    private Size canvasSize;
    public PlayerView playerView;
    public EnemyView enemyView;

    public GameView(Size canvasSize) {
        this.canvasSize = canvasSize;
        this.canvas = new Canvas(canvasSize.w, canvasSize.h);
        this.gc = canvas.getGraphicsContext2D();
        this.playerView = new PlayerView(this.gc);
        this.enemyView = new EnemyView(this.gc);
    }

    public void draw() {
        this.gc.clearRect(0, 0, canvasSize.w, canvasSize.h);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
