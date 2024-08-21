package repository;

import repository.entity.GuestEntity;

import java.io.IOException;

public interface Repository <E extends GuestEntity> {
    void create(E entity) throws IOException;
    E read(Integer id) throws IOException;
    boolean update(E entity) throws IOException;
    boolean delete(Integer id);
}
