package korvus.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import korvus.Config;

/**
 * Storage class as a controller for storage operations.
 */
public class Storage {
    private String relativePath;
    private ArrayList<StorageParser<? extends Storable>> parserList;

    /**
     * Returns an instance of the object Storage.
     */
    public Storage() {
        this("");
    }

    /**
     * Returns an instance of the object Storage, given the relative path of a folder.
     * This instance of Storage is "bound to" said folder, and any subsequent file read is assumed to be in the folder.
     *
     * @param path (Relative) path of folder containing files.
     */
    public Storage(String path) {
        this.relativePath = path;
        this.parserList = new ArrayList<>();

        try {
            Files.createDirectories(Path.of(relativePath));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Returns a BufferedReader object, given the relative path of the file to read.
     *
     * @param filePath Path of the file, relative to the directory Storage is bound to.
     * @return BufferedReader bufferedReader.
     * @throws IOException If file cannot be read for whatever reason.
     */
    public BufferedReader readFile(String filePath) throws IOException {
        FileReader file = new FileReader(relativePath + filePath);
        return new BufferedReader(file);
    }

    /**
     * Returns a BufferedWriter object, given the relative path of the file to read.
     *
     * @param filePath Path of the file, relative to the directory Storage is bound to.
     * @return BufferedWriter buffedWriter.
     * @throws IOException If file cannot be written for whatever reason.
     */
    public BufferedWriter writeFile(String filePath) throws IOException {
        FileWriter file = new FileWriter(relativePath + filePath);
        return new BufferedWriter(file);
    }

    /**
     * Adds a StorageParser into the storage's list of storage parsers.
     * This ensures that there are no conflicting storage parsers (reading the same file),
     * throwing an exception if there are conflicting filePaths.
     *
     * @param storageParser StorageParser to be added to the storage and checked.
     * @throws StorageConflictException If there exists another StorageParser that has the same file path.
     */
    public <T extends Storable<T>> void addParser(StorageParser<T> storageParser) throws StorageConflictException {
        for (StorageParser<? extends Storable<?>> parser : parserList) {
            if (parser.isParserConflict(storageParser)) {
                throw new StorageConflictException("Cannot assign multiple parsers to one file");
            }
        }

        parserList.add(storageParser);
    }

    /**
     * Returns a Config object, which contains the configuration settings for Korvus.
     * If the config file cannot be found/read, the default config is generated instead.
     *
     * @return Config config.
     */
    public Config readConfigFile() throws FileNotFoundException {
        FileReader configFile = new FileReader(relativePath + Config.CONFIG_FILE);
        BufferedReader reader = new BufferedReader(configFile);
        return Config.readConfigFile(reader);
    }

    /**
     * Saves the given Config object to its config file.
     *
     * @param config Config object to be saved.
     */
    public void saveConfigFile(Config config) throws IOException {
        FileWriter configFile = new FileWriter(relativePath + Config.CONFIG_FILE);
        BufferedWriter writer = new BufferedWriter(configFile);
        config.saveConfigFile(writer);
    }
}
