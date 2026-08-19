public class Task {
    private boolean isDone;
    private String name;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public boolean doTask() {
        if(isDone) return false;
        isDone = true;
        return true;
    }

    public boolean undoTask() {
        if(!isDone) return false;
        isDone = false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s",isDone ? "x" : " ",name);
    }
}
