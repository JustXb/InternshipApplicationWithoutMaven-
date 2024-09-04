package util;

import repository.entity.BookingEntity;
import repository.entity.GuestEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class CsvParser {
    private final String filePath;
    private final String PATTERN_GUEST_CSV = "ID,Name,Age,PassportNumber,Address";
    private final String PATTERN_BOOKINGS_CSV = "ID, GuestID, HotelID";
    private final Logger LOGGER = Logger.getLogger(CsvParser.class.getName());

    public CsvParser(String filePath) {
        this.filePath = filePath;
    }

    public void saveGuests(List<GuestEntity> guests) throws IOException {
        checkIfFileExist();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(PATTERN_GUEST_CSV);
            writer.newLine();

            if (!guests.isEmpty()) {
                for (GuestEntity guest : guests) {

                    writer.write(guest.getId() + "," + guest.getName() + "," + guest.getAge() + "," + guest.getPassportNumber()
                            + "," + guest.getAddress());
                    writer.newLine();
                }
                LOGGER.info("Данные успешно записаны в " + filePath);
            }
            else {
                LOGGER.info("Данные в файл " + filePath + " записать не удалось");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<GuestEntity> loadGuests() throws IOException {
        checkIfFileExist();
        List<GuestEntity> guests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String readHeaderOfCsv = reader.readLine();

            String dataline;

            while ((dataline = reader.readLine()) != null) {
                String[] fields = dataline.split(",");
                if (fields.length == 5) {
                    GuestEntity guest = new GuestEntity(Integer.parseInt(fields[0]) ,fields[1],
                            Integer.parseInt(fields[2]), fields[3], fields[4]);
                    guests.add(guest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guests;
    }


    public void saveBooking(List<BookingEntity> bookingEntities) throws IOException {
        checkIfFileExist();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(PATTERN_BOOKINGS_CSV);
            writer.newLine();

            if (!bookingEntities.isEmpty()) {
                for (BookingEntity bookingEntity : bookingEntities) {

                    writer.write(bookingEntity.getId() + "," +bookingEntity.getGuestId() + ","
                            + bookingEntity.getHotelId());
                    writer.newLine();
                }

                LOGGER.info("Данные успешно записаны в " + filePath);
            }
            else {
                LOGGER.info("Данные в файл " + filePath + " записать не удалось");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfFileExist() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public List<BookingEntity> loadBookings() throws IOException {
        checkIfFileExist();
        List<BookingEntity> bookingEntities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String readHeaderOfCsv = reader.readLine();
            String dataline;
            while ((dataline = reader.readLine()) != null) {
                String[] fields = dataline.split(",");
                if (fields.length == 3) {
                    BookingEntity booking = new BookingEntity(Integer.parseInt(fields[0]),Integer.parseInt(fields[1]),
                            Integer.parseInt(fields[2]));
                    bookingEntities.add(booking);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingEntities;
    }

}

//
