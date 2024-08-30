package repository.impl;

import repository.Repository;
import repository.entity.GuestEntity;
import util.CsvParser;
import util.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestCsvRepository implements Repository {
    private final CsvParser csvParser;
    private final Mapper MAPPER = new Mapper();



    private List<GuestEntity> guests = new ArrayList<>();


    public GuestCsvRepository(String csvFilePath)  {
        this.csvParser=new CsvParser(csvFilePath);
    }

    @Override
    public void create(GuestEntity guestEntity) throws IOException {
        guests = csvParser.loadGuests();
        int id = guests.size();
        guestEntity.setId(++id);
        guests.add(guestEntity);
        csvParser.saveGuests(guests);
        for (GuestEntity s : guests) {
            s.getInfo();
        }
    }

    @Override
    public GuestEntity read(Integer id) throws IOException {
        List<GuestEntity> guests = csvParser.loadGuests();
        for (GuestEntity guest : guests) {
            if (id == guest.getId()) {
                return guest;
            }
        }
        return null;
    }

    public List<GuestEntity> getGuests() throws IOException {
        List<GuestEntity> guests = csvParser.loadGuests();
        return guests;
    }

    @Override
    public boolean update(GuestEntity guestEntity) throws IOException {
        List<GuestEntity> guests = csvParser.loadGuests();
////        for (int i = 0; i < guests.size(); i++) {
////            if (guests.get(i).getId().equals(entity.getId())) {
////                guests.set(i, entity);
////                break;
//            }
//        }
//        csvParser.saveGuests(guests);
       return true;
    }

    @Override
    public boolean delete(Integer id) {
//        //List<Entity> guests = csvParser.loadGuests();
//        guests.removeIf(guest -> guest.getId().equals(id));
//        csvParser.saveGuests(guests);
        return true;
    }

}
