import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class Config {
    private enum ConfigType {
        tasklist_file_path("tasklist.txt"),
        datetime_format("yyyy-MM-dd HHmm"),
        commandline_length("100");

        private String value;

        ConfigType(String value) {
            this.value = value;
        }
    }

    public static final String CONFIG_FILE = "korvus.config"; //Using Hashmap

    private HashMap<String, String> configMap;
    
    private Config() {
        this.configMap = new HashMap<>();
    }

    public String getValue(String configName) {
        return configMap.get(configName);
    }

    public String setValue(String configName, String configValue) {
        return configMap.put(configName, configValue);
    }

    public static Config generateNewConfig() {
        Config newConfig = new Config();

        for (ConfigType ct : ConfigType.values()) {
            newConfig.configMap.put(ct.toString(), ct.value);
        }

        return newConfig;
    }

    public static Config readConfigFile(BufferedReader br) {
        Config config = new Config();
        br.lines()
                .map(str -> str.split("="))
                .filter(arr -> arr.length == 2)
                .forEach(arr -> config.configMap.put(arr[0], arr[1]));

        for (ConfigType ct : ConfigType.values()) {
            if(!config.configMap.containsKey(ct.toString())) {
                config.configMap.put(ct.toString(), ct.value);
            }
        }

        return config;
    }

    public void saveConfigFile(BufferedWriter bw) throws IOException {
        StringBuilder output = new StringBuilder();
        for(String key : configMap.keySet()) {
            if(!output.isEmpty()) {
                output.append('\n');
            }
            output.append(key);
            output.append('=');
            output.append(configMap.get(key));
        }

        bw.write(output.toString());
        bw.close();
    }
}
