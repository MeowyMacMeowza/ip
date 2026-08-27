import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Korvus {
    private static int MAX_LENGTH = 80;
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
    private Tasklist tasklist;
    private StorageParser<Tasklist> tasklistParser;
    private Storage storage;
    private Config config;
    private boolean isActive;

    private Korvus() {
        this(System.in, System.out, "");
    }

    private Korvus(InputStream input, PrintStream output, String storagePath) {
        this.userInput = new Scanner(input);
        this.botOutput = output;
        this.tasklist = new Tasklist();
        this.storage = new Storage(storagePath);
    }

    public static void main(String[] args) {
        Korvus bot = new Korvus(System.in, System.out, "");
        bot.start();
    }

    private void start() {
        if(isActive) throw new RuntimeException("Already Running!");
        else isActive = true;

        say("Loading config file...");
        try {
            this.config = this.storage.readConfigFile();
            say("Loaded config file!");
        } catch (FileNotFoundException e) {
            this.config = Config.generateNewConfig();
            say("Failed to find config file, using default configurations~");
        }

        // Adding Parsers to Storage
        try {
            this.tasklistParser = new StorageParser<Tasklist>(this.storage, this.config.getValue("tasklist_file_path"));
        } catch (StorageConflictException e) {
            say(String.format("""
                    Warning: Failed to connect parser to tasklist!
                    Tasklist will be empty and cannot be saved to storage.
                    Error: %s""", e.getMessage()));
        }

        // Read from files
        try {
            boolean hasErrors = this.tasklist.readFromParser(this.tasklistParser);
            if(hasErrors) {
                say("Warning: Failed to read some tasks! Tasklist may be missing tasks.");
            }
        } catch (IOException e) {
            say("Warning: Failed tasklist file! Tasklist will be empty.");
        }
        divider();

        this.greet();
        while(isActive) {
            String userReply = userInput.nextLine().trim();

            switch (userReply) {
                case String s when s.matches("(good)?bye( -f)?") -> {
                    goodbye(s.matches(".*-f.*"));
                }
                case "help" -> {
                    help();
                }
                case String s when s.matches("(task(s)?)|(list(s)?)") -> {
                    printTasks();
                }
                // Add Task
                case String s when s.matches("add task .*") -> {
                    String sTask = s.split("add task ",2)[1];
                    addTask(sTask);
                }
                // Add Task subclasses
                case String s when s.matches("(add )?todo .*") -> {
                    String sTask = "-t " + s.split("(add )?todo ",2)[1];
                    addTask(sTask);
                }
                case String s when s.matches("(add )?deadline .*") -> {
                    String sTask = "-d " + s.split("(add )?deadline ",2)[1];
                    addTask(sTask);
                }
                case String s when s.matches("(add )?event .*") -> {
                    String sTask = "-e " + s.split("(add )?event ",2)[1];
                    addTask(sTask);
                }
                // Do Task
                case String s when s.matches("do(ne)? task .*") -> {
                    String sTask = s.split("do(ne)? task ",2)[1];
                    doTask(sTask);
                }
                // Undo Task
                case String s when s.matches("undo(ne)? task .*") -> {
                    String sTask = s.split("undo(ne)? task ",2)[1];
                    undoTask(sTask);
                }
                // Delete Task
                case String s when s.matches("del(ete)? task .*") -> {
                    String sTask = s.split("del(ete)? task ",2)[1];
                    deleteTask(sTask);
                }
                default -> {
                    say(userReply.isEmpty() ? "Caw~" : userReply +"~");
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
        say("To get a list of cawmands, tweet 'help'!");
        divider();
    }

    private void help() {
        say("Here are a list of cawmands!\nFor any invalid cawmands, I will simply parrot them back~\n");
        say("list[s], task[s] - View your tasks.");
        say("""
                add task <task> - Adds a task with name <task>.
                Use the flags -t for a ToDo, -d for a Deadline and -e for an Event.
                 -t <task> : Adds a ToDo Task.
                 -d <task> // <deadline> : Adds a Deadline Task with an (optional) deadline.
                 -e <task> // <start> // <end> : Adds an Event Task with (optional) duration.""");
        say("add todo <task> - Adds a ToDo Task.");
        say("add deadline <task> // <deadline> - Adds a Deadline Task with an (optional) deadline.");
        say("add event <task> // <start> // <end> - Adds an Event Task with (optional) duration.");
        say("""
                do task <q_task> - Marks task with info <q_task> as done.
                
                <q_task> is first assumed to be the task id, but if invalid then assumed to be task name.
                If there are duplicate tasks with the same name, it will only use the first one.""");
        say("""
                undo task <q_task> - Marks task with info <q_task> as not done.
                
                <q_task> is first assumed to be the task id, but if invalid then assumed to be task name.
                If there are duplicate tasks with the same name, it will only use the first one.""");
        say("""
                delete task <q_task> - Removes task with info <q_task> from the tasklist.
                
                <q_task> is first assumed to be the task id, but if invalid then assumed to be task name.
                If there are duplicate tasks with the same name, it will only use the first one.""");
        say("bye - Closes the program (goodbye...)");
        say("help - Hi there! I'm here to help!");
        divider();
    }

    private void divider() {
        StringBuilder divider = new StringBuilder();
        divider.repeat("_", MAX_LENGTH);

        botOutput.println(divider);
    }

    private void goodbye(boolean isForced) {
        say("Saving session information to disk...");

        // Only runs when user says bye
        isActive = false;
        try {
            if(tasklistParser != null) {
                tasklistParser.writeStorage(tasklist);
            }
            storage.saveConfigFile(config);
        } catch (IOException e) {
            if(!isForced) {
                say(String.format("Failed to save some files!\n%s", e.getMessage()));
                say("Aborting exit... If you want to force exit, tweet \"goodbye -f\".");
                return;
            }
        }

        say("Goodbye! Eagle to see you again!");
        divider();
    }

    private void addTask(String task) {
        try {
            String newTask = tasklist.addTask(task);
            say(String.format("Added task:\n%d. %s", tasklist.getSize(), newTask));
            divider();
        } catch(InvalidTaskException e) {
            say("An error occurred while creating task!");
            say(e.getMessage());
            divider();
        }
    }

    private void doTask(String sTask) {
        try {
            String taskString;
            if(sTask.matches("\\d+") && Integer.parseInt(sTask) - 1 < tasklist.getSize()) {
                taskString = tasklist.doTask(Integer.parseInt(sTask) - 1);
            } else {
                taskString = tasklist.doTask(sTask);
            }
            say(String.format("Success! Task has been marked done!\n%s", taskString));
        } catch (InvalidTaskException e) {
            say(String.format("Oh no... Failed to mark task: %s", e.getMessage()));
        }
        divider();
    }

    // Tries to undo task given a name
    private void undoTask(String sTask) {
        try {
            String taskString;
            if(sTask.matches("\\d+") && Integer.parseInt(sTask) - 1 < tasklist.getSize()) {
                taskString = tasklist.undoTask(Integer.parseInt(sTask) - 1);
            } else {
                taskString = tasklist.undoTask(sTask);
            }
            say(String.format("Success! Task has been unmarked!\n%s", taskString));
        } catch (InvalidTaskException e) {
            say(String.format("Oh no... Failed to unmark task: %s", e.getMessage()));
        }
        divider();
    }

    // Tries to delete task given a name
    private void deleteTask(String sTask) {
        try {
            String taskString;
            if(sTask.matches("\\d+") && Integer.parseInt(sTask) - 1 < tasklist.getSize()) {
                taskString = tasklist.deleteTask(Integer.parseInt(sTask) - 1);
            } else {
                taskString = tasklist.deleteTask(sTask);
            }
            say(String.format("""
                    Success! Task (%s) has been deleted!
                    Take note that the other tasks may have new indexes now.
                    Do tweet "list" or "task" to view your updated tasklist.""", taskString));
        } catch (InvalidTaskException e) {
            say(String.format("Oh no... Failed to delete task: %s", e.getMessage()));
        }
        divider();
    }

    private void printTasks() {
        if(tasklist.getSize() == 0) {
            say("You have no tasks! Caw-ngratulations!");
            return;
        }
        say("Here are your tasks!");
        say(tasklist.toString());
        divider();
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
