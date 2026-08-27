public class Event extends Task{
    String start;
    String end;

    public Event(boolean done, String name, String start, String end) throws InvalidTaskException {
        super(done, name);
        this.start = start;
        this.end = end;
    }

    public Event(String name, String start, String end) throws InvalidTaskException {
        super(name);
        this.start = start;
        this.end = end;
    }

    public Event(String name) throws InvalidTaskException {
        this(name, "unknown", "unknown");
    }

    public String getDuration() {
        return String.format("%s - %s", start, end);
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    protected String writeToStore() {
        return String.format("%s%s%s%s%s%s%s",
                TaskType.EVENT.flag,
                DATA_SEP,
                super.writeToStore(),
                DATA_SEP,
                this.start,
                DATA_SEP,
                this.end
        );
    }

    @Override
    public String toString() {
        return String.format("[E]%s (Duration: %s)",super.toString(), getDuration());
    }
}