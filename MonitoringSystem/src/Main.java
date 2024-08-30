import service.MonitoringService;
import transport.server.MonitoringServer;

import javax.management.monitor.Monitor;

public class Main {
    public static void main(String[] args) {
        MonitoringService monitoringService = new MonitoringService();
        MonitoringServer monitoringServer = new MonitoringServer();
        monitoringServer.start();
    }
}