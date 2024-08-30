package transport.client.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MonitoringSocketClientImpl {

    public static void sendEvent(String eventType, String message) throws IOException {
        Socket socket = new Socket("localhost", 5555);
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(eventType + ";" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
