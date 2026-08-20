public class ToDo extends Task{
    public ToDo(String name) throws InvalidTaskException {
        super(name);
    }

    @Override
    public String toString() {
        return String.format("[T]%s",super.toString());
    }
}
