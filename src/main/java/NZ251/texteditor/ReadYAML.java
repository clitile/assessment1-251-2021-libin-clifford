package NZ251.texteditor;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
// Read the yaml file
public class ReadYAML {
    Map<String, ArrayList<String>> properties;

    public ReadYAML() {

    }
    // The parameter is a path
    public ReadYAML(String path) {
        Yaml yaml = new Yaml();
        try {
            // Read the path as a stream
            properties = yaml.load(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
