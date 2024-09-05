package repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonView;
import repository.entity.HotelAvailablilityEntity;
import repository.entity.HotelEntity;

import java.io.IOException;


import java.io.File;
import java.util.List;

public class HotelJsonRepository {

    private final String hotelsfilepath;

    public HotelJsonRepository(String hotelsfilepath) {
        this.hotelsfilepath = hotelsfilepath;

    }

    public List<HotelEntity> loadHotelsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(hotelsfilepath), new TypeReference<List<HotelEntity>>() {
        });
    }

    public HotelAvailablilityEntity readHotelFromFile(int id) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("HotelsAvailability.json");

        // Чтение списка отелей из файла
        List<HotelAvailablilityEntity> hotels = objectMapper.readValue(file, new TypeReference<List<HotelAvailablilityEntity>>() {});
        for (HotelAvailablilityEntity hotel : hotels)
            if (hotel.getId() == id) {
                return hotel; // Возвращаем найденный отель
            }
        return null;
    }

}
