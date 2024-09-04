package transport.client.impl;

import service.EventType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MonitoringSocketClientImpl {

    public void sendEvent(EventType eventType, String message) {
        try (Socket socket = new Socket("localhost", 5555);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(eventType + ";" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
