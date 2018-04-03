package game.views;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MessageView {

    public Pane root;
    public HBox view = new HBox();

    public MessageView(Pane root) {
        this.root = root;
    }

    public void showAnimatedMessage(String msg) {
		root.getChildren().add(view);

		for (int i = 0; i < msg.toCharArray().length; i++) {
			char letter = msg.charAt(i);

			Text text = new Text(String.valueOf(letter));
			text.setFont(Font.font(48));
			text.setOpacity(0);
			text.setFill(Color.WHITE);

			view.getChildren().add(text);

			FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
			ft.setToValue(1);
			ft.setDelay(Duration.seconds(i * 0.15));
			ft.play();
		}
		view.autosize();
		view.setTranslateX(root.getWidth()/2-view.getWidth()/2);
		view.setTranslateY(root.getHeight()/2-view.getHeight()/2);
	}

    public void removeMessage() {
        root.getChildren().remove(view);
        view.getChildren().clear();
    }
}
