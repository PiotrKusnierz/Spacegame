package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import game.tools.*;

public class GameView {
    private Canvas canvas;
    private GraphicsContext gc;
    private Size canvasSize;

    public GameView(Size size) {
    }

    public void GameView(Size canvasSize) {
        this.canvasSize = canvasSize;
        this.canvas = new Canvas(canvasSize.w, canvasSize.h);
        this.gc = canvas.getGraphicsContext2D();
    }

    public void draw() {
        this.gc.clearRect(0, 0, canvasSize.w, canvasSize.h);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
