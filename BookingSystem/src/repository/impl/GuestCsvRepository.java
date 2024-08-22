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
        if (validate(guestEntity)){
            guests.add(guestEntity);
            csvParser.saveGuests(guests);
            for (GuestEntity s : guests) {
                s.getInfo();
            }
        }
        else {
            System.out.println("Некорректные данные");
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
            System.out.println("Неверно указано имя");
            return false;
        }
    }

    private boolean validateAge(int age){
        if(age >= 0 && age <= 120){
            return true;
        }
        else {
            System.out.println("Неверно указан возраст");
            return false;
        }
    }


    private boolean validatePassportNumber(String passportNumber) throws IOException {
        if (passportNumber != null && passportNumber.length() == 6 && passportNumber.matches("\\d{6}")) {
        boolean passportExists = true;

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
            System.out.println("Неверно указан адрес");
            return false;
        }
    }



}
