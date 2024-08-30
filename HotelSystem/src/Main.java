
import repository.impl.HotelJsonRepository;
import service.HotelService;
import transport.server.HotelServer;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        HotelJsonRepository hotelJsonRepository = new HotelJsonRepository();
        HotelServer hotelServer = new HotelServer();
        HotelService hotelService = new HotelService(hotelJsonRepository, hotelServer);
        hotelService.responseHotels();
    }


}