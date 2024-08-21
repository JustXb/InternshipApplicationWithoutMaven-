package transport.dto.request;

public class HotelRequestDto extends RequestDto {
    private int hotelId;

    public HotelRequestDto(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }
}
