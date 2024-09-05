package service;


import exception.GuestNotFoundException;
import exception.HotelUnavailableException;
import repository.entity.BookingEntity;
import repository.entity.GuestEntity;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import transport.client.impl.MonitoringSocketClientImpl;
import transport.dto.request.GuestDTO;
import util.Config;
import util.CsvParser;
import util.Mapper;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public class BookingService {
    private final Config config = new Config();
    private final GuestCsvRepository repositoryGuest;
    private final BookingCsvRepository repositoryBooking;
    private final Mapper mapper = new Mapper();
    private final Scanner scanner;
    private final CsvParser csvParserBooking;
    private final CsvParser csvParserGuest;
    private final Logger LOGGER = Logger.getLogger(BookingService.class.getName());
    private final MonitoringSocketClientImpl monitoringSocketClient;


    public BookingService(GuestCsvRepository repositoryGuest, BookingCsvRepository repositoryBooking, Scanner scanner,
                          CsvParser csvParserBooking, CsvParser csvParserGuest, MonitoringSocketClientImpl monitoringSocketClient) {
        this.repositoryGuest = repositoryGuest;
        this.repositoryBooking = repositoryBooking;
        this.scanner = scanner;
        this.csvParserBooking = csvParserBooking;
        this.csvParserGuest = csvParserGuest;
        this.monitoringSocketClient = monitoringSocketClient;
    }

    public void createGuest() {
        try {
            String firstName = getValidInput(ServiceMessages.ENTER_NAME.getMessage(),
                    ServiceMessages.ERROR_MESSAGE_EMPTY_NAME.getMessage());
            String ageInput = getValidInput(ServiceMessages.ENTER_AGE.getMessage(),
                    ServiceMessages.ERROR_MESSAGE_EMPTY_AGE.getMessage());
            int age = Integer.parseInt(ageInput);
            String address = getValidInput(ServiceMessages.ENTER_ADDRESS.getMessage(),
                    ServiceMessages.ERROR_MESSAGE_EMPTY_ADDRESS.getMessage());
            String passport = getValidInput(ServiceMessages.ENTER_PASSPORT.getMessage(),
                    ServiceMessages.ERROR_MESSAGE_EMPTY_PASSPORT.getMessage());

            GuestDTO guestDTO = new GuestDTO(firstName, age, passport, address);
            GuestEntity guest = mapper.toEntity(guestDTO);

            if (validateCreateGuest(guest)) {
                repositoryGuest.create(guest);
                monitoringSocketClient.sendEvent(EventType.CREATED, "Гость добавлен: " + guest.toString());
            } else {
                monitoringSocketClient.sendEvent(EventType.MISTAKE, "Добавление гостя " + guest.toString() + " неудачно");
            }
        } catch (NumberFormatException e) {
            LOGGER.severe(ServiceMessages.ERROR_AGE_NOT_INT.getMessage());
        } catch (IOException e) {
            LOGGER.severe(ServiceMessages.ERROR_CREATE_GUEST.getMessage() + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe(ServiceMessages.UNKNOWN_ERROR.getMessage() + e.getMessage());
        }
    }



    public void checkIn() throws HotelUnavailableException, GuestNotFoundException {
        try {
            int idGuest = selectGuestToCheckIn();
            if (validateCheckInGuest(idGuest)) {
                LOGGER.info(ServiceMessages.REQUEST_TO_HOTEL_SYSTEM.getMessage());

                try (Socket socket = new Socket("localhost", config.getPort());
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    int idHotel = selectHotelToCheckIn(in, out);
                    processRoomAvailability(in, idGuest, idHotel);

                } catch (IOException e) {
                    LOGGER.severe("Ошибка при заселении: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Неизвестная ошибка при заселении: " + e.getMessage());
        }

    }

    private int selectGuestToCheckIn() throws IOException {
        List<GuestEntity> guests = csvParserGuest.loadGuests();
        for(GuestEntity guest : guests){
            guest.getInfo();
        }
        System.out.println(ServiceMessages.SELECT_GUEST.getMessage());
        String input;
        int guestId = -1; // Инициализируем переменную для хранения ID гостя

        while (true) {
            input = scanner.nextLine().trim(); // Убираем пробелы

            // Проверяем на пустую строку
            if (input.isEmpty()) {
                LOGGER.warning("Ошибка: Ввод не может быть пустым. Повторите ввод.");
                continue;
            }

            try {
                guestId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                LOGGER.warning("Ошибка: Введите корректный числовой идентификатор гостя.");
            }
        }

        return guestId;
    }

    private void processRoomAvailability(BufferedReader in, int idGuest, int idHotel) throws IOException {
        String getAvailabilityResponse = in.readLine(); // Получение ответа
        if ("AVAILABLE".equals(getAvailabilityResponse)) {
            bookRoom(idGuest, idHotel);
        } else {
            if("UNAVAILABLE".equals(getAvailabilityResponse)){
                LOGGER.warning("Такого отеля не существует");
                monitoringSocketClient.sendEvent(EventType.MISTAKE, "Гостиница " + idHotel +
                        " недоступна. Заявка отменена.");
            }
            else{
                LOGGER.warning("В этом отеле нет мест");
                monitoringSocketClient.sendEvent(EventType.MISTAKE, "Гостиница " + idHotel +
                        " недоступна. Заявка отменена.");
            }
        }
    }

    private int selectHotelToCheckIn(BufferedReader in, PrintWriter out) throws IOException {
        String getAllHotelsResponse = in.readLine();
        System.out.println(getAllHotelsResponse);
        System.out.println(ServiceMessages.ENTER_HOTEL.getMessage());
        int idHotel = Integer.parseInt(scanner.nextLine());
        out.println(idHotel); // Отправка запроса о доступности гостиницы
        return idHotel;
    }

    private void bookRoom(int guestId, int hotelId) throws IOException {
        if (validateBookingDoubleCheckIn(guestId)){
            BookingEntity booking = new BookingEntity(guestId, hotelId);
            repositoryBooking.create(booking);
            LOGGER.info("Гость " + guestId + " успешно заселен в " + hotelId);
            monitoringSocketClient.sendEvent(EventType.CREATED, "Гость " + guestId +
                    " заселен в отель " + hotelId);
        }
    }


    private boolean validateBookingDoubleCheckIn(int guestId) throws IOException {
        List<BookingEntity> bookings = csvParserBooking.loadBookings();
        for (BookingEntity booking : bookings) {
            if (booking.getGuestId() == guestId) {
                LOGGER.warning("Заселение отменено: гость уже заселен в отель " + booking.getHotelId());
                monitoringSocketClient.sendEvent(EventType.MISTAKE,"Заселение отменено: гость уже заселен в" +
                        " отель " + booking.getHotelId());
                return false;
            }
        }
        return true;
    }

    private boolean validateCheckInGuest(int guestId) throws IOException {
        boolean isExist = false;
        List<GuestEntity> guests = csvParserGuest.loadGuests();
        for (GuestEntity guest : guests) {
            if (guestId == guest.getId()) {
                isExist = true;
                break;
            }
        }

        if(!isExist){
            LOGGER.warning("Заселение отменено: гостя с таким ID не существует");
            monitoringSocketClient.sendEvent(EventType.MISTAKE,"Заселение отменено: гостя с таким ID не существует");
        }
        return isExist;
    }

    public boolean validateCreateGuest(GuestEntity guestEntity) throws IOException {
        if(!validateName(guestEntity.getName())){
            System.out.println(ServiceMessages.WRONG_NAME.getMessage());
            return false;
        }
        if(!validateAge(guestEntity.getAge())){
            System.out.println(ServiceMessages.WRONG_AGE.getMessage());
            return false;
        }

        if (!validateAddress(guestEntity.getAddress())){
            System.out.println(ServiceMessages.WRONG_ADDRESS.getMessage());
            return false;
        }

        if(!validatePassportNumber(guestEntity.getPassportNumber())){
            System.out.println(ServiceMessages.WRONG_PASSPORT.getMessage());
            return false;
        }
        return true;
    }

    private boolean validateName(String name){
        return name != null && name.length() <= 20 && Character.isUpperCase(name.charAt(0));
    }

    private boolean validateAge(int age){
        return age >= 0 && age <= 120;
    }

    private String getValidInput(String prompt, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(errorMessage);
            }
        } while (input.isEmpty());
        return input;
    }



    private boolean validatePassportNumber(String passportNumber) throws IOException {
        if (passportNumber != null && passportNumber.length() == 6 && passportNumber.matches("\\d{6}")) {
            boolean passportExists = true;
            List<GuestEntity> guests = csvParserGuest.loadGuests();
            for (GuestEntity guest : guests) {
                if (guest.getPassportNumber().equals(passportNumber)) {
                    passportExists = false;
                    break;
                }
            }
            return passportExists;
        } else {
            System.out.println(ServiceMessages.WRONG_COUNT_NUMBER_PASSPORT.getMessage());
            return false;
        }
    }

    private boolean validateAddress(String address){

        return address != null && address.length() <= 20;
    }
}

