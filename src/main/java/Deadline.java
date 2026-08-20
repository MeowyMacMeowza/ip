public class Deadline extends Task{
    String due;

    public Deadline(String name, String due) {
        super(name);
        this.due = due;
    }

    public Deadline(String name) {
        this(name, "unknown");
    }

    public String getDue() {
        return due;
    }

    public void setDueDate(String due) {
        this.due = due;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (Due: %s)",super.toString(), due);
    }
}
