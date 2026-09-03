package korvus.bot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import korvus.ui.AppUI;

public class AppKorvus extends Korvus {

    /**
     * Returns an instance of the Korvus object, with the provided parameters.
     *
     * @param ui UI tagged to the Korvus instance.
     * @param storagePath Relative path to the directory with files for Korvus to use.
     */
    public AppKorvus(AppUI ui, String storagePath) {
        super(ui, storagePath);
    }

    @Override
    public void readUserMessage(String msg) {
        if (!this.isActive) {
            return;
        }

        super.readUserMessage(msg);

        if (!this.isActive) {
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(800), (t) -> Platform.exit()));
            tl.play();
        }
    }
}
