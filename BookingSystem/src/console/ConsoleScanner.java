package console;

import service.BookingService;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConsoleScanner {

    private static final Logger LOGGER = Logger.getLogger(ConsoleScanner.class.getName());

    private final Scanner scanner;
    private final BookingService bookingService;

    public ConsoleScanner(Scanner scanner, BookingService bookingService) {
        this.scanner = scanner;
        this.bookingService = bookingService;
    }


    private String scan() {
        return scanner.nextLine().toUpperCase(Locale.ROOT);
    }

    public void checkCommand(){
        while (true) {
            String scannedCommand = this.scan();

            try {
                Commands command = Commands.valueOf(scannedCommand);

                // Выполнение команды
                switch (command) {
                    case HELP:
                        command.printHelp();
                        break;
                    case CREATE:
                        bookingService.createGuest();
                        break;
                    case CHECKIN:
                        bookingService.checkIn();
                        break;
                    case EXIT:
                        System.exit(0);
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Unknown command: " + scannedCommand);
                System.out.println("Вы ввели неизвестную команду. Попробуйте еще раз");
            } catch (Exception exception) {
                LOGGER.severe("An unexpected error occurred: " + exception.getMessage());
            }
        }
    }




}
