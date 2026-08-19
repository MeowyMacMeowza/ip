import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Korvus {
    private static int MAX_LENGTH = 60;
    private static String banner =
            """
             ,_
             | | _ ____--___   ___   _  ____
             | |/ |    |  __| / / | | |/ __/
             |   <| [] | | \\ ' /| |_| |\\__ \\
             |_|\\_|____|_|  \\_/  \\__,_|\\___/
            """;

    private Scanner userInput;
    private PrintStream botOutput;

    private Korvus(InputStream input, PrintStream output) {
        this.userInput = new Scanner(input);
        this.botOutput = output;
    }

    public static void main(String[] args) {
        Korvus bot = new Korvus(System.in, System.out);

        bot.greet();
        bot.start();
    }

    private void start() {
        while(true) {
            String userReply = userInput.nextLine().trim();

            switch (userReply) {
                case "bye":
                    goodbye();
                default:
                    say(userReply);
                    divider();
            }
        }
    }

    private void greet() {
        divider();
        botOutput.println(banner);
        say("Nice to meet you!");
        say("I am caw-lled Korvus, your personal chatbot for keeping track of shiny things.");
        divider();
    }

    private void divider() {
        StringBuilder divider = new StringBuilder();
        divider.repeat("_", MAX_LENGTH);

        botOutput.println(divider);
    }

    private void goodbye() {
        say("Goodbye! Eagle to see you again!");
        divider();

        // Only runs when user says bye
        System.exit(0);
    }

    // For formatting
    private void say(String text) {
        int lastSpace = -1, lastLine = -1;
        StringBuilder newText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == ' ') {
                lastSpace = i;
            }

            //End of line
            if(i - lastLine > MAX_LENGTH - 2) {
                if(newText.isEmpty()) newText.append("> ");
                else newText.append("  ");

                if(lastSpace > lastLine) {
                    newText.append(text.substring(lastLine + 1, lastSpace));
                    newText.append("\n");

                    lastLine = lastSpace;
                } else {
                    newText.append(text.substring(lastLine + 1, lastLine + MAX_LENGTH - 1));
                    newText.append("\n");

                    lastLine += MAX_LENGTH - 2;
                }
            }
        }

        if(newText.isEmpty()) newText.append("> ");
        else newText.append("  ");
        newText.append(text.substring(lastLine + 1));

        botOutput.println(newText);
    }
}
