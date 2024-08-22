
import service.HotelService;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        HotelService hotelService = new HotelService();
        hotelService.responseHotels();
    }


}