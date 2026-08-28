package korvus.storage;

import java.io.IOException;

public interface Storable<T> {
    public String writeToString();
    public boolean readFromParser(StorageParser<? extends Storable<T>> parser) throws IOException;
}
