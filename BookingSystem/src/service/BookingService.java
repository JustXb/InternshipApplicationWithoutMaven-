package service;


import repository.entity.BookingEntity;
import repository.entity.GuestEntity;
import repository.impl.BookingCsvRepository;
import repository.impl.GuestCsvRepository;
import transport.client.impl.HotelSocketClientImpl;
import transport.server.GuestServer;
import util.CsvParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


public class BookingService {
    

    private static GuestCsvRepository repositoryGuest = new GuestCsvRepository("guest.csv");
    private static BookingCsvRepository repositoryBooking = new BookingCsvRepository("booking.csv");
    private static GuestServer server = new GuestServer();
    private static Scanner scanner = new Scanner(System.in);
    private static CsvParser csvParser = new CsvParser("booking.csv");



    public BookingService() {

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
        repositoryGuest.create(guest1);
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
            System.out.println("Реквест к процессингу");

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
                } else {
                    System.out.println("Гостиница " + idHotel + " недоступна. Заявка отменена.");
                }

                // Закрытие соединения
                in.close();
                out.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
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
            System.out.println("Гость " + guestId + " успешно заселен в " + hotelId);
        }


    }

    private boolean validateBooking(int guestId) throws IOException {
        List<BookingEntity> bookings = csvParser.loadBookings();
        for (BookingEntity booking : bookings) {
            if (booking.getGuestId() == guestId) {
                System.out.println("Заселение отменено: гость уже заселен в отель " + booking.getHotelId());
                return false;
            }
        }

        return true;
    }

}

