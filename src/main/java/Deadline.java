import java.time.LocalDateTime;

public class Deadline extends Task{
    LocalDateTime due;

    public Deadline(boolean done, String name, LocalDateTime due) throws InvalidTaskException {
        super(done, name);
        this.due = due;
    }

    public Deadline(String name, LocalDateTime due) throws InvalidTaskException {
        super(name);
        this.due = due;
    }

    public Deadline(String name) throws InvalidTaskException {
        this(name, null);
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDueDate(LocalDateTime due) {
        this.due = due;
    }

    @Override
    protected String writeToStore(DateTimeParser dateParser) {
        return String.format("%s%s%s%s%s",
                TaskType.DEADLINE.flag,
                DATA_SEP,
                super.writeToStore(dateParser),
                DATA_SEP,
                dateParser.convertDateToString(this.due)
        );
    }

    @Override
    public String toString() {
        return String.format("[D]%s (Due: %s)",super.toString(), due);
    }
}
