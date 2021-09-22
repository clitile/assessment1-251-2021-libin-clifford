package NZ251.texteditor;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

public class ReadYAML {
    Map<String, ArrayList<String>> properties;

    public ReadYAML() {

    }

    public ReadYAML(String path) {
        Yaml yaml = new Yaml();
        try {
            properties = yaml.load(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
