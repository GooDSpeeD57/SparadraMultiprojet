package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {

    protected abstract String getTableName();
    protected abstract String getPrimaryKey();
    protected abstract PreparedStatement prepareInsert(T obj, Connection conn) throws Exception;
    protected abstract PreparedStatement prepareUpdate(T obj, Connection conn) throws Exception;



    public boolean insert(T obj) {
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = prepareInsert(obj, conn)) {

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.err.println("Erreur INSERT dans " + getTableName() + " : " + e.getMessage());
            return false;
        }
    }

    public boolean update(T obj) {
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = prepareUpdate(obj, conn)) {

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.err.println("Erreur UPDATE dans " + getTableName() + " : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKey() + "=?";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Erreur DELETE dans " + getTableName() + " : " + e.getMessage());
            return false;
        }
    }


    public ResultSet findById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKey() + "=?";
        try {
            Connection conn = DCSingletonHikaricp.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            return pst.executeQuery();
        } catch (Exception e) {
            System.err.println("Erreur findByIdRaw dans " + getTableName() + " : " + e.getMessage());
        }
        return null;
    }

    public ResultSet findAll() {
        String sql = "SELECT * FROM " + getTableName();
        try {
            Connection conn = DCSingletonHikaricp.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            return pst.executeQuery();
        } catch (Exception e) {
            System.err.println("Erreur findAllRaw dans " + getTableName() + " : " + e.getMessage());
        }
        return null;
    }
}
