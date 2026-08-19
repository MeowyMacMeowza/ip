import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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
    private ArrayList<Task> tasklist;

    private Korvus(InputStream input, PrintStream output) {
        this.userInput = new Scanner(input);
        this.botOutput = output;
        this.tasklist = new ArrayList<>(50);
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
                    break;
                case "list", "lists", "task", "tasks":
                    printTasks();
                    divider();
                    break;
                case String s when s.matches("add task .*"):
                    say(String.format("Adding task: %s", s.substring(9)));
                    tasklist.add(new Task(s.substring(9)));
                    divider();
                    break;
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

    private void printTasks() {
        if(tasklist.isEmpty()) {
            say("You have no tasks! Caw-ngratulations!");
            return;
        }
        StringBuilder tasks = new StringBuilder("Here are your tasks!");
        for (int i = 0; i < tasklist.size(); i++) {
            tasks.append(String.format("\n%d. %s", i+1, tasklist.get(i)));
        }
        say(tasks.toString());
    }

    // For formatting
    private void say(String text) {
        if(text.isEmpty()) {
            System.out.println("> Caw");
            return;
        }

        int lastSpace = -1, lastLine = -1;
        StringBuilder newText = new StringBuilder("> ");

        for (int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == ' ') {
                lastSpace = i;
            }

            if(text.charAt(i) == '\n') {
                newText.append(text, lastLine + 1, i);
                newText.append("\n  ");

                lastLine = i;
            }

            //End of line
            else if(i - lastLine > MAX_LENGTH - 2) {
                if(lastSpace > lastLine) {
                    newText.append(text, lastLine + 1, lastSpace);
                    newText.append("\n  ");

                    lastLine = lastSpace;
                } else {
                    newText.append(text, lastLine + 1, lastLine + MAX_LENGTH - 1);
                    newText.append("\n  ");

                    lastLine += MAX_LENGTH - 2;
                }
            }
        }
        newText.append(text.substring(lastLine + 1));
        botOutput.println(newText);
    }
}
