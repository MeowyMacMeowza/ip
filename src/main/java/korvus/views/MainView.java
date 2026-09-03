package korvus.views;

import javafx.css.Stylesheet;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import korvus.bot.AppKorvus;
import korvus.views.components.ChatBox;
import korvus.views.components.ChatMessage;

public class MainView extends AnchorPane {
    private AppKorvus korvus;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/userImage.gif"));
    private Image korvusImage = new Image(this.getClass().getResourceAsStream("/images/botImage.jpg"), 120, 120, true, true);

    private ChatBox chatBox;
    private TextField userInput;
    private Button sendButton;

    public MainView(AppKorvus korvus) {
        this.korvus = korvus;

        chatBox = new ChatBox();
        chatBox.setFitToWidth(true);
        AnchorPane.setTopAnchor(chatBox, 10.0);
        AnchorPane.setLeftAnchor(chatBox, 10.0);
        AnchorPane.setRightAnchor(chatBox, 10.0);

        userInput = new TextField();
        userInput.setPromptText("Send a message here!");
        userInput.setOnAction(e -> handleUserMessage());
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setLeftAnchor(userInput, 10.0);

        sendButton = new Button("Send");
        sendButton.setOnAction(e -> handleUserMessage());
        AnchorPane.setBottomAnchor(sendButton, 10.0);
        AnchorPane.setRightAnchor(sendButton, 10.0);

        chatBox.prefHeightProperty().bind(this.heightProperty().subtract(userInput.heightProperty()).subtract(30));
        userInput.prefWidthProperty().bind(this.widthProperty().subtract(sendButton.widthProperty()).subtract(30));
        sendButton.prefHeightProperty().bindBidirectional(userInput.prefHeightProperty());

        String mainCss = this.getClass().getResource("/css/main.css").toExternalForm();
        this.getStylesheets().add(mainCss);
        this.getChildren().addAll(chatBox, userInput, sendButton);
    }

    private void handleUserMessage() {
        chatBox.addMessage(new ChatMessage(false, userImage, userInput.getText()));
        this.korvus.readUserMessage(userInput.getText());

        userInput.clear();
    }

    public void handleKorvusMessage(String msg) {
        chatBox.addMessage(new ChatMessage(true, korvusImage, msg));
    }
}
