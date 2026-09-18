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

/**
 * ChatMessage class to contain the user and bot messages for the GUI.
 */
public class ChatMessage extends HBox {
    private static int DEFAULT_DISPLAY_SIZE = 60;
    private static int DEFAULT_DISPLAY_BORDER = 2;
    private static int DEFAULT_SPACING = 8;

    private Label messageLabel;
    private ImageView displayImage;
    private StackPane displayFrame;
    private Circle imageBorder;
    private boolean isBot;

    /**
     * Returns a ChatMessage with the given message.
     *
     * @param isBot Whether the message is from the bot.
     * @param image Display Image of the message owner.
     * @param message Content to be written.
     */
    public ChatMessage(boolean isBot, Image image, String message) {
        this.isBot = isBot;

        createStyling();
        createDisplayBorder();
        createDisplayPicture(image, DEFAULT_DISPLAY_SIZE);

        writeMessage(message);
        generateMessage(isBot);
    }

    private void writeMessage(String message) {
        messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.prefHeightProperty().bindBidirectional(this.prefHeightProperty());
    }

    private void createDisplayBorder() {
        imageBorder = new Circle();
        imageBorder.setFill(Color.TRANSPARENT);
        imageBorder.setStroke(Color.VIOLET);
        imageBorder.setStrokeWidth(DEFAULT_DISPLAY_BORDER);
    }

    private void createDisplayPicture(Image image, int size) {
        displayImage = new ImageView(image);
        displayImage.setPreserveRatio(true);

        double halfSize = size / 2.0;
        displayFrame = new StackPane();

        imageBorder.setRadius(halfSize);
        imageBorder.setCenterY(halfSize);
        imageBorder.setCenterX(halfSize);

        Circle backgroundCircle = new Circle(halfSize, halfSize, halfSize, Color.WHITESMOKE);

        displayImage.setClip(new Circle(halfSize, halfSize, halfSize));
        displayImage.setFitHeight(size);

        displayFrame.getStyleClass().add("display-picture");
        displayFrame.getChildren().addAll(backgroundCircle, displayImage, imageBorder);
    }

    private void generateMessage(boolean isBot) {
        if (isBot) {
            messageLabel.getStyleClass().add("korvus-label");
            this.getChildren().addAll(displayFrame, messageLabel);
            this.setAlignment(Pos.CENTER_LEFT);
        } else {
            messageLabel.getStyleClass().add("user-label");
            this.getChildren().addAll(messageLabel, displayFrame);
            this.setAlignment(Pos.CENTER_RIGHT);
        }
    }

    private void createStyling() {
        this.setPadding(new Insets(DEFAULT_SPACING));
        this.setSpacing(DEFAULT_SPACING);

        String messageCss = this.getClass().getResource("/css/chatmessage.css").toExternalForm();
        this.getStylesheets().add(messageCss);
    }

    protected boolean isBotMessage() {
        return isBot;
    }
}
