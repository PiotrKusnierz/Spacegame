package game.models;

import java.io.Serializable;
import java.util.List;

/**
 * Class for holding all important game variables (to make the game Serializable).
 */
public class Game implements Serializable {
    public Player player;
    public List<Enemy> enemies;
    public List<BackgroundObject> backgroundPlanet;
    public List<BackgroundObject> backgroundStar;
    public Enemy boss;
    public int score = 0;
    public int frameCounter = 0;
    public int level = 1;
}
