public class Deadline extends Task{
    String due;

    public Deadline(boolean done, String name, String due) throws InvalidTaskException {
        super(done, name);
        this.due = due;
    }

    public Deadline(String name, String due) throws InvalidTaskException {
        super(name);
        this.due = due;
    }

    public Deadline(String name) throws InvalidTaskException {
        this(name, "unknown");
    }

    public String getDue() {
        return due;
    }

    public void setDueDate(String due) {
        this.due = due;
    }

    protected String writeToStore() {
        return String.format("%s%s%s%s%s",
                TaskType.DEADLINE.flag,
                DATA_SEP,
                super.writeToStore(),
                DATA_SEP,
                this.due
        );
    }

    @Override
    public String toString() {
        return String.format("[D]%s (Due: %s)",super.toString(), due);
    }
}
