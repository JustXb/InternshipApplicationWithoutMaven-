package repository;

import repository.entity.Entity;
import util.CsvParser;
import util.Mapper;

import java.io.IOException;

public abstract class AbstractRepository<E extends Entity>{
    private CsvParser csvParser;
    private Mapper mapper;

    void create(E entity) throws IOException {
    }
}
