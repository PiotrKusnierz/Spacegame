import com.sun.glass.ui.Size;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public abstract class Sprite {

    public Point2D position;
    public Point2D velocity;

    private Image image;
    private double[] size;
    private int lives;


    abstract void draw();
    abstract void update();

    public int getLives(){
       return 0;
    }

    public boolean isAlive(){
        return true;
    }

    public Size getSize(){
        return ;
    }




}
