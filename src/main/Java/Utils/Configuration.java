package Utils;

import java.io.*;
import java.util.Properties;

public class Configuration {

    private Properties properties;

    Configuration(String configFileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(configFileName);
        properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
