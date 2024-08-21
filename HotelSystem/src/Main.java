
import service.HotelRequestService;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        HotelRequestService hotelRequestService = new HotelRequestService();
        hotelRequestService.responseHotels();
    }


}