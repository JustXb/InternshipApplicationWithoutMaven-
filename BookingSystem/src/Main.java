import console.ConsoleScanner;
import service.BookingService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        BookingService bookingService = new BookingService();
        ConsoleScanner consoleScanner = new ConsoleScanner(scanner, bookingService);



        System.out.println("Введите help для получения помощи");
        consoleScanner.checkCommand();


    }
}

