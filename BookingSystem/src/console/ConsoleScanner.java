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

    private static final Logger LOGGER = Logger.getLogger(HotelSocketClientImpl.class.getName());

    //TODO: вынести в сервис
    //TODO: не константы, объекты сеттим в main
    private static GuestCsvRepository repo = new GuestCsvRepository("guest.csv");
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
        while(true) {
            String scannedCommand = this.scan();
            Commands command = Commands.valueOf(scannedCommand);
            try {
                if (isKnownCommand(scannedCommand)){
                    System.out.println("Znayu");
                }
                else {
                    System.out.println("Вы ввели неизвестную команду. Попробуйте еще раз");
                    scannedCommand = this.scan();
                }
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
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            } catch ( IOException e) {
                //TODO: логгирировать
                throw new RuntimeException(e);
            } catch (Exception exception){
                //пример
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
