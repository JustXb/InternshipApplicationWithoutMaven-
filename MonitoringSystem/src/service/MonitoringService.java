package service;

import repository.impl.MonitoringBinRepository;
import transport.server.MonitoringServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class MonitoringService {
    private static final String LOG_DIRECTORY = "logs"; // Директория для логов
    private static final String LOG_FILE_PATH = "monitoring.bin"; // Префикс имени файла
    private static final long MAX_FILE_SIZE = 1024 * 1024; // Максимальный размер файла (1 МБ)
    private static final String RECORD_SEPARATOR = "\n"; // Разделитель записей
    private static final Logger LOGGER = Logger.getLogger(MonitoringService.class.getName());

    public MonitoringService() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("resources/application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.createDirectories(Path.of(properties.getProperty("LOG_DIRECTORY")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logEvent(String eventType, String message) {
        String header = createHeader(eventType);
        String logEntry = header + message + RECORD_SEPARATOR;
        writeLogEntry(logEntry);
    }

    private String createHeader(String eventType) {
        String header;
        switch (eventType.toLowerCase()) {
            case "created":
                header = "CREATED ";
                break;
            case "mistake":
                header = "MISTAKE ";
                break;
            case "success":
                header = "SUCCESS ";
                break;
            default:
                throw new IllegalArgumentException("Неверный тип события: " + eventType);
        }
        return header;
    }

    private void writeLogEntry(String logEntry) {
        try {
            File logFile = getCurrentLogFile();
            if (logFile.length() >= MAX_FILE_SIZE) {
                rotateLogFile(logFile);
            }
            try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
                fos.write(logEntry.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getCurrentLogFile() {
        return new File(LOG_DIRECTORY, LOG_FILE_PATH);
    }

    private void rotateLogFile(File logFile) throws IOException {
        File newLogFile = new File(LOG_DIRECTORY, LOG_FILE_PATH);
        Files.move(logFile.toPath(), newLogFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
