package korvus;

import javafx.application.Application;
import korvus.bot.CommandLineKorvus;
import korvus.ui.AppUI;

/**
 * Main Class that gets ran.
 */
public class Launcher {
    /**
     * Creates the Korvus bot and starts it.
     */
    public static void main(String... args) {
        if (isCli(args)) {
            new CommandLineKorvus(System.in, System.out, "data/").initialise();
        } else {
            Application.launch(AppUI.class, args);
        }
    }

    private static boolean isCli(String... args) {
        boolean isCli = false;

        for (String arg : args) {
            if (arg == "-cli") {
                isCli = true;
            }
        }

        return isCli;
    }
}
