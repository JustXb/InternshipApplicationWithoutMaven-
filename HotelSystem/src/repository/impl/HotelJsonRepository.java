package repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonView;
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

}
