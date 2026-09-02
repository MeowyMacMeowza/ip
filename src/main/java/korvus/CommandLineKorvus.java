package korvus;

import java.io.InputStream;
import java.io.PrintStream;

import korvus.ui.CommandLineUI;

/**
 * hi.
 */
public class CommandLineKorvus extends Korvus {
    /**
     * Returns a default instance of the Korvus object.
     */
    public CommandLineKorvus() {
        this(System.in, System.out, "");
    }

    /**
     * Returns an instance of the Korvus object, with the provided parameters.
     *
     * @param input Input Stream that user inputs from.
     * @param output Output Stream that Korvus writes to.
     * @param storagePath Relative path to the directory with files for Korvus to use.
     */
    public CommandLineKorvus(InputStream input, PrintStream output, String storagePath) {
        super(new CommandLineUI(input, output), storagePath);
    }

    //CHECKSTYLE.OFF: SeparatorWrap
    @Override
    public void initialise() {
        super.initialise();
        ((CommandLineUI) ui).setMaxLength(Integer.parseInt(this.config.getValue("commandline_length")));

        while (isActive) {
            String userReply = ((CommandLineUI) ui).listen();
            readUserMessage(userReply);
        }
    }

    @Override
    public void readUserMessage(String msg) {
        super.readUserMessage(msg);
        ui.divider();
    }

    //CHECKSTYLE.ON: SeparatorWrap
}
