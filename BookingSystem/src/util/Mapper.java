package util;

import repository.entity.GuestEntity;
import transport.dto.request.RequestDto;

public class Mapper {
    public RequestDto toDto(GuestEntity guestEntity){
        RequestDto dto = new RequestDto();
        dto.setId(guestEntity.getId());
        dto.setName(guestEntity.getName());
        dto.setAge(guestEntity.getAge());
        dto.setAddress(guestEntity.getAddress());
        dto.setPassportNumber(guestEntity.getPassportNumber());
        return dto;
    }

    public GuestEntity toEntity(RequestDto dto){
        GuestEntity guestEntity = new GuestEntity();
        guestEntity.setId(dto.getId());
        guestEntity.setName(dto.getName());
        guestEntity.setAge(dto.getAge());
        guestEntity.setAddress(dto.getAddress());
        guestEntity.setPassportNumber(dto.getPassportNumber());
        return guestEntity;
    }
}
