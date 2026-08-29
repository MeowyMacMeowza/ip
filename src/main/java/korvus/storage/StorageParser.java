package korvus.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class StorageParser<T extends Storable<T>> {
    private Storage storage;
    private String filePath;

    /**
     * Returns an instance of the object StorageParser.
     *
     * @param storage Storage object pointing to the directory with files.
     * @param filePath (Relative) path of the file to be read.
     * @throws StorageConflictException If another parser in storage is pointing to the same file.
     */
    public StorageParser(Storage storage, String filePath) throws StorageConflictException {
        this.storage = storage;
        this.filePath = filePath;

        storage.addParser(this);
    }

    /**
     * Returns whether the given parser has conflicting file name.
     *
     * @param parser StorageParser to be checked with.
     * @return Boolean whether the current parser conflicts with the given parser.
     */
    public <S extends Storable<S>> boolean isParserConflict(StorageParser<S> parser) {
        return this.filePath.equals(parser.filePath);
    }

    /**
     * Returns the data from Storage.
     *
     * @return Data of object as a String.
     * @throws IOException If there are any errors in reading the file.
     */
    public String readStorage() throws IOException {
        BufferedReader reader = storage.readFile(filePath);
        String taskslistString = reader.readAllAsString();
        reader.close();
        return taskslistString;
    }

    /**
     * Writes the Storable into Storage.
     *
     * @param t The Storable object to be written to storage.
     * @throws IOException If there are any errors in writing to the file.
     */
    public void writeStorage(T t) throws IOException {
        BufferedWriter writer = storage.writeFile(filePath);
        writer.write(t.writeToString());
        writer.close();
    }
}
