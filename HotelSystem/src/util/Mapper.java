package util;
import repository.entity.HotelAvailablilityEntity;
import repository.entity.HotelEntity;
import transport.dto.request.HotelAvailabilityDTO;
import transport.dto.request.HotelDTO;

public class Mapper {

    public static HotelDTO toHotelDTO(HotelEntity entity) {
        if (entity == null) {
            return null;
        }
        return new HotelDTO(
                entity.getHotelName()
        );
    }

    public static HotelEntity toHotelEntity(HotelDTO dto) {
        if (dto == null) {
            return null;
        }
        HotelEntity entity = new HotelEntity();
        entity.setHotelName(dto.getHotelName());
        return entity;
    }




    public static HotelAvailabilityDTO toHotelAvailabilityDTO(HotelAvailablilityEntity entity) {
        if (entity == null) {
            return null;
        }
        return new HotelAvailabilityDTO(
                entity.getId(),
                entity.getAvailability()
        );
    }

    public static HotelAvailablilityEntity toHotelEntity(HotelAvailabilityDTO dto) {
        if (dto == null) {
            return null;
        }
        HotelAvailablilityEntity entity = new HotelAvailablilityEntity();
        entity.setId(dto.getId());
        entity.setAvailability(dto.getAvailableRooms());
        return entity;
    }


}
