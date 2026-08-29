package korvus.tasks;

import korvus.utils.DateTimeParser;

import java.time.LocalDateTime;

public class Deadline extends Task{
    LocalDateTime due;

    public Deadline(boolean isDone, String name, String due, DateTimeParser dateTimeParser) throws InvalidTaskException {
        super(isDone, name, dateTimeParser);
        this.due = dateTimeParser.parseDateString(due);
    }

    public Deadline(String name, String due, DateTimeParser dateTimeParser) throws InvalidTaskException {
        super(name, dateTimeParser);
        this.due = dateTimeParser.parseDateString(due);
    }

    public Deadline(String name, DateTimeParser dateTimeParser) throws InvalidTaskException {
        this(name, null, dateTimeParser);
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDueDate(LocalDateTime due) {
        this.due = due;
    }

    @Override
    protected String writeToStore() {
        return String.format("%s%s%s%s%s",
                TaskType.DEADLINE.flag,
                DATA_SEP,
                super.writeToStore(),
                DATA_SEP,
                dateTimeParser.convertDateToString(this.due));
    }

    @Override
    public String toString() {
        return String.format("[D]%s (Due: %s)", super.toString(), dateTimeParser.convertDateToString(this.due));
    }
}
