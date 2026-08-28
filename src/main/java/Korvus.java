import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

public class Korvus {
    private static String banner =
            """
              /|
             / | _ ____--___   ___   _  ____
             | |/ |    |  __| / / | | |/ __/
             |   <| [] | | \\ ' /| |_| |\\__ \\
             |_/\\_|____|_|  \\_/  \\__,_|\\___/
            """;

    private UI ui;
    private DateTimeParser dateTimeParser;
    private Tasklist tasklist;
    private StorageParser<Tasklist> tasklistParser;
    private Storage storage;
    private Config config;
    private boolean isActive;

    private Korvus() {
        this(System.in, System.out, "");
    }

    private Korvus(InputStream input, PrintStream output, String storagePath) {
        this.ui = new UI(input, output);
        this.storage = new Storage(storagePath);
    }

    public static void main(String[] args) {
        Korvus bot = new Korvus(System.in, System.out, "");
        bot.start();
    }

    private void start() {
        if(isActive) throw new RuntimeException("Already Running!");
        else isActive = true;

        ui.say("Loading config file...");
        try {
            this.config = this.storage.readConfigFile();
            ui.say("Loaded config file!");
        } catch (FileNotFoundException e) {
            this.config = Config.generateNewConfig();
            ui.say("Failed to find config file, using default configurations~");
        }

        // Reading config file
        this.dateTimeParser = new DateTimeParser(
                DateTimeFormatter.ofPattern(this.config.getValue("datetime_format"))
        );
        this.ui.setMaxLength(Integer.parseInt(this.config.getValue("commandline_length")));

        // Adding Parsers to Storage
        try {
            this.tasklistParser = new StorageParser<Tasklist>(
                    this.storage,
                    this.config.getValue("tasklist_file_path")
            );
        } catch (StorageConflictException e) {
            ui.say(String.format("""
                    Warning: Failed to connect parser to tasklist!
                    Tasklist will be empty and cannot be saved to storage.
                    Error: %s""", e.getMessage()));
        }

        // Initialise Tasklist
        this.tasklist = new Tasklist(this.dateTimeParser);

        // Read from files
        try {
            boolean hasErrors = this.tasklist.readFromParser(this.tasklistParser);
            if(hasErrors) {
                ui.say("Warning: Failed to read some tasks! Tasklist may be missing tasks.");
            }
        } catch (IOException e) {
            ui.say("Warning: Failed tasklist file! Tasklist will be empty.");
        }
        ui.divider();

        this.greet();
        while(isActive) {
            String userReply = ui.listen();

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
                    ui.say(userReply.isEmpty() ? "Caw~" : userReply +"~");
                    ui.divider();
                }
            }
        }
    }

    private void greet() {
        ui.divider();
        ui.rawPrint(banner);
        ui.say("Nice to meet you!");
        ui.say("I am caw-lled Korvus, your personal chatbot for keeping track of shiny things.");
        ui.say("To get a list of cawmands, tweet 'help'!");
        ui.divider();
    }

    private void help() {
        ui.say("Here are a list of cawmands!\nFor any invalid cawmands, I will simply parrot them back~\n");
        ui.say("list[s], task[s] - View your tasks.");
        ui.say("""
                add task <task> - Adds a task with name <task>.
                Use the flags -t for a ToDo, -d for a Deadline and -e for an Event.
                 -t <task> : Adds a ToDo Task.
                 -d <task> // <deadline> : Adds a Deadline Task with an (optional) deadline.
                 -e <task> // <start> // <end> : Adds an Event Task with (optional) duration.""");
        ui.say("add todo <task> - Adds a ToDo Task.");
        ui.say("add deadline <task> // <deadline> - Adds a Deadline Task with an (optional) deadline.");
        ui.say("add event <task> // <start> // <end> - Adds an Event Task with (optional) duration.");
        ui.say("""
                do task <q_task> - Marks task with info <q_task> as done.
                
                <q_task> is first assumed to be the task id, but if invalid then assumed to be task name.
                If there are duplicate tasks with the same name, it will only use the first one.""");
        ui.say("""
                undo task <q_task> - Marks task with info <q_task> as not done.
                
                <q_task> is first assumed to be the task id, but if invalid then assumed to be task name.
                If there are duplicate tasks with the same name, it will only use the first one.""");
        ui.say("""
                delete task <q_task> - Removes task with info <q_task> from the tasklist.
                
                <q_task> is first assumed to be the task id, but if invalid then assumed to be task name.
                If there are duplicate tasks with the same name, it will only use the first one.""");
        ui.say("bye - Closes the program (goodbye...)");
        ui.say("help - Hi there! I'm here to help!");
        ui.divider();
    }

    private void goodbye(boolean isForced) {
        ui.say("Saving session information to disk...");

        // Only runs when user says bye
        isActive = false;
        try {
            if(tasklistParser != null) {
                tasklistParser.writeStorage(tasklist);
            }
            storage.saveConfigFile(config);
        } catch (IOException e) {
            if(!isForced) {
                ui.say(String.format("Failed to save some files!\n%s", e.getMessage()));
                ui.say("Aborting exit... If you want to force exit, tweet \"goodbye -f\".");
                return;
            }
        }

        ui.say("Goodbye! Eagle to see you again!");
        ui.divider();
    }

    private void addTask(String task) {
        try {
            String newTask = tasklist.addTask(task);
            ui.say(String.format("Added task:\n%d. %s", tasklist.getSize(), newTask));
            ui.divider();
        } catch(InvalidTaskException e) {
            ui.say("An error occurred while creating task!");
            ui.say(e.getMessage());
            ui.divider();
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
            ui.say(String.format("Success! Task has been marked done!\n%s", taskString));
        } catch (InvalidTaskException e) {
            ui.say(String.format("Oh no... Failed to mark task: %s", e.getMessage()));
        }
        ui.divider();
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
            ui.say(String.format("Success! Task has been unmarked!\n%s", taskString));
        } catch (InvalidTaskException e) {
            ui.say(String.format("Oh no... Failed to unmark task: %s", e.getMessage()));
        }
        ui.divider();
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
            ui.say(String.format("""
                    Success! Task (%s) has been deleted!
                    Take note that the other tasks may have new indexes now.
                    Do tweet "list" or "task" to view your updated tasklist.""", taskString));
        } catch (InvalidTaskException e) {
            ui.say(String.format("Oh no... Failed to delete task: %s", e.getMessage()));
        }
        ui.divider();
    }

    private void printTasks() {
        if(tasklist.getSize() == 0) {
            ui.say("You have no tasks! Caw-ngratulations!");
            return;
        }
        ui.say("Here are your tasks!");
        ui.say(tasklist.toString());
        ui.divider();
    }
}
