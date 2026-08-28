package korvus.storage;

public class StorageConflictException extends Exception{
    public StorageConflictException(String msg) {
        super(msg);
    }
}
