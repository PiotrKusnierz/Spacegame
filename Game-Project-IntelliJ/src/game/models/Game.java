package game.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import game.tools.*;
import game.models.Player;
import game.models.Enemy;

public class Game implements Serializable {
	public Player player;
	public List<Enemy> enemies;
	public Enemy boss;
	public int score = 0;
	public int frameCounter = 0;
	public int level = 1;

	public void countFrames() {
		frameCounter = (frameCounter + 1);
	}
}
