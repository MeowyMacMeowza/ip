package korvus.views.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ChatMessage extends HBox {
    private Label messageLabel;
    private ImageView displayImage;
    private StackPane displayFrame;
    private Circle imageBorder;

    public ChatMessage(boolean isBot, Image image, String message) {
        displayImage = new ImageView(image);
        displayImage.setPreserveRatio(true);

        messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.prefHeightProperty().bindBidirectional(this.prefHeightProperty());

        imageBorder = new Circle();
        imageBorder.setFill(Color.TRANSPARENT);
        imageBorder.setStroke(Color.VIOLET);
        imageBorder.setStrokeWidth(2);

        createDisplayPicture(60);

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        if (isBot) { // flip
            messageLabel.getStyleClass().add("korvus-label");
            this.getChildren().addAll(displayFrame, messageLabel);
            this.setAlignment(Pos.CENTER_LEFT);
        } else {
            messageLabel.getStyleClass().add("user-label");
            this.getChildren().addAll(messageLabel, displayFrame);
            this.setAlignment(Pos.CENTER_RIGHT);
        }

        String messageCss = this.getClass().getResource("/css/chatmessage.css").toExternalForm();
        this.getStylesheets().add(messageCss);
    }

    private void createDisplayPicture(int size) {
        double halfSize = size / 2.0;
        displayFrame = new StackPane();

        imageBorder.setRadius(halfSize);
        imageBorder.setCenterY(halfSize);
        imageBorder.setCenterX(halfSize);

        displayImage.setClip(new Circle(halfSize, halfSize, halfSize));
        displayImage.setFitHeight(size);

        displayFrame.getChildren().addAll(displayImage, imageBorder);
    }
}
