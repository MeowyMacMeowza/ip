package korvus.storage;

public class StorageConflictException extends Exception{

    /**
     * Returns an instance of StorageConflictException, if there is any conflicts with file access in Storage.
     *
     * @param msg String message to be passed down.
     */
    public StorageConflictException(String msg) {
        super(msg);
    }
}
