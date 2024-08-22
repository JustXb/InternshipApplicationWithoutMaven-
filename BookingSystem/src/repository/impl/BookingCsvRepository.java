package repository.impl;

import repository.Repository;
import repository.entity.BookingEntity;
import repository.entity.GuestEntity;
import util.CsvParser;
import util.Mapper;

import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookingCsvRepository  {
    private CsvParser csvParser;
    private Mapper mapper = new Mapper();

    private List<BookingEntity> bookings = new ArrayList<>();


    public BookingCsvRepository(String csvFilePath)  {
        this.csvParser=new CsvParser(csvFilePath);
    }


    public void create(BookingEntity bookingEntity) throws IOException {
        bookings = csvParser.loadBookings();
        int id = bookings.size();
        bookingEntity.setId(++id);
        if (validate(bookingEntity)){
            bookings.add(bookingEntity);
            csvParser.saveBooking(bookings);
            for (BookingEntity s : bookings) {
                s.getInfo();
            }
        }
        else {
            System.out.println("Некорректные данные");
        }
    }


    public GuestEntity read(Integer id) throws IOException {
        return null;
    }


    public boolean update(GuestEntity entity) throws IOException {
        return false;
    }


    public boolean delete(Integer id) {
        return false;
    }

    private boolean validate(BookingEntity bookingEntity){
        return true;
    }
}
