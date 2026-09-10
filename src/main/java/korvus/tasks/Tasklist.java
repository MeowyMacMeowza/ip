package korvus.tasks;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import korvus.storage.Storable;
import korvus.storage.StorageParser;
import korvus.utils.DateTimeParser;

/**
 * Tasklist for tracking all tasks and performing task operations.
 * Able to store information to a file.
 */
public class Tasklist implements Storable<Tasklist> {
    private static String TASK_SEP = "<>";
    private static int TASK_NOT_FOUND;

    private DateTimeParser dateTimeParser;
    private ArrayList<Task> tasklist;

    /**
     * Returns an instance of tasklist.
     *
     * @param dateTimeParser DateTimeParser that should be used for datetime formatting.
     */
    public Tasklist(DateTimeParser dateTimeParser) {
        tasklist = new ArrayList<>();
        this.dateTimeParser = dateTimeParser;
    }

    /**
     * Returns a default instance of tasklist.
     */
    public Tasklist() {
        this(new DateTimeParser(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    /**
     * Returns the total number of tasks.
     *
     * @return Size of the tasklist.
     */
    public int getSize() {
        return tasklist.size();
    }

    /**
     * Returns the index of the queried Task.
     * This assumes that the task is in the tasklist.
     *
     * @param task Task to be found.
     * @return Index of the task in the tasklist.
     */
    public int getIndex(Task task) {
        return tasklist.indexOf(task);
    }

    /**
     * Adds the task into the tasklist.
     *
     * @param task Task data to be added.
     * @return String representation of the new Task in the tasklist.
     * @throws InvalidTaskException If the task data provided is invalid.
     */
    public String addTask(String task) throws InvalidTaskException {
        Task newTask = Task.generateTask(task, dateTimeParser);
        tasklist.add(newTask);
        return newTask.toString();
    }

    /**
     * Removes the task from the tasklist.
     *
     * @param sTask Task data to be deleted.
     * @return String representation of the deleted Task in the tasklist.
     * @throws InvalidTaskException If the task data provided is invalid.
     */
    public String deleteTask(String sTask) throws InvalidTaskException {
        int id = findTaskIdByName(sTask);

        //Failed to find task
        if (id == TASK_NOT_FOUND) {
            throw new InvalidTaskException(String.format("Task cannot be found.\nName: %s", sTask));
        } else {
            return deleteTask(id);
        }
    };

    /**
     * Removes the task from the tasklist.
     *
     * @param id Task index to be deleted.
     * @return String representation of the deleted Task in the tasklist.
     */
    public String deleteTask(int id) {
        return tasklist.remove(id).toString();
    };


    /**
     * Marks the task from the tasklist as done.
     *
     * @param sTask Task data to be marked as done.
     * @return String representation of the Task provided in the tasklist after marking.
     * @throws InvalidTaskException If the task is already marked as done.
     */
    public String doTask(String sTask) throws InvalidTaskException {
        int id = findTaskIdByName(sTask);

        //Failed to find task
        if (id == TASK_NOT_FOUND) {
            throw new InvalidTaskException(String.format("Task cannot be found.\nInput: %s", sTask));
        } else {
            return doTask(id);
        }
    };

    /**
     * Marks the task from the tasklist as done.
     *
     * @param id Index of task to be marked as done.
     * @return String representation of the Task provided in the tasklist after marking.
     * @throws InvalidTaskException If the task is already marked as done.
     */
    public String doTask(int id) throws InvalidTaskException {
        boolean status = tasklist.get(id).doTask();

        if (!status) {
            throw new InvalidTaskException(String.format("Task has already been done\n%s", tasklist.get(id)));
        }

        return tasklist.get(id).toString();
    };

    /**
     * Marks the task from the tasklist as not done.
     *
     * @param sTask Task data to be marked as not done.
     * @return String representation of the Task provided in the tasklist after marking.
     * @throws InvalidTaskException If the task is already marked as not done.
     */
    public String undoTask(String sTask) throws InvalidTaskException {
        int id = findTaskIdByName(sTask);

        //Failed to find task
        if (id == TASK_NOT_FOUND) {
            throw new InvalidTaskException(String.format("Task cannot be found.\nName: %s", sTask));
        } else {
            return undoTask(id);
        }
    };

    /**
     * Marks the task from the tasklist as not done.
     *
     * @param id Index of task to be marked as not done.
     * @return String representation of the Task provided in the tasklist after marking.
     * @throws InvalidTaskException If the task is already marked as not done.
     */
    public String undoTask(int id) throws InvalidTaskException {
        boolean status = tasklist.get(id).undoTask();

        if (!status) {
            throw new InvalidTaskException(String.format("Task has not been done\n%s", tasklist.get(id)));
        }

        return tasklist.get(id).toString();
    };

    /**
     * Finds tasks in the tasklist given a query.
     *
     * @param keyWord Keyword to search in the tasklist.
     * @return String representation of a list of Tasks that satisfy the provided keyword.
     */
    public String findTask(String keyWord) {
        StringBuilder stringBuilder = new StringBuilder();
        String searchPattern = ".*(" + keyWord + ").*";

        for (int i = 0; i < tasklist.size(); i++) {
            if (tasklist.get(i).getName().matches(searchPattern)) {
                addNewLine(stringBuilder);
                stringBuilder.append(String.format("%d. %s", i + 1, tasklist.get(i)));
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Adds a new line to the StringBuilder if it is non-empty.
     *
     * @param stringBuilder StringBuilder to append a new line to.
     */
    private void addNewLine(StringBuilder stringBuilder) {
        if (!stringBuilder.isEmpty()) {
            stringBuilder.append('\n');
        }
    }

    /**
     * Returns the id of the task that has the same name as the query.
     *
     * @param sTask Name of the task to find.
     * @return Index of the task with the same name.
     */
    private int findTaskIdByName(String sTask) {
        int id = -1;
        for (int i = 0; i < tasklist.size(); i++) {
            if (tasklist.get(i).getName().equals(sTask)) {
                id = i;
                break;
            }
        }

        return id;
    }

    /**
     * Writes the tasklist into a String for storing.
     *
     * @return String containing all the task information in the tasklist.
     */
    @Override
    public String writeToString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < tasklist.size(); i++) {
            output.append(tasklist.get(i).writeToStore());
            if (i + 1 != tasklist.size()) {
                output.append(TASK_SEP);
            }
        }
        return output.toString();
    }

    /**
     * Reads from a StorageParser to populate the tasks in the tasklist.
     *
     * @param storageParser StorageParser to read from.
     * @return Boolean on whether there are any errors in the file operations.
     * @throws IOException If there is an error in reading the file.
     */
    @Override
    public boolean readFromParser(StorageParser<? extends Storable<Tasklist>> storageParser) throws IOException {
        boolean hasErrors = false;

        for (String sTask : storageParser.readStorage().split(TASK_SEP)) {
            try {
                tasklist.add(Task.readTaskFromFile(sTask, dateTimeParser));
            } catch (InvalidTaskException | IndexOutOfBoundsException e) {
                hasErrors = true;
            }
        }

        return hasErrors;
    }

    /**
     * Returns a String containing all tasks.
     *
     * @return String containing all tasks in a formatted list.
     */
    @Override
    public String toString() {
        StringBuilder tasks = new StringBuilder();
        for (int i = 0; i < tasklist.size(); i++) {
            tasks.append(String.format("%d. %s", i + 1, tasklist.get(i)));
            if (i != tasklist.size() - 1) {
                tasks.append("\n");
            }
        }
        return tasks.toString();
    }
}
