package transport.dto.request;

public class MonitoringRequestDto  extends RequestDto {

    public MonitoringRequestDto(int id, String name, int age, String passportNumber, String address) {
        super( name, age, passportNumber, address);
    }
}
