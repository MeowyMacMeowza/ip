package korvus.views.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * ChatBox class for to contain the chat messages for the GUI.
 */
public class ChatBox extends ScrollPane {
    private VBox chatLog;

    /**
     * Returns a ChatBox to be displayed in the App GUI.
     */
    public ChatBox() {
        this.chatLog = new VBox();

        this.setContent(chatLog);
        this.setUpScrollPane();
    }

    /**
     * Initialises the ChatBox to the correct parameters.
     */
    private void setUpScrollPane() {
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.vvalueProperty().bind(chatLog.heightProperty());
        this.setFitToWidth(true);
    }

    /**
     * Adds a new ChatMessage to the ChatBox.
     *
     * @param chatMessage ChatMessage to be added.
     */
    public void addMessage(ChatMessage chatMessage) {
        this.chatLog.getChildren().add(chatMessage);

        chatMessage.maxWidthProperty().bind(this.chatLog.widthProperty());
    }
}
