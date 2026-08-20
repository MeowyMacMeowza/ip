import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Korvus {
    private static int MAX_LENGTH = 60;
    private static String banner =
            """
              /|
             / | _ ____--___   ___   _  ____
             | |/ |    |  __| / / | | |/ __/
             |   <| [] | | \\ ' /| |_| |\\__ \\
             |_/\\_|____|_|  \\_/  \\__,_|\\___/
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
                case String s when s.matches("(good*)*bye") -> {
                    goodbye();
                }
                case "help" -> {
                    help();
                    divider();
                }
                case String s when s.matches("(task(s)*)*(list(s)*)*") -> {
                    printTasks();
                    divider();
                }
                // Add Task
                case String s when s.matches("add task .*") -> {
                    String sTask = s.split("add task ",2)[1];
                    addTask(sTask);
                }
                // Do Task
                case String s when s.matches("do(ne)? task .*") -> {
                    String sTask = s.split("do(ne)? task ",2)[1];
                    try {
                        doTask(Integer.parseInt(sTask) - 1);
                    } catch (Exception e) {
                        doTask(sTask);
                    }
                }
                // Undo Task
                case String s when s.matches("undo(ne)? task .*") -> {
                    String sTask = s.split("undo(ne)? task ",2)[1];
                    try {
                        undoTask(Integer.parseInt(sTask) - 1);
                    } catch (Exception e) {
                        undoTask(sTask);
                    }
                }
                default -> {
                    say(userReply);
                    divider();
                }
            }
        }
    }

    private void greet() {
        divider();
        botOutput.println();
        botOutput.println(banner);
        say("Nice to meet you!");
        say("I am caw-lled Korvus, your personal chatbot for keeping track of shiny things.");
        say("To a get a list of cawmands, tweet 'help'!");
        divider();
    }

    private void help() {
        say("Here are a list of cawmands!");
        say("list[s], task[s] - View your tasks");
        say("add task <task> - Adds a task with name <task>");
        say("do task <q_task> - Marks task with info <q_task> as done. <q_task> is first assumed to be the task id, but if invalid then assumed to be task name. If there are duplicate tasks with the same name, it will only use the first one.");
        say("undo task <q_task> - Marks task with info <q_task> as not done. <q_task> is first assumed to be the task id, but if invalid then assumed to be task name. If there are duplicate tasks with the same name, it will only use the first one.");
        say("bye - Closes the program (goodbye...)");
        say("help - Hi! I'm here to help!");
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

    private void addTask(String task) {
        say(String.format("Added task: %s", task));
        tasklist.add(new Task(task));
        divider();
    }

    // Tries to do task given a name
    private void doTask(String sTask) {
        int id = -1;
        for (int i = 0; i < tasklist.size(); i++) {
            if(!tasklist.get(i).getName().equals(sTask)) continue;
            id = i;
            break;
        }

        //Failed to find task
        if(id == -1) {
            say(String.format("Oh no... Failed to mark task: Task cannot be found.\nName: %s",sTask));
            divider();
        } else{
            doTask(id);
        }
    }

    // Tries to do task given a (valid) id
    private void doTask(int id) {
        boolean status = tasklist.get(id).doTask();
        if(status) {
            say(String.format("Success! Task has been marked done!\n%s",tasklist.get(id)));
        } else {
            say(String.format("Oh no... Failed to mark task: Task has already been done\n%s",tasklist.get(id)));
        }
        divider();
    }

    // Tries to do task given a name
    private void undoTask(String sTask) {
        int id = -1;
        for (int i = 0; i < tasklist.size(); i++) {
            if(!tasklist.get(i).getName().equals(sTask)) continue;
            id = i;
            break;
        }

        //Failed to find task
        if(id == -1) {
            say(String.format("Oh no... Failed to unmark task: Task cannot be found.\nName: %s",sTask));
            divider();
        } else{
            undoTask(id);
        }
    }

    // Tries to do task given a (valid) id
    private void undoTask(int id) {
        boolean status = tasklist.get(id).undoTask();
        if(status) {
            say(String.format("Success! Task has been unmarked!\n%s",tasklist.get(id)));
        } else {
            say(String.format("Oh no... Failed to unmark task: Task has not been done\n%s",tasklist.get(id)));
        }
        divider();
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
