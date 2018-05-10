package game.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import game.tools.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
* Class for creating the canvas (aka "drawing board") of the game.
* Middle-man class between GameController and Player- and EnemyView
*/
public class GameView {
    public Canvas canvas;
    private GraphicsContext gc;
    public PlayerView playerView;

    // NEW ||||||||||||||||||||||||||||||
    public BackgroundObjectView backObjView;

    public EnemyView enemyView;
    public Text lives; // [P]
    public Text score; // [P]

    /**
     * GameView constructor.
     * @param root the main node of the game.
     */
    public GameView(Pane root) {
        canvas = (Canvas) root.lookup("#canvas");
        lives = (Text) root.lookup("#lives");   // [P]
        score = (Text) root.lookup("#score");   // [P]
        this.canvas.setScaleY(-1);
        this.gc = canvas.getGraphicsContext2D();
        this.playerView = new PlayerView(this.gc);

        // NEW |||||||||||||||||||
        this.backObjView = new BackgroundObjectView(this.gc);

        this.enemyView = new EnemyView(this.gc);
    }

    /**
     * Clears the canvas.
     */
    public void clearCanvas() {
        this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
