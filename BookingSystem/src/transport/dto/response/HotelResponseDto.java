package transport.dto.response;

public class HotelResponseDto {
    private int hotelId;
    private int available;

    public HotelResponseDto(int hotelId, int available) {
        this.hotelId = hotelId;
        this.available = available;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
