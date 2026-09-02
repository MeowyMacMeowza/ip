package korvus;

import javafx.application.Application;
import korvus.ui.AppUI;

public class Launcher {
    /**
     * Creates the Korvus bot and starts it.
     */
    public static void main(String[] args) {
        // Korvus bot = new CommandLineKorvus(System.in, System.out, "data/");
        // bot.initialise();

        Application.launch(AppUI.class, args);
    }
}
