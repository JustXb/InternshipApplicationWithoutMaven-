import console.ConsoleScanner;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import service.BookingService;
import transport.client.impl.MonitoringSocketClientImpl;
import util.Config;
import util.CsvParser;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        Config config = new Config();

        GuestCsvRepository repositoryGuest = new GuestCsvRepository(config.getGuestsPath());
        BookingCsvRepository repositoryBooking = new BookingCsvRepository(config.getBookingsPath());
        CsvParser csvParserBooking = new CsvParser(config.getBookingsPath());
        CsvParser csvParserGuest = new CsvParser(config.getGuestsPath());
        MonitoringSocketClientImpl monitoringSocketClient = new MonitoringSocketClientImpl();



        BookingService bookingService = new BookingService(repositoryGuest, repositoryBooking, scanner,
                csvParserBooking, csvParserGuest, monitoringSocketClient);
        ConsoleScanner consoleScanner = new ConsoleScanner(scanner, bookingService);



        System.out.println("Введите help для получения помощи");
        consoleScanner.checkCommand();


    }
}

