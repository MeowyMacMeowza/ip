package korvus.views.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ChatBox extends ScrollPane {
    private VBox chatLog;

    public ChatBox() {
        this.chatLog = new VBox();
        this.setContent(chatLog);

        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.vvalueProperty().bind(chatLog.heightProperty());
    }

    public void addMessage(ChatMessage chatMessage) {
        this.chatLog.getChildren().add(chatMessage);

        chatMessage.maxWidthProperty().bind(this.chatLog.widthProperty());
    }
}
