package util;

import repository.entity.BookingEntity;
import repository.entity.GuestEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class CsvParser {
    private final String filePath;;

    public CsvParser(String filePath) {
        this.filePath = filePath;
    }

    public void saveGuests(List<GuestEntity> guests) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,Name,Age,PassportNumber,Address");
            writer.newLine();

            if (!guests.isEmpty()) {
                for (GuestEntity guest : guests) {

                    writer.write(guest.getId() + "," + guest.getName() + "," + guest.getAge() + "," + guest.getPassportNumber()
                            + "," + guest.getAddress());
                    writer.newLine();
                }
                System.out.println("Данные успешно записаны в " + filePath);
            }
            else {
                System.out.println("Данные в файл " + filePath + " записать не удалось");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<GuestEntity> loadGuests() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        List<GuestEntity> guests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            if ((line = reader.readLine()) != null) {

            }
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 5) {
                    int id = Integer.parseInt(fields[0]);
                    GuestEntity guest = new GuestEntity(fields[1], Integer.parseInt(fields[2]), fields[3], fields[4]);
                    guest.setId(id);


                    guests.add(guest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guests;
    }


    public void saveBooking(List<BookingEntity> bookingEntities) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID, GuestID, HotelID");
            writer.newLine();

            if (!bookingEntities.isEmpty()) {
                for (BookingEntity bookingEntity : bookingEntities) {

                    writer.write(bookingEntity.getId() + "," +bookingEntity.getGuestId() + ","
                            + bookingEntity.getHotelId());
                    writer.newLine();
                }
                System.out.println("Данные успешно записаны в " + filePath);
            }
            else {
                System.out.println("Данные в файл " + filePath + " записать не удалось");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BookingEntity> loadBookings() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        List<BookingEntity> bookingEntities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            if ((line = reader.readLine()) != null) {
            }
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    int id = Integer.parseInt(fields[0]);
                    BookingEntity booking = new BookingEntity(Integer.parseInt(fields[1]), Integer.parseInt(fields[2]));
                    booking.setId(id);


                    bookingEntities.add(booking);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingEntities;
    }

}
