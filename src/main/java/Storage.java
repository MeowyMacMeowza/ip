import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Storage {
    private String relativePath;
    private ArrayList<StorageParser<? extends Storable>> parserList;

    public Storage() {
        this("");
    }

    public Storage(String path) {
        this.relativePath = path;
        this.parserList = new ArrayList<>();
    }

    //Read File
    public BufferedReader readFile(String filePath) throws IOException {
        FileReader file = new FileReader(relativePath + filePath);
        return new BufferedReader(file);
    }

    //Write File
    public BufferedWriter writeFile(String filePath) throws IOException {
        FileWriter file = new FileWriter(relativePath + filePath);
        return new BufferedWriter(file);
    }

    public <T extends Storable<T>> void addParser(StorageParser<T> storageParser) throws StorageConflictException {
        for (StorageParser<? extends Storable<?>> parser : parserList) {
            if (parser.isParserConflict(storageParser)) {
                throw new StorageConflictException("Cannot assign multiple parsers to one file");
            }
        }

        parserList.add(storageParser);
    }

    //Read Setting?
    public Config readConfigFile() throws FileNotFoundException {
        if(Files.exists(Path.of(relativePath, Config.CONFIG_FILE))) {
            FileReader configFile = new FileReader(Config.CONFIG_FILE);
            BufferedReader reader = new BufferedReader(configFile);
            return Config.readConfigFile(reader);
        } else {
            // Create new Config to save
            return Config.generateNewConfig();
        }
    }

    public void saveConfigFile(Config config) throws IOException {
        FileWriter configFile = new FileWriter(relativePath + Config.CONFIG_FILE);
        BufferedWriter writer = new BufferedWriter(configFile);
        config.saveConfigFile(writer);
    }
}
