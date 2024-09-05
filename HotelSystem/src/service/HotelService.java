package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import repository.entity.HotelAvailablilityEntity;
import repository.entity.HotelEntity;
import repository.impl.HotelJsonRepository;
import transport.server.HotelServer;
import util.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class HotelService {
    private final int PORT = 12345;
    private final HotelJsonRepository hotelJsonRepository;
    private final HotelServer hotelServer;
    private final Logger LOGGER = Logger.getLogger(HotelService.class.getName());
    private final Config config = new Config();

    public HotelService(HotelJsonRepository hotelJsonRepository, HotelServer hotelServer) {
        this.hotelJsonRepository = hotelJsonRepository;
        this.hotelServer = hotelServer;
    }

    public void responseHotels() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            List<HotelEntity> hotels = hotelJsonRepository.loadHotelsFromFile();
            String result = getString(hotels);

            LOGGER.info(ServiceMessages.WAITING_CONNECT.getMessage());

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    out.println(result);

                    int hotelId = Integer.parseInt(in.readLine());
                    HotelAvailablilityEntity hotel = hotelJsonRepository.readHotelFromFile(hotelId);
                    LOGGER.info(ServiceMessages.REQUEST_HOTEL_AVAILABILITY.getMessage() + hotelId);

                    if (isHotelAvailable(hotelId)) {
                        out.println(ServiceMessages.AVAILABLE.getMessage());
                    } else {
                        if (hotel == null) {
                            out.println(ServiceMessages.UNAVAILABLE.getMessage());
                        } else {
                            if (!hotel.decreaseAvailableRooms()) {
                                out.println(ServiceMessages.UNAVAILABLE_NOAVAILABILITY.getMessage());
                            } else {
                                out.println(ServiceMessages.AVAILABLE.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getString(List<HotelEntity> hotels) {
        StringBuilder sb = new StringBuilder();
        for (HotelEntity hotelString : hotels) {
            sb.append(hotelString.toString());
            sb.append('\t');
        }
        return sb.toString();
    }

    private boolean isHotelAvailable(int id) throws IOException {
        boolean isExist = false;
        List<HotelEntity> hotels = hotelJsonRepository.loadHotelsFromFile();
        for (HotelEntity hotel : hotels) {
            if (id == hotel.getId()) {
                isExist = true;
                break;
            }
        }

        if(!isExist){
            LOGGER.warning(ServiceMessages.WRONG_HOTEL.getMessage());
            return isExist;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(new File(config.getHotelsPath()), new TypeReference<List<HotelEntity>>() {});
        List<HotelAvailablilityEntity> hotelsAvailability = objectMapper.readValue(new File(config.getHotelsAvailabilityPath()),
                new TypeReference<List<HotelAvailablilityEntity>>() {});
        for (HotelEntity hotel : hotels) {
            if (hotel.getId() == id) {
                for (HotelAvailablilityEntity hotelAvailability : hotelsAvailability) {
                    if (hotel.getId() == hotelAvailability.getId()) {
                        if(hotelAvailability.decreaseAvailableRooms()){
                            objectMapper.writeValue(new File(config.getHotelsAvailabilityPath()), hotelsAvailability);
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }



}
