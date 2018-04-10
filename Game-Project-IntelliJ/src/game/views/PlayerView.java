package game.views;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import game.models.Player;

/**
* Class that determines how the player object is displayed in the game window.
* The class does not modify the player object in any way,
* only the way it is displayed.
*/
public class PlayerView {
    public Color color;
    private GraphicsContext gc;

    public PlayerView(GraphicsContext gc) {
        this.gc = gc;
        this.color = Color.MAGENTA;
    }

    // Method draws player object
    public void draw(Player player) {
        gc.setFill(this.color);
        gc.fillRect(
            player.rect.x, player.rect.y,
            player.rect.w, player.rect.h
        );
    }
}
