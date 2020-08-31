package datastorage;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    void create(T t) throws SQLException;

    T read(int key) throws SQLException;

    List<T> readAll() throws SQLException;

    void update(T t) throws SQLException;

    void deleteById(int key) throws SQLException;
}
