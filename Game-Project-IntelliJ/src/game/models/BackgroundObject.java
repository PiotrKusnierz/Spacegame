package game.models;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import game.tools.*;

public class BackgroundObject implements Serializable {

    public Point velocity;
    public Rect rect;
    public double boost = 1;

    /**
     * Enemy constructor which creates an Enemy object with the given position
     * size, and lives.
     * @param x double value of the enemy's x-position.
     * @param y double value of the enemy's y-position.
     * @param w double value of the enemy's width.
     * @param h double value of the enemy's height.
     * @param boost double value of the enemy's boost value (when the player presses the up-arrow).
     */
    public BackgroundObject (double x, double y, double w, double h, double boost) {
        this.rect = new Rect(x, y, w, h);
        this.velocity = new Point(0, -ThreadLocalRandom.current().nextInt(3, 6));
        this.boost = boost;
    }

    public BackgroundObject (double x, double y, double r) {
        this(x, y, r, r, 1);
    }

    public void update() {
        this.rect.x += this.velocity.x;
        this.rect.y += this.velocity.y * this.boost;
    }











    // (ADDED) I UPDATE:
    /*for (Enemy enemy : game.enemies) {
        enemy.update();
    }*/



    /* (ADDED)
    if (Math.random() < 0.02 * boost) {
        switch (game.level) {
            case 1:
                addEnemy(ThreadLocalRandom.current().nextInt(0, 3));
                break;
            case 2:
                addEnemy(ThreadLocalRandom.current().nextInt(3, 5));
                playSound("sound/space2.mp3");
                break;
            case 3:
                addEnemy(ThreadLocalRandom.current().nextInt(5, 7));
                playSound("sound/space2.mp3");
                break;
            case 4:
                addEnemyChildren();
                playSound("sound/19.wav");
                break;
        }
    }*/

    /*
    /** (ADDED)
     * This runs every frame to update the game graphics.
     */
    /*
    public void draw() {
        gameView.clearCanvas();
        gameView.playerView.draw(game.player);
        gameView.enemyView.draw(game.enemies);
    }
    */

    // (ADDED)
    /**
     * Creates a new enemy with a random size and position and adds it to the
     * games enemies list.
     * @param type value for the enemy type which is sent to the Enemy
     * constructor.
     */
    /*
    public void addEnemy(int type) {
        double r = ThreadLocalRandom.current().nextDouble(windowSize.w*0.08, windowSize.w*0.13);
        double x = ThreadLocalRandom.current().nextDouble(0, windowSize.w-r);
        double y = windowSize.h+r;
        Enemy enemy = new Enemy(x, y, r, boost);
        enemy.type = type;
        game.enemies.add(enemy);
    }*/
}

// (ADDED)
// I "public void startGame(MouseEvent mouseEvent) throws Exception{"
// Adde en arraylist med objekter


// HUSKE Å CLEARE I NEW GAME OSV.

// (ADDED)
// OGSÅ CLEARES NÅR DE GÅR UT AV SKJERMEN!!!!!!!

// (ADDED)
// Opprettet array list i Game.java