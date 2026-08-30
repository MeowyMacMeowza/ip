package korvus;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class UI {
    private Scanner inputScanner;
    private PrintStream outputStream;
    private int maxLength;

    /**
     * Returns an instance of UI.
     *
     * @param inStream Input stream that users will input from.
     * @param outStream Output stream that the UI should print to.
     * @param maxLength Max length of characters in the outputStream.
     */
    public UI(InputStream inStream, PrintStream outStream, int maxLength) {
        this.inputScanner = new Scanner(inStream);
        this.outputStream = outStream;
        this.maxLength = maxLength;
    }

    /**
     * Returns an instance of UI.
     *
     * @param inStream Input stream that users will input from.
     * @param outStream Output stream that the UI should print to.
     */
    public UI(InputStream inStream, PrintStream outStream) {
        this(inStream, outStream, 70);
    }

    /**
     * Returns the user's input. The program pauses until a new input is detected.
     *
     * @return User's input into the input stream.
     */
    public String listen() {
        return inputScanner.nextLine().trim();
    }

    /**
     * Sets the max length of the output.
     *
     * @param length New length to be updated
     * @return The previous max length used.
     */
    public int setMaxLength(int length) {
        int oldLength = this.maxLength;
        this.maxLength = length;
        return oldLength;
    }

    /**
     * Returns the max length of the output.
     *
     * @return The max length of the output.
     */
    public int getMaxLength() {
        return this.maxLength;
    }

    /**
     * Sends the provided message into the output stream.
     * Custom formatting is used to make the output more readable.
     *
     * @param msg Message to be outputted.
     */
    public void say(String msg) {
        if(msg.isEmpty()) {
            System.out.println("> Caw");
            return;
        }

        int lastSpace = -1, lastLine = -1;
        StringBuilder newText = new StringBuilder("> ");

        for (int i = 0; i < msg.length(); i++) {
            if(msg.charAt(i) == ' ') {
                lastSpace = i;
            }

            if(msg.charAt(i) == '\n') {
                newText.append(msg, lastLine + 1, i);
                newText.append("\n  ");

                lastLine = i;
            }

            //End of line
            else if(i - lastLine > maxLength - 2) {
                if(lastSpace > lastLine) {
                    newText.append(msg, lastLine + 1, lastSpace);
                    newText.append("\n  ");

                    lastLine = lastSpace;
                } else {
                    newText.append(msg, lastLine + 1, lastLine + maxLength - 1);
                    newText.append("\n  ");

                    lastLine += maxLength - 2;
                }
            }
        }
        newText.append(msg.substring(lastLine + 1));
        outputStream.println(newText);
    }

    /**
     * Prints the provided message into the output stream with no formatting
     *
     * @param s Message to be outputted.
     */
    public void rawPrint(String s) {
        outputStream.println(s);
    }

    public void divider() {
        StringBuilder divider = new StringBuilder();
        divider.repeat("_", maxLength);

        outputStream.println(divider);
    }
}
