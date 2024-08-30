package repository.entity;

public class HotelAvailablilityEntity {
    private int id;
    private int availability;

    public HotelAvailablilityEntity(int id, int availability) {
        this.id = id;
        this.availability = availability;
    }
    public HotelAvailablilityEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public void decreaseAvailableRooms() {
        if (availability > 0) {
            availability--;
        } else {
            throw new IllegalStateException("Нет доступных мест");
        }
    }
}



