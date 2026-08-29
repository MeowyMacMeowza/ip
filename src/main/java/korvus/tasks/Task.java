package korvus.tasks;

import korvus.utils.DateTimeParser;

public class Task {
    protected enum TaskType {
        TODO("-t "),
        DEADLINE("-d "),
        EVENT("-e ");

        protected String flag;

        TaskType(String str) {
            this.flag = str;
        }
    }

    protected static String DATA_SEP = " // ";

    protected final DateTimeParser dateTimeParser;
    private boolean isDone;
    private String name;

    public Task(String name, DateTimeParser dateTimeParser) throws InvalidTaskException {
        this(false, name, dateTimeParser);
    }

    public Task(boolean isDone, String name, DateTimeParser dateTimeParser) throws InvalidTaskException {
        if(name.startsWith("-")) {
            throw new InvalidTaskException
                    ("korvus.tasks.Task name cannot start with '-'! Are you sure that you typed the name correctly?");
        }
        this.name = name;
        this.isDone = isDone;
        this.dateTimeParser = dateTimeParser;
    }

    public boolean doTask() {
        if(isDone) {
            return false;
        }
        isDone = true;

        return true;
    }

    public boolean undoTask() {
        if(!isDone) {
            return false;
        }
        isDone = false;

        return true;
    }

    public String getName() {
        return name;
    }

    public static Task generateTask(String taskInput, DateTimeParser dateParser) throws InvalidTaskException {
        return switch (taskInput) {
            case String s when s.startsWith(TaskType.TODO.flag) -> {
                yield new ToDo(s.substring(3), dateParser);
            }
            case String s when s.startsWith(TaskType.DEADLINE.flag) -> {
                String[] args = s.substring(3).split(DATA_SEP, 2);
                yield args.length > 1
                        ? new Deadline(args[0], args[1], dateParser)
                        : new Deadline(args[0], dateParser);
            }
            case String s when s.startsWith(TaskType.EVENT.flag) -> {
                String[] args = s.substring(3).split(DATA_SEP, 3);
                yield args.length > 2
                        ? new Event(args[0], args[1], args[2], dateParser)
                        : new Event(args[0], dateParser);
            }
            default -> new ToDo(taskInput, dateParser);
        };
    }

    protected static Task readTaskFromFile(String fileInput, DateTimeParser dateParser) throws InvalidTaskException {
        return switch (fileInput) {
            case String s when s.startsWith(TaskType.TODO.flag) -> {
                String[] args = s.split(DATA_SEP,3);
                yield new ToDo(Boolean.parseBoolean(args[2]), args[1], dateParser);
            }
            case String s when s.startsWith(TaskType.DEADLINE.flag) -> {
                String[] args = s.split(DATA_SEP, 4);
                yield new Deadline(Boolean.parseBoolean(args[2]), args[1], args[3], dateParser);
            }
            case String s when s.startsWith(TaskType.EVENT.flag) -> {
                String[] args = s.split(DATA_SEP, 5);
                yield new Event(Boolean.parseBoolean(args[2]), args[1], args[3], args[4], dateParser);
            }
            default -> {
                throw new InvalidTaskException(String.format("Task format not supported!\nInput: %s", fileInput));
            }
        };
    }

    protected String writeToStore() {
        return String.format("%s%s%s",
                this.name,
                DATA_SEP,
                this.isDone);
    };

    @Override
    public String toString() {
        return String.format("[%s] %s",isDone ? "x" : " ", name);
    }
}
