package repository.entity;

public class BookingEntity {
    private int guestId;
    private int hotelId;

    public BookingEntity(int guestId, int hotelId) {
        this.guestId = guestId;
        this.hotelId = hotelId;
    }

    public BookingEntity(){
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}
