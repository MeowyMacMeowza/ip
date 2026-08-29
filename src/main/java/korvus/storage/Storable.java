package korvus.storage;

import java.io.IOException;

public interface Storable<T> {

    /**
     * Returns the current Storable in a string for saving to file.
     *
     * @return String string to be written to a file.
     */
    public String writeToString();

    /**
     * Reads the provided parser and saves its data in itself.
     *
     * @param parser StorageParser StorageParser that contains the information needed to read the file
     * @return Boolean boolean on whether the reading process had any errors
     * @throws IOException If there is any error reading the file (with the parser)
     */
    public boolean readFromParser(StorageParser<? extends Storable<T>> parser) throws IOException;
}
