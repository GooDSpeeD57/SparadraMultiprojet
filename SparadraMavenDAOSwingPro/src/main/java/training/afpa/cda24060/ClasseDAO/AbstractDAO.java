package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.connection.Singleton_HikariCP;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<T> {

    protected Connection getConnection() throws SQLException {
        return Singleton_HikariCP.getInstanceDB();
    }

    public abstract boolean insert(T obj) throws SQLException;
    public abstract boolean update(T obj) throws SQLException;
    public abstract boolean delete(Integer id) throws SQLException;
    public abstract T findById(Integer id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
}