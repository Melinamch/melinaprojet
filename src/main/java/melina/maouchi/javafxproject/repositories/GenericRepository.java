package melina.maouchi.javafxproject.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    void add(T entity);
    void update(T entity);
    void delete(ID id) throws SQLException;
    Optional<T> findById(ID id);
    List<T> findAll();
}