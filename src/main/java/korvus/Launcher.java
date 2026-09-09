package korvus;

import javafx.application.Application;
import korvus.bot.CommandLineKorvus;
import korvus.ui.AppUI;

public class Launcher {
    /**
     * Creates the Korvus bot and starts it.
     */
    public static void main(String... args) {
        boolean isCli = false;

        for (String arg : args) {
            if (arg.equals("-cli")) {
                isCli = true;
            }
        }

        if (isCli) {
            new CommandLineKorvus(System.in, System.out, "data/").initialise();
        } else {
            Application.launch(AppUI.class, args);
        }
    }
}
