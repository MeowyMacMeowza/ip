package korvus;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class UI {
    private Scanner inputScanner;
    private PrintStream outputStream;
    private int maxLength;

    public UI(InputStream inStream, PrintStream outStream, int maxLength) {
        this.inputScanner = new Scanner(inStream);
        this.outputStream = outStream;
        this.maxLength = maxLength;
    }

    public UI(InputStream inStream, PrintStream outStream) {
        this(inStream, outStream, 70);
    }

    public String listen() {
        return inputScanner.nextLine().trim();
    }

    public int setMaxLength(int length) {
        int oldLength = this.maxLength;
        this.maxLength = length;
        return oldLength;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

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

    public void rawPrint(String s) {
        outputStream.println(s);
    }

    public void divider() {
        StringBuilder divider = new StringBuilder();
        divider.repeat("_", maxLength);

        outputStream.println(divider);
    }
}
