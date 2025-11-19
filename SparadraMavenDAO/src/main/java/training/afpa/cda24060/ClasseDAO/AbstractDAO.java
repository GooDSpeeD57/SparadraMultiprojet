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
    protected abstract T map(ResultSet rs) throws Exception;
    protected abstract PreparedStatement prepareInsert(T obj, Connection conn) throws Exception;
    protected abstract PreparedStatement prepareUpdate(T obj, Connection conn) throws Exception;

    // ======================================================
    //  CRUD GÉNÉRIQUE
    // ======================================================

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

    public T findById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKey() + "=?";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur findById dans " + getTableName() + " : " + e.getMessage());
        }

        return null;
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            System.err.println("Erreur findAll dans " + getTableName() + " : " + e.getMessage());
        }

        return list;
    }
}
