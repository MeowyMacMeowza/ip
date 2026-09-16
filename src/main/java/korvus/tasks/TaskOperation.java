package korvus.tasks;

public class TaskOperation {
    public enum OperationType {
        DO, ADD, UNDO, DELETE;

        private static final OperationType[] operations = values();

        public OperationType reverse() {
            return switch(this) {
                case DO -> UNDO;
                case UNDO -> DO;
                case ADD -> DELETE;
                case DELETE -> ADD;
                default -> null;
            };
        }
    }
    private int id;
    private Task task;
    private OperationType operation;

    public TaskOperation(int id, Task task, OperationType operationType) {
        this.id = id;
        this.task = task;
        this.operation = operationType;
    }

    public TaskOperation getReverseOperation() {
        return new TaskOperation(this.id, this.task, this.operation.reverse());
    }

    public OperationType getOperation() {
        return operation;
    }

    public Task getTask() {
        return task;
    }

    public int getId() {
        return id;
    }
}
