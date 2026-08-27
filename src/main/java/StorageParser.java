import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class StorageParser<T extends Storable<T>> {
    private Storage storage;
    private String filePath;

    public StorageParser(Storage storage, String filePath) throws StorageConflictException {
        this.storage = storage;
        this.filePath = filePath;

        storage.addParser(this);
    }

    public <S extends Storable<S>> boolean isParserConflict(StorageParser<S> parser) {
        return this.filePath.equals(parser.filePath);
    }

    // Reads from provided storage, returns
    public String readStorage() throws IOException {
        BufferedReader reader = storage.readFile(filePath);
        String taskslistString = reader.readAllAsString();
        reader.close();
        return taskslistString;
    }

    // Writes to storage
    public void writeStorage(T t) throws IOException {
        BufferedWriter writer = storage.writeFile(filePath);
        writer.write(t.writeToString());
        writer.close();
    }
}
