package service;


import repository.entity.BookingEntity;
import repository.entity.GuestEntity;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import transport.client.impl.MonitoringSocketClientImpl;
import util.CsvParser;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public class BookingService {

    private final GuestCsvRepository repositoryGuest;
    private final BookingCsvRepository repositoryBooking;
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

    public void createGuest() throws IOException {
        System.out.print(ServiceMessages.ENTER_NAME);
        String firstName = scanner.nextLine();
        System.out.print(ServiceMessages.ENTER_AGE);
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print(ServiceMessages.ENTER_ADDRESS);
        String address = scanner.nextLine();
        System.out.print(ServiceMessages.ENTER_PASSPORT);
        String passport = scanner.nextLine();
        GuestEntity guest = new GuestEntity( firstName, age, passport, address);
        if(validateCreateGuest(guest)){
            repositoryGuest.create(guest);
            monitoringSocketClient.sendEvent(EventType.CREATED, "Гость добавлен: " + guest.toString());
        }
        else{
            monitoringSocketClient.sendEvent(EventType.MISTAKE, "Добавление гостя " + guest.toString()
                    + " неудачно" );
        }
    }


    public void checkIn() {
        try{
            int idGuest = selectGuestToCheckIn();
            if(validateCheckInGuest(idGuest)) {
                LOGGER.info("Реквест к процессингу");

                try(Socket socket = new Socket("localhost", 12345);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    int idHotel = selectHotelToCheckIn(in, out);
                    processRoomAvailability(in, idGuest, idHotel);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            //TODO: всегда логируем и бросаем исключение
            e.printStackTrace();
        }

    }

    private int selectGuestToCheckIn() throws IOException {
        List<GuestEntity> guests = csvParserGuest.loadGuests();
        for(GuestEntity guest : guests){
            guest.getInfo();
        }
        System.out.println(ServiceMessages.SELECT_GUEST);
        return Integer.parseInt(scanner.nextLine());
    }

    private void processRoomAvailability(BufferedReader in, int idGuest, int idHotel) throws IOException {
        String getAvailabilityResponse = in.readLine(); // Получение ответа
        if ("AVAILABLE".equals(getAvailabilityResponse)) {
            bookRoom(idGuest, idHotel);
        } else {
            LOGGER.warning("Такого отеля не существует");
            monitoringSocketClient.sendEvent(EventType.MISTAKE, "Гостиница " + idHotel +
                    " недоступна. Заявка отменена.");
        }
    }

    private int selectHotelToCheckIn(BufferedReader in, PrintWriter out) throws IOException {
        String getAllHotelsResponse = in.readLine();
        System.out.println(getAllHotelsResponse);
        System.out.println(ServiceMessages.ENTER_HOTEL);
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
            System.out.println(ServiceMessages.WRONG_NAME);
            return false;
        }
        if(!validateAge(guestEntity.getAge())){
            System.out.println(ServiceMessages.WRONG_AGE);
            return false;
        }

        if (!validateAddress(guestEntity.getAddress())){
            System.out.println(ServiceMessages.WRONG_ADDRESS);
            return false;
        }

        if(!validatePassportNumber(guestEntity.getPassportNumber())){
            System.out.println(ServiceMessages.WRONG_PASSPORT);
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
            System.out.println(ServiceMessages.WRONG_COUNT_NUMBER_PASSPORT);
            return false;
        }
    }

    private boolean validateAddress(String address){

        return address != null && address.length() <= 20;
    }
}

