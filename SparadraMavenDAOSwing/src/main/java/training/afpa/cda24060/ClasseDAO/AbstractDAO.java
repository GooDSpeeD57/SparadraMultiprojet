package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);

    protected abstract String getTableName();
    protected abstract String getPrimaryKey();
    protected abstract T map(ResultSet rs) throws Exception;
    protected abstract PreparedStatement prepareInsert(T obj, Connection conn) throws Exception;
    protected abstract PreparedStatement prepareUpdate(T obj, Connection conn) throws Exception;

    public boolean insert(T obj) {
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = prepareInsert(obj, conn)) {

            int rows = pst.executeUpdate();
            LogUtils.debug(logger, "Insert " + getTableName() + " effectué pour : " + obj);
            return rows > 0;

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur INSERT dans " + getTableName(), e);
            return false;
        }
    }

    public boolean update(T obj) {
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = prepareUpdate(obj, conn)) {

            int rows = pst.executeUpdate();
            LogUtils.debug(logger, "Update " + getTableName() + " effectué pour : " + obj);
            return rows > 0;

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur UPDATE dans " + getTableName(), e);
            return false;
        }
    }

    public boolean delete(int id) {
        //noinspection UnsafeMemberAccess
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKey() + "=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            LogUtils.debug(logger, "Delete " + getTableName() + " id=" + id + " effectué, lignes supprimées=" + rows);
            return rows > 0;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur DELETE dans " + getTableName() + " id=" + id, e);
            return false;
        }
    }

    public T findById(int id) {
        //noinspection UnsafeMemberAccess
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKey() + "=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    T obj = map(rs);
                    LogUtils.debug(logger, "findById " + getTableName() + " id=" + id + " trouvé : " + obj);
                    return obj;
                }
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById dans " + getTableName() + " id=" + id, e);
        }
        return null;
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        //noinspection UnsafeMemberAccess
        String sql = "SELECT * FROM " + getTableName();
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                try {
                    T obj = map(rs);
                    list.add(obj);
                } catch (Exception e) {
                    LogUtils.error(logger, "Erreur mapping dans " + getTableName(), e);
                }
            }
            LogUtils.debug(logger, "findAll " + getTableName() + " retourné " + list.size() + " enregistrements");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll dans " + getTableName(), e);
        }
        return list;
    }
}