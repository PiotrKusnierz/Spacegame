package game.views;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import game.models.Player;

public class PlayerView {
    public Color color;
    private GraphicsContext gc;

    public PlayerView(GraphicsContext gc) {
        this.gc = gc;
        this.color = Color.MAGENTA;
    }

    public void draw(Player player) {
        gc.setFill(this.color);
        gc.fillRect(
            player.rect.x, player.rect.y,
            player.rect.w, player.rect.w
        );
    }
}
