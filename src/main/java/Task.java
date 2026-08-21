public abstract class Task {
    private enum TaskType {
        TODO("-t "),
        DEADLINE("-d "),
        EVENT("-e ");

        private String flag;

        TaskType(String str) {
            this.flag = str;
        }
    }

    private boolean isDone;
    private String name;

    public Task(String name) throws InvalidTaskException {
        if(name.startsWith("-"))
            throw new InvalidTaskException("Task name cannot start with '-'! Are you sure that you typed the name correctly?");
        this.name = name;
        this.isDone = false;
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
                String[] args = s.substring(3).split(" \\| ", 2);
                yield args.length > 1 ? new Deadline(args[0], args[1]) : new Deadline(args[0]);
            }
            case String s when s.startsWith(TaskType.EVENT.flag) -> {
                String[] args = s.substring(3).split(" \\| ", 3);
                yield args.length > 2 ? new Event(args[0], args[1], args[2]) : new Event(args[0]);
            }
            default -> new ToDo(taskInput);
        };
    }

    @Override
    public String toString() {
        return String.format("[%s] %s",isDone ? "x" : " ",name);
    }
}
