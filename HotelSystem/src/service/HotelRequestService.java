package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import repository.entity.HotelEntity;
import repository.impl.HotelJsonRepository;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HotelRequestService {
    private static final int PORT = 12345;
    private static HotelJsonRepository hotelJsonRepository = new HotelJsonRepository();

    public void responseHotels() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            List<HotelEntity> hotels = hotelJsonRepository.loadHotelsFromFile();
            System.out.println("Сервис гостиницы запущен и ожидает подключения...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    out.println(hotels.toString());

                    int hotelId = Integer.parseInt(in.readLine());
                    System.out.println("Запрос на доступность гостиницы: " + hotelId);

                    // Логика проверки доступности гостиницы
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

    private static boolean isHotelAvailable(int id) throws IOException {
        List<HotelEntity> hotels;
        ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(new File("hotels.json"), new TypeReference<List<HotelEntity>>() {});
        List<HotelEntity> hotelsAvailability = objectMapper.readValue(new File("HotelsAvailability.json"),
                new TypeReference<List<HotelEntity>>() {});
        for (HotelEntity hotel : hotels) {
            if (hotel.getId() == id) {
                for (HotelEntity hotelAvailability : hotelsAvailability) {
                    if (hotel.getId() == hotelAvailability.getId()) {
                        hotelAvailability.decreaseAvailableRooms();
                        objectMapper.writeValue(new File("HotelsAvailability.json"), hotelsAvailability);
                        return true;
                    }
                }
            }
        }
        return false; // или false, в зависимости от логики
    }

    /*public void decreaseAvailableRooms (int id) throws IOException {
        List<HotelApplicationEntity> hotels;
        ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(new File("hotelsAvialability.json"), new TypeReference<List<HotelApplicationEntity>>() {});

        if (available > 0) {
            available--;
        } else {
            throw new IllegalStateException("Нет доступных мест");
        }*/
    //}

}
