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

            LOGGER.info("Сервис гостиницы запущен и ожидает подключения...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    out.println(result);

                    int hotelId = Integer.parseInt(in.readLine());
                    LOGGER.info("Запрос на доступность гостиницы: " + hotelId);

                    if (isHotelAvailable(hotelId)) {
                        out.println("AVAILABLE");
                    } else {
                        out.println("UNAVAILABLE");
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
            LOGGER.warning("Такого отеля не существует");
            return isExist;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(new File("hotels.json"), new TypeReference<List<HotelEntity>>() {});
        List<HotelAvailablilityEntity> hotelsAvailability = objectMapper.readValue(new File(config.getHotelsAvailabilityPath()),
                new TypeReference<List<HotelAvailablilityEntity>>() {});
        for (HotelEntity hotel : hotels) {
            if (hotel.getId() == id) {
                for (HotelAvailablilityEntity hotelAvailability : hotelsAvailability) {
                    if (hotel.getId() == hotelAvailability.getId()) {
                        hotelAvailability.decreaseAvailableRooms();
                        objectMapper.writeValue(new File(config.getHotelsAvailabilityPath()), hotelsAvailability);
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
