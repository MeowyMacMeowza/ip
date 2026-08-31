package korvus.tasks;

import korvus.storage.Storable;
import korvus.storage.StorageParser;
import korvus.utils.DateTimeParser;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Tasklist implements Storable<Tasklist> {
    private static String TASK_SEP = "<>";

    private DateTimeParser dateTimeParser;
    private ArrayList<Task> tasklist;

    public Tasklist(DateTimeParser dateTimeParser) {
        tasklist = new ArrayList<>();
        this.dateTimeParser = dateTimeParser;
    }

    public Tasklist() {
        this(new DateTimeParser(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public int getSize() {
        return tasklist.size();
    }

    public int getIndex(Task task) {
        return tasklist.indexOf(task);
    }

    public String addTask(String task) throws InvalidTaskException {
        Task newTask = Task.generateTask(task, dateTimeParser);
        tasklist.add(newTask);
        return newTask.toString();
    }

    public String deleteTask(String sTask) throws InvalidTaskException {
        int id = -1;
        for (int i = 0; i < tasklist.size(); i++) {
            if(!tasklist.get(i).getName().equals(sTask)) {
                continue;
            }
            id = i;
            break;
        }

        //Failed to find task
        if(id == -1) {
            throw new InvalidTaskException(String.format("Task cannot be found.\nName: %s", sTask));
        } else{
            return deleteTask(id);
        }
    };

    public String deleteTask(int id) {
        return tasklist.remove(id).toString();
    };

    public String doTask(String sTask) throws InvalidTaskException {
        int id = -1;
        for (int i = 0; i < tasklist.size(); i++) {
            if(!tasklist.get(i).getName().equals(sTask)) {
                continue;
            }
            id = i;
            break;
        }

        //Failed to find task
        if(id == -1) {
            throw new InvalidTaskException(String.format("Task cannot be found.\nInput: %s", sTask));
        } else{
            return doTask(id);
        }
    };

    public String doTask(int id) throws InvalidTaskException {
        boolean status = tasklist.get(id).doTask();
        if(!status) {
            throw new InvalidTaskException(String.format("Task has already been done\n%s", tasklist.get(id)));
        }

        return tasklist.get(id).toString();
    };

    public String undoTask(String sTask) throws InvalidTaskException {
        int id = -1;
        for (int i = 0; i < tasklist.size(); i++) {
            if(!tasklist.get(i).getName().equals(sTask)) {
                continue;
            }
            id = i;
            break;
        }

        //Failed to find task
        if(id == -1) {
            throw new InvalidTaskException(String.format("Task cannot be found.\nName: %s",sTask));
        } else{
            return undoTask(id);
        }
    };

    public String undoTask(int id) throws InvalidTaskException{
        boolean status = tasklist.get(id).undoTask();
        if(!status) {
            throw new InvalidTaskException(String.format("Task has not been done\n%s",tasklist.get(id)));
        }
        return tasklist.get(id).toString();
    };

    public String findTask(String keyWord) {
        StringBuilder stringBuilder = new StringBuilder();
        String searchPattern = ".*("+keyWord+").*";
        for (int i = 0; i < tasklist.size(); i++) {
            if(tasklist.get(i).getName().matches(searchPattern)) {
                if(!stringBuilder.isEmpty()) {
                    stringBuilder.append('\n');
                }
                stringBuilder.append(String.format("%d. %s", i, tasklist.get(i)));
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public String writeToString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < tasklist.size(); i++) {
            output.append(tasklist.get(i).writeToStore());
            if(i + 1 != tasklist.size()) {
                output.append(TASK_SEP);
            }
        }
        return output.toString();
    }

    @Override
    public boolean readFromParser(StorageParser<? extends Storable<Tasklist>> parser) throws IOException {
        boolean hasErrors = false;

        for(String sTask : parser.readStorage().split(TASK_SEP)) {
            try {
                tasklist.add(Task.readTaskFromFile(sTask, dateTimeParser));
            } catch (InvalidTaskException | IndexOutOfBoundsException e) {
                hasErrors = true;
            }
        }

        return hasErrors;
    }

    @Override
    public String toString() {
        StringBuilder tasks = new StringBuilder();
        for (int i = 0; i < tasklist.size(); i++) {
            tasks.append(String.format("%d. %s", i + 1, tasklist.get(i)));
            if(i != tasklist.size() - 1) {
                tasks.append("\n");
            }
        }
        return tasks.toString();
    }
}
