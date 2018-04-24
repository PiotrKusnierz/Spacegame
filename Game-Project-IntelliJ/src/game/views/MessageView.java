package game.views;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
* Class for creating the animated message for user inside a HBox which is placed in the middle.
* Here FadeTransition is used for text animation.
*/

public class MessageView {

    public Pane root;
    public HBox view = new HBox();

    public MessageView(Pane root) {
        this.root = root;
    }

	// [S] made this one private, and allowing for a persistant message or one
	// that automatically disappears.
	private void animatedMessage(String msg, boolean persistant) {
		root.getChildren().add(view);

		for (int i = 0; i < msg.toCharArray().length; i++) {   //toCharArray() converts string into character array
			char letter = msg.charAt(i);	   // charAt(int index) returns the character at the specified index

			Text text = new Text(String.valueOf(letter));
			text.setFont(Font.font(48));
			text.setOpacity(0);
			text.setFill(Color.WHITE);

			view.getChildren().add(text);

			//FadeTransition creates a fade effect animation
			FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
			ft.setToValue(1);
			if (!persistant) {
				ft.setCycleCount(2);
				ft.setAutoReverse(true);
			}
			ft.setDelay(Duration.seconds(i * 0.05));
			ft.play();
			// FadeTransition ft2 = new FadeTransition(Duration.seconds(0.66), text);
			// ft2.setToValue(0);
			// ft2.setDelay(Duration.seconds(i * 0.05));
			// ft2.play();
		}
		view.autosize();
		view.setTranslateX(root.getWidth()/2-view.getWidth()/2);
		view.setTranslateY(root.getHeight()/2-view.getHeight()*1.5);
	}

    public void showPeristantAnimatedMessage(String msg) {
		animatedMessage(msg, true);
	}

	public void showAnimatedMessage(String msg) {
		animatedMessage(msg, false);
	}

    public void removeMessage() {
        root.getChildren().remove(view);
        view.getChildren().clear();
    }
}
