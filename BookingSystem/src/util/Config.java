package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;

public class Config {
    private int port;
    private String bookingsPath;
    private String guestsPath;


    public Config() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("resources/application.properties")) {
            properties.load(input);
            this.port = Integer.parseInt(properties.getProperty("processor.port"));
            this.bookingsPath = properties.getProperty("receiver.bookings.csv.filepath");
            this.guestsPath = properties.getProperty("receiver.guests.csv.filepath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public String getBookingsPath() {
        return bookingsPath;
    }

    public String getGuestsPath() {
        return guestsPath;
    }
}

