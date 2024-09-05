package util;

import repository.entity.GuestEntity;
import transport.dto.request.GuestDTO;
import transport.dto.request.RequestDto;

import java.util.UUID;

public class Mapper {
    public GuestDTO toDto(GuestEntity guestEntity){
        GuestDTO dto = new GuestDTO();
        dto.setName(guestEntity.getName());
        dto.setAge(guestEntity.getAge());
        dto.setAddress(guestEntity.getAddress());
        dto.setPassportNumber(guestEntity.getPassportNumber());
        return dto;
    }

    public GuestEntity toEntity(GuestDTO dto){
        GuestEntity guestEntity = new GuestEntity();
        guestEntity.setName(dto.getName());
        guestEntity.setAge(dto.getAge());
        guestEntity.setAddress(dto.getAddress());
        guestEntity.setPassportNumber(dto.getPassportNumber());
        return guestEntity;
    }
}
