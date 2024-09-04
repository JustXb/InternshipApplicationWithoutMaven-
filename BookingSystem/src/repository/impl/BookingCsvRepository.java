package repository.impl;

import repository.AbstractRepository;
import repository.entity.BookingEntity;
import util.CsvParser;
import util.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookingCsvRepository extends AbstractRepository {
    private final CsvParser csvParser;
    private final Mapper mapper = new Mapper();


    public BookingCsvRepository(String csvFilePath)  {
        this.csvParser=new CsvParser(csvFilePath);
    }

    public void create(BookingEntity bookingEntity) throws IOException {
        List<BookingEntity> bookings = csvParser.loadBookings();
        int id = bookings.size();
        bookingEntity.setId(++id);
        bookings.add(bookingEntity);
        csvParser.saveBooking(bookings);
        for (BookingEntity s : bookings) {
            s.getInfo();
        }
    }


}
