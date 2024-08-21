import console.ConsoleScanner;
import service.HotelRequestService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        HotelRequestService hotelRequestService = new HotelRequestService();
        ConsoleScanner consoleScanner = new ConsoleScanner(scanner, hotelRequestService);



        System.out.println("Введите help для получения помощи");
        consoleScanner.checkCommand();


    }
}

