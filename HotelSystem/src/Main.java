
import repository.impl.HotelJsonRepository;
import service.HotelService;
import transport.server.HotelServer;
import util.Config;

import java.io.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();

        HotelJsonRepository hotelJsonRepository = new HotelJsonRepository(config.getHotelsPath());
        HotelServer hotelServer = new HotelServer(hotelJsonRepository);
        HotelService hotelService = new HotelService(hotelJsonRepository, hotelServer);
        hotelService.responseHotels();
    }


}