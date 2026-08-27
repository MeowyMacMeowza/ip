public class ToDo extends Task{
    public ToDo(boolean done, String name) throws InvalidTaskException {
        super(done, name);
    }

    public ToDo(String name) throws InvalidTaskException {
        super(name);
    }

    protected String writeToStore() {
        return String.format("%s%s%s",
                TaskType.TODO.flag,
                DATA_SEP,
                super.writeToStore()
        );
    }

    @Override
    public String toString() {
        return String.format("[T]%s",super.toString());
    }
}
