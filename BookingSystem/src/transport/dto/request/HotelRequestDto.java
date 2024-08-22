package transport.dto.request;

public class HotelRequestDto {
    private int hotelId; // ID выбранного отеля

    // Конструкторы, геттеры и сеттеры
    public HotelRequestDto(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}
