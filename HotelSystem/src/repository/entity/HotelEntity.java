package repository.entity;

public class HotelEntity {
    private int id;
    private String hotelName;
    private int available;

    public HotelEntity() {
    }

    public HotelEntity(int id, String hotelName, int available) {
        this.id = id;
        this.hotelName = hotelName;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void decreaseAvailableRooms() {
        if (available > 0) {
            available--;
        } else {
            throw new IllegalStateException("Нет доступных мест");
        }
    }

    public void getInfo() {
        System.out.println("ID : " + this.id);
        System.out.println("Hotel name : " + this.hotelName);
        System.out.println("Available : " + this.available + '\n');

    }

    @Override
    public String toString() {
            return id + "," + hotelName + "," + available;

    }
}
