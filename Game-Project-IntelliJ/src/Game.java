import java.util.ArrayList;

public class Game {
    private Player player;
    private ArrayList<Enemy> enemies;
    private Menu menu;
    public boolean paused;


    public Game() {
        player = new Player();
        enemies = new ArrayList<Enemy>();
        menu = new Menu();
        paused = false;
    }

    public void draw() {
        player.draw();
        for (Enemy enemy : enemies) {
            enemy.draw();
        }
    }

    public void update() {
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
        }
    }

    public void saveGame() {
    }

    public void eventHandler() {
    }
}
