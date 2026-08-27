import java.time.LocalDateTime;

public class Event extends Task{
    LocalDateTime start;
    LocalDateTime end;

    public Event(boolean done, String name, LocalDateTime start, LocalDateTime end) throws InvalidTaskException {
        super(done, name);
        this.start = start;
        this.end = end;
    }

    public Event(String name, LocalDateTime start, LocalDateTime end) throws InvalidTaskException {
        super(name);
        this.start = start;
        this.end = end;
    }

    public Event(String name) throws InvalidTaskException {
        this(name, null, null);
    }

    public String getDuration() {
        return String.format("%s - %s", start, end);
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    protected String writeToStore(DateTimeParser dateParser) {
        return String.format("%s%s%s%s%s%s%s",
                TaskType.EVENT.flag,
                DATA_SEP,
                super.writeToStore(dateParser),
                DATA_SEP,
                dateParser.convertDateToString(this.start),
                DATA_SEP,
                dateParser.convertDateToString(this.end)
        );
    }

    @Override
    public String toString() {
        return String.format("[E]%s (Duration: %s)",super.toString(), getDuration());
    }
}