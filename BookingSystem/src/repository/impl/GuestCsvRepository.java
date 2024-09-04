package repository.impl;

import repository.AbstractRepository;
import repository.entity.GuestEntity;
import util.CsvParser;
import util.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestCsvRepository extends AbstractRepository {
    private final CsvParser csvParser;
    private final Mapper MAPPER = new Mapper();


    public GuestCsvRepository(String csvFilePath)  {
        this.csvParser=new CsvParser(csvFilePath);
    }

    public void create(GuestEntity guestEntity) throws IOException {
        List<GuestEntity> guests = csvParser.loadGuests();
        int id = guests.size();
        guestEntity.setId(++id);
        guests.add(guestEntity);
        csvParser.saveGuests(guests);
        for (GuestEntity s : guests) {
            s.getInfo();
        }
    }

}
