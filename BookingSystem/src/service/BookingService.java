package service;


import repository.entity.BookingEntity;
import repository.entity.GuestEntity;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import transport.client.impl.HotelSocketClientImpl;
import transport.client.impl.MonitoringSocketClientImpl;
import util.CsvParser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public class BookingService {

    private static GuestCsvRepository repositoryGuest;
    private static BookingCsvRepository repositoryBooking;
    private static Scanner scanner;
    private static CsvParser csvParserBooking;
    private static CsvParser csvParserGuest;
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());
    private static MonitoringSocketClientImpl monitoringSocketClient = new MonitoringSocketClientImpl();

    private List<GuestEntity> guests = new ArrayList<>();


    public BookingService() throws FileNotFoundException {
    }

    public BookingService(GuestCsvRepository repositoryGuest, BookingCsvRepository repositoryBooking, Scanner scanner,
                          CsvParser csvParserBooking, CsvParser csvParserGuest) {
        this.repositoryGuest = repositoryGuest;
        this.repositoryBooking = repositoryBooking;
        this.scanner = scanner;
        this.csvParserBooking = csvParserBooking;
        this.csvParserGuest = csvParserGuest;
    }

    public void createGuest() throws IOException {
        System.out.print("Enter Name : ");
        String firstName = scanner.nextLine();
        System.out.print("Enter age : ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Address : ");
        String address = scanner.nextLine();
        System.out.print("Enter passport number : ");
        String passport = scanner.nextLine();
        GuestEntity guest1 = new GuestEntity( firstName, age, passport, address);
        if(validate(guest1)){
            repositoryGuest.create(guest1);
            monitoringSocketClient.sendEvent("created", "Гость добавлен: " + guest1.toString());
        }
        else{
            monitoringSocketClient.sendEvent("mistake", "Добавление гостя " + guest1.toString()
                    + " неудачно" );
        }
    }



    public void validateDto() {

    }

    public void checkIn() {
        try{
            List<GuestEntity> guests = repositoryGuest.getGuests();
            for(GuestEntity guest : guests){
                guest.getInfo();
            }

            System.out.println("Выберите гостя по его ID");
            int idGuest = Integer.parseInt(scanner.nextLine());
            if(validateGuest(idGuest)) {
                LOGGER.info("Реквест к процессингу");

                HotelSocketClientImpl client = new HotelSocketClientImpl();

                try {
                    Socket socket = new Socket("localhost", 12345);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = in.readLine();
                    System.out.println(response);
                    System.out.println("Выберите отель");
                    int idHotel = Integer.parseInt(scanner.nextLine());
                    out.println(idHotel); // Отправка запроса о доступности гостиницы
                    response = in.readLine(); // Получение ответа
                    if ("AVAILABLE".equals(response)) {
                        bookRoom(idGuest, idHotel);
                        monitoringSocketClient.sendEvent("created", "Гость " + idGuest +
                                " заселен в отель " + idHotel);
                    } else {
                        LOGGER.warning("Такого отеля не существует");
                        monitoringSocketClient.sendEvent("mistake", "Гостиница " + idHotel +
                                " недоступна. Заявка отменена.");
                    }

                    // Закрытие соединения
                    in.close();
                    out.close();
                    socket.close();


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

    private void bookRoom(int guestId, int hotelId) throws IOException {
        if (validateBooking(guestId)){
            BookingEntity booking = new BookingEntity( guestId, hotelId);
            repositoryBooking.create(booking);
            LOGGER.info("Гость " + guestId + " успешно заселен в " + hotelId);
        }


    }


    public void connectMonitoring(){
        try {
            Socket socket = new Socket("localhost", 5555);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Выберите отель");
            int idHotel = Integer.parseInt(scanner.nextLine());
            out.println(idHotel); // Отправка запроса о доступности гостиницы


            // Закрытие соединения
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean validateBooking(int guestId) throws IOException {
        List<BookingEntity> bookings = csvParserBooking.loadBookings();
        for (BookingEntity booking : bookings) {
            if (booking.getGuestId() == guestId) {
                LOGGER.warning("Заселение отменено: гость уже заселен в отель " + booking.getHotelId());
                return false;
            }
        }
        return true;
    }

    private boolean validateGuest(int guestId) throws IOException {
        boolean isExist = false;
        List<GuestEntity> guests = csvParserGuest.loadGuests();
        for (GuestEntity guest : guests) {
            if (guestId == guest.getId()) {
                isExist = true;
            }
        }

        if(!isExist){
            LOGGER.warning("Заселение отменено: гостя с таким ID не существует");
        }

        return isExist;
    }

    public boolean validate(GuestEntity guestEntity) throws IOException {
        if(!validateName(guestEntity.getName())){
            System.out.println("Неверное имя пользователя");
            return false;
        }
        if(!validateAge(guestEntity.getAge())){
            System.out.println("Неверно указан возраст");
            return false;
        }

        if (!validateAddress(guestEntity.getAddress())){
            System.out.println("Неверно указан адрес");
            return false;
        }

        if(!validatePassportNumber(guestEntity.getPassportNumber())){
            System.out.println("Номер паспорта не валиден");
            return false;
        }
        return true;
    }

    private boolean validateName(String name){
        if(name != null && name.length() <= 20 && Character.isUpperCase(name.charAt(0))){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean validateAge(int age){
        if(age >= 0 && age <= 120){
            return true;
        }
        else {
            return false;
        }
    }


    private boolean validatePassportNumber(String passportNumber) throws IOException {
        if (passportNumber != null && passportNumber.length() == 6 && passportNumber.matches("\\d{6}")) {
            boolean passportExists = true;
            guests = csvParserGuest.loadGuests();
            for (GuestEntity guest : guests) {

                if (guest.getPassportNumber().equals(passportNumber)) {
                    passportExists = false;
                    break;

                }
            }
            return passportExists;
        } else {
            System.out.println("Номер паспорта должен содержать ровно 6 цифр.");
            return false;
        }
    }

    private boolean validateAddress(String address){

        if(address != null && address.length() <= 20){
            return true;
        }
        else {
            return false;
        }
    }
}

