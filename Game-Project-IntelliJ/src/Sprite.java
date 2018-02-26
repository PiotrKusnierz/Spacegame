import com.sun.javafx.css.Size;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public abstract class Sprite {

    public Point2D position;
    public Point2D velocity;

    private Image image;
    private double[] size;
    private int lives;

    public Sprite(Image img, double[] size, int lives){
        this.image = img;
        this.size = size;
        this.lives = lives;
    }

    abstract void draw();
    abstract void update();

    public int getLives(){
       return 3;
    }

    public boolean isAlive(){
        for (Enemy enemy : enemies) {
            if (player.position == enemy.position) {
                return false;
            }
        }
        return true;
    }

    public Size getSize(){
        return HUGE;
    }

    public void die(){
        if(lives<1){
            initiateDeathAnimation();
        }
    }

    public void hit(){
        if (!isAlive()){
            lives--;
        }
    }



}
