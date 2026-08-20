public class Event extends Task{
    String start;
    String end;

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

    @Override
    public String toString() {
        return String.format("[E]%s (Duration: %s)",super.toString(), getDuration());
    }
}