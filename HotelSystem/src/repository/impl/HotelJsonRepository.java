package repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonView;
import repository.entity.HotelEntity;

import java.io.IOException;


import java.io.File;
import java.util.List;

public class HotelJsonRepository {

    private List<HotelEntity> hotels;
    private static final String hotelsfilepath = "hotels.json";


    public List<HotelEntity> loadHotelsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(new File(hotelsfilepath), new TypeReference<List<HotelEntity>>() {});
        return hotels;
    }


    public void saveHotelsToFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filePath), hotels);
    }



}
