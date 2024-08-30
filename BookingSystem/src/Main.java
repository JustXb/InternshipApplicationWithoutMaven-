import console.ConsoleScanner;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import service.BookingService;
import transport.client.impl.MonitoringSocketClientImpl;
import util.CsvParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Properties properties = new Properties();
        properties.load(new FileReader("resources/application.properties"));

        GuestCsvRepository repositoryGuest = new GuestCsvRepository(properties.getProperty
                ("receiver.guests.csv.filepath"));
        BookingCsvRepository repositoryBooking = new BookingCsvRepository(properties.getProperty
                ("receiver.bookings.csv.filepath"));
        CsvParser csvParserBooking = new CsvParser(properties.getProperty
                ("receiver.guests.csv.filepath"));
        CsvParser csvParserGuest = new CsvParser(properties.getProperty
                ("receiver.bookings.csv.filepath"));
        MonitoringSocketClientImpl monitoringSocketClient = new MonitoringSocketClientImpl();



        BookingService bookingService = new BookingService(repositoryGuest, repositoryBooking, scanner,
                csvParserBooking, csvParserGuest, monitoringSocketClient);
        ConsoleScanner consoleScanner = new ConsoleScanner(scanner, bookingService);



        System.out.println("Введите help для получения помощи");
        consoleScanner.checkCommand();


    }
}

