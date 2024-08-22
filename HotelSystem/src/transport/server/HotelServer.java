package transport.server;

import repository.entity.HotelEntity;
import repository.impl.HotelJsonRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HotelServer {
    private static final int PORT = 12345;
    private static HotelJsonRepository hotelJsonRepository = new HotelJsonRepository();


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервис гостиницы запущен и ожидает подключения...");
            List<HotelEntity> hotels = hotelJsonRepository.loadHotelsFromFile();

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
