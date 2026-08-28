package korvus.tasks;

import korvus.utils.DateTimeParser;

import java.time.LocalDateTime;

public class Event extends Task{
    LocalDateTime start;
    LocalDateTime end;

    public Event(boolean done, String name, String start, String end, DateTimeParser dateTimeParser) throws InvalidTaskException {
        super(done, name, dateTimeParser);
        this.start = dateTimeParser.parseDateString(start);
        this.end = dateTimeParser.parseDateString(end);
    }

    public Event(String name, String start, String end, DateTimeParser dateTimeParser) throws InvalidTaskException {
        super(name, dateTimeParser);
        this.start = dateTimeParser.parseDateString(start);
        this.end = dateTimeParser.parseDateString(end);
    }

    public Event(String name, DateTimeParser dateTimeParser) throws InvalidTaskException {
        this(name, null, null, dateTimeParser);
    }

    public String getDuration() {
        return String.format("%s - %s",
                dateTimeParser.convertDateToString(start),
                dateTimeParser.convertDateToString(end)
        );
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    protected String writeToStore() {
        return String.format("%s%s%s%s%s%s%s",
                TaskType.EVENT.flag,
                DATA_SEP,
                super.writeToStore(),
                DATA_SEP,
                dateTimeParser.convertDateToString(this.start),
                DATA_SEP,
                dateTimeParser.convertDateToString(this.end)
        );
    }

    @Override
    public String toString() {
        return String.format("[E]%s (Duration: %s)",super.toString(), this.getDuration());
    }
}