package korvus.tasks;

import korvus.utils.DateTimeParser;

public class ToDo extends Task{
    public ToDo(boolean isDone, String name, DateTimeParser dateTimeParser) throws InvalidTaskException {
        super(isDone, name, dateTimeParser);
    }

    public ToDo(String name, DateTimeParser dateTimeParser) throws InvalidTaskException {
        super(name, dateTimeParser);
    }

    @Override
    protected String writeToStore() {
        return String.format("%s%s%s",
                TaskType.TODO.flag,
                DATA_SEP,
                super.writeToStore());
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
