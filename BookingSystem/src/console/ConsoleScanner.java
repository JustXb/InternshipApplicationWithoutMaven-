package console;

import exception.EnteredNotValidDataException;
import repository.impl.GuestCsvRepository;
import service.BookingService;
import transport.client.impl.HotelSocketClientImpl;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleScanner {

    private static final Logger LOGGER = Logger.getLogger(ConsoleScanner.class.getName());

    private Scanner scanner;
    private BookingService bookingService;

    public ConsoleScanner(Scanner scanner, BookingService bookingService) {
        this.scanner = scanner;
        this.bookingService = bookingService;
    }

    public void parseCommand(String inputData){
        String scannerCommand = scanner.nextLine().toUpperCase(Locale.ROOT);
    }

    private String scan() {
        return scanner.nextLine().toUpperCase(Locale.ROOT);
    }

    public void checkCommand(){
        while (true) {
            String scannedCommand = this.scan();

            try {
                Commands command = Commands.valueOf(scannedCommand);

                if (isKnownCommand(scannedCommand)) {
                    LOGGER.info("Znayu");
                } else {
                    LOGGER.info("Ne znayu");
                    continue;
                }

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
            } catch (IOException e) {
                LOGGER.severe("IOException occurred: " + e.getMessage());
                throw new RuntimeException(e);
            } catch (Exception exception) {
                LOGGER.severe("An unexpected error occurred: " + exception.getMessage());
            }
        }
    }



    private void validateData(String data) throws EnteredNotValidDataException {
        if (data == null) {
            throw new EnteredNotValidDataException("InputData is null");
        }
    }

    private void proccessData() throws EnteredNotValidDataException {
        LOGGER.log(Level.INFO, "Enter Data");
        String data = scan();
        validateData(data);
        bookingService.validateDto();
    }



    private boolean isKnownCommand(String command) {
        for (Commands commands : Commands.values()) {
            if (commands.name().equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }


}
