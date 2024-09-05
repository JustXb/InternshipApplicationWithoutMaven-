package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private int port;
    private String hotelsPath;
    private String hotelsAvailabilityPath;


    public Config() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("resources/application.properties")) {
            properties.load(input);
            this.port = Integer.parseInt(properties.getProperty("processor.port"));
            this.hotelsPath = properties.getProperty("processor.hotels.json.filepath");
            this.hotelsAvailabilityPath = properties.getProperty("processor.hotelsAvailability.json.filepath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public String getHotelsPath() {
        return hotelsPath;
    }

    public String getHotelsAvailabilityPath() {
        return hotelsAvailabilityPath;
    }
}

