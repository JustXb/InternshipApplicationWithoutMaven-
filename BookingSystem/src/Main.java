import console.ConsoleScanner;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import service.BookingService;
import util.CsvParser;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        GuestCsvRepository repositoryGuest = new GuestCsvRepository("guest.csv");
        BookingCsvRepository repositoryBooking = new BookingCsvRepository("booking.csv");
        CsvParser csvParserBooking = new CsvParser("booking.csv");
        CsvParser csvParserGuest = new CsvParser("guest.csv");



        BookingService bookingService = new BookingService(repositoryGuest, repositoryBooking, scanner,
                csvParserBooking, csvParserGuest);
        ConsoleScanner consoleScanner = new ConsoleScanner(scanner, bookingService);



        System.out.println("Введите help для получения помощи");
        consoleScanner.checkCommand();


    }
}

