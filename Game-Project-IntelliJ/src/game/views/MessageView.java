package game.views;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Class for creating the animated message for user inside a HBox which is
 * placed in the center of the window.
 * FadeTransition is used for text animation.
 */
public class MessageView {
    public Pane root;
    public HBox view = new HBox();
    public MessageView(Pane root) {
        this.root = root;
    }

    /**
     * Shows an animated message on the game window.
     * @param msg The message to be shown.
     * @param persistant Sets the message to be persistant or not with a boolean value.
     */
    private void animatedMessage(String msg, boolean persistant) {
		if (root.getChildren().contains(view)) {
			root.getChildren().remove(view);
			view.getChildren().clear();
		}
        root.getChildren().add(view);

        for (int i = 0; i < msg.length(); i++) {
            char letter = msg.charAt(i);

            Text text = new Text(String.valueOf(letter));
            text.setFont(Font.font(48));
            text.setOpacity(0);
            text.setFill(Color.WHITE);

            view.getChildren().add(text);

            FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
            ft.setToValue(1);
            if (!persistant) {
                ft.setCycleCount(2);
                ft.setAutoReverse(true);
				if (i == msg.length()-1) {
					ft.setOnFinished(event -> {
						root.getChildren().remove(view);
						view.getChildren().clear();
					});
				}
            }
            ft.setDelay(Duration.seconds(i * 0.05));
            ft.play();
        }
        view.autosize();
        view.setTranslateX(root.getWidth()/2-view.getWidth()/2);
        view.setTranslateY(root.getHeight()/2-view.getHeight()*2.1);
    }

    /**
     * Shows a persistant animated message.
     * @param msg The message to be shown.
     */
    public void showPeristantAnimatedMessage(String msg) {
        animatedMessage(msg, true);
    }

    /**
    * Shows an animates message.
    * @param msg The message to be shown.
    */
    public void showAnimatedMessage(String msg) {
        animatedMessage(msg, false);
    }

    /**
    * Removes message.
    */
    public void removeMessage() {
        root.getChildren().remove(view);
        view.getChildren().clear();
    }
}
