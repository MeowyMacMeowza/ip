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

    private boolean isDone;
    private String name;

    public Task(String name) throws InvalidTaskException {
        this(false, name);
    }

    public Task(boolean done, String name) throws InvalidTaskException {
        if(name.startsWith("-"))
            throw new InvalidTaskException("Task name cannot start with '-'! Are you sure that you typed the name correctly?");
        this.name = name;
        this.isDone = done;
    }

    public boolean doTask() {
        if(isDone) return false;
        isDone = true;
        return true;
    }

    public boolean undoTask() {
        if(!isDone) return false;
        isDone = false;
        return true;
    }

    public String getName() {
        return name;
    }

    public static Task generateTask(String taskInput) throws InvalidTaskException {
        return switch (taskInput) {
            case String s when s.startsWith(TaskType.TODO.flag) -> {
                yield new ToDo(s.substring(3));
            }
            case String s when s.startsWith(TaskType.DEADLINE.flag) -> {
                String[] args = s.substring(3).split(DATA_SEP, 2);
                yield args.length > 1 ? new Deadline(args[0], args[1]) : new Deadline(args[0]);
            }
            case String s when s.startsWith(TaskType.EVENT.flag) -> {
                String[] args = s.substring(3).split(DATA_SEP, 3);
                yield args.length > 2 ? new Event(args[0], args[1], args[2]) : new Event(args[0]);
            }
            default -> new ToDo(taskInput);
        };
    }

    protected static Task readTaskFromFile(String fileInput) throws InvalidTaskException {
        return switch (fileInput) {
            case String s when s.startsWith(TaskType.TODO.flag) -> {
                String[] args = s.split(DATA_SEP,3);
                yield new ToDo(Boolean.parseBoolean(args[2]), args[1]);
            }
            case String s when s.startsWith(TaskType.DEADLINE.flag) -> {
                String[] args = s.split(DATA_SEP, 4);
                yield new Deadline(Boolean.parseBoolean(args[2]), args[1], args[3]);
            }
            case String s when s.startsWith(TaskType.EVENT.flag) -> {
                String[] args = s.split(DATA_SEP, 5);
                yield new Event(Boolean.parseBoolean(args[2]), args[1], args[3], args[4]);
            }
            default -> throw new InvalidTaskException(String.format("Task format not supported!\nInput: %s",fileInput));
        };
    }

    protected String writeToStore() {
        return String.format("%s%s%s",
                this.name,
                DATA_SEP,
                this.isDone
        );
    };

    @Override
    public String toString() {
        return String.format("[%s] %s",isDone ? "x" : " ",name);
    }
}
