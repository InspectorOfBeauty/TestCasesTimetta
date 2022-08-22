package utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class PropertyReader {
    private static String getPropertiesFromFile(String nameProperty) {
        try (InputStream input = new FileInputStream("src/test/resources/properties.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, Object> loaded = yaml.load(input);
            Object value = loaded.get(nameProperty);
            if (value != null) {
                return value.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            System.out.println("No such property!");
            e.printStackTrace();
            return null;
        }
    }

    public static String getPasswordRecoveryPageAddress() {
        return PropertyReader.getPropertiesFromFile("PasswordRecoveryPageAddress");
    }

    public static String getPasswordRecoveryNotification1() {
        return PropertyReader.getPropertiesFromFile("PasswordRecoveryNotification1");
    }

    public static String getPasswordRecoveryNotification2() {
        return PropertyReader.getPropertiesFromFile("PasswordRecoveryNotification2");
    }
}
