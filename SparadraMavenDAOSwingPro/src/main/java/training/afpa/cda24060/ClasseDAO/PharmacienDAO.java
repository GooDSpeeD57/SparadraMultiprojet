package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.Singleton_HikariCP;
import training.afpa.cda24060.modele.Pharmacien;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacienDAO {

    private static final Logger logger = LoggerFactory.getLogger(PharmacienDAO.class);

    public boolean insert(Pharmacien p) throws SQLException {
        String sql = "INSERT INTO Pharmacien (nomPharmacien, prenomPharmacien, rppsPharmacien) VALUES (?, ?, ?)";
        try (Connection con = Singleton_HikariCP.getInstanceDB();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getRPPS());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) p.setIdPharmacien(rs.getInt(1));
            }

            LogUtils.debug(logger, "Pharmacien inséré : " + p);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Pharmacien : " + p, e);
            throw e;
        }
    }

    public boolean update(Pharmacien p) throws SQLException {
        String sql = "UPDATE Pharmacien SET nomPharmacien=?, prenomPharmacien=?, rppsPharmacien=? WHERE id_Pharmacien=?";
        try (Connection con = Singleton_HikariCP.getInstanceDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getRPPS());
            pst.setInt(4, p.getIdPharmacien());

            boolean ok = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Pharmacien mis à jour : " + p + " -> succès=" + ok);
            return ok;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Pharmacien : " + p, e);
            throw e;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Pharmacien WHERE id_Pharmacien=?";
        try (Connection con = Singleton_HikariCP.getInstanceDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            boolean ok = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Pharmacien supprimé id=" + id + " -> succès=" + ok);
            return ok;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Pharmacien id=" + id, e);
            throw e;
        }
    }

    public Pharmacien findById(int id) throws SQLException {
        String sql = "SELECT * FROM Pharmacien WHERE id_Pharmacien=?";
        try (Connection con = Singleton_HikariCP.getInstanceDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pharmacien p = new Pharmacien();
                    p.setIdPharmacien(rs.getInt("id_Pharmacien"));
                    p.setNom(rs.getString("nomPharmacien"));
                    p.setPrenom(rs.getString("prenomPharmacien"));
                    p.setRPPS(rs.getString("rppsPharmacien"));
                    LogUtils.debug(logger, "Pharmacien trouvé id=" + id + " : " + p);
                    return p;
                }
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Pharmacien id=" + id, e);
            throw e;
        }
        return null;
    }

    public List<Pharmacien> findAll() throws SQLException {
        List<Pharmacien> pharmaciens = new ArrayList<>();
        String sql = "SELECT * FROM Pharmacien";

        try (Connection con = Singleton_HikariCP.getInstanceDB();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Pharmacien p = new Pharmacien();
                p.setIdPharmacien(rs.getInt("id_Pharmacien"));
                p.setNom(rs.getString("nomPharmacien"));
                p.setPrenom(rs.getString("prenomPharmacien"));
                p.setRPPS(rs.getString("rppsPharmacien"));
                pharmaciens.add(p);
            }
            LogUtils.debug(logger, "Total pharmaciens trouvés : " + pharmaciens.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Pharmacien", e);
            throw e;
        }
        return pharmaciens;
    }

    public List<Pharmacien> findByNom(String nom) throws SQLException {
        List<Pharmacien> pharmaciens = new ArrayList<>();
        String sql = "SELECT * FROM Pharmacien WHERE nomPharmacien=?";
        try (Connection con = Singleton_HikariCP.getInstanceDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, nom);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Pharmacien p = new Pharmacien();
                    p.setIdPharmacien(rs.getInt("id_Pharmacien"));
                    p.setNom(rs.getString("nomPharmacien"));
                    p.setPrenom(rs.getString("prenomPharmacien"));
                    p.setRPPS(rs.getString("rppsPharmacien"));
                    pharmaciens.add(p);
                }
            }
            LogUtils.debug(logger, "Pharmaciens trouvés avec nom=" + nom + " : " + pharmaciens.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByNom Pharmacien nom=" + nom, e);
            throw e;
        }
        return pharmaciens;
    }

    public Pharmacien findByRPPS(String rpps) throws SQLException {
        String sql = "SELECT * FROM Pharmacien WHERE rppsPharmacien=?";
        try (Connection con = Singleton_HikariCP.getInstanceDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, rpps);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pharmacien p = new Pharmacien();
                    p.setIdPharmacien(rs.getInt("id_Pharmacien"));
                    p.setNom(rs.getString("nomPharmacien"));
                    p.setPrenom(rs.getString("prenomPharmacien"));
                    p.setRPPS(rs.getString("rppsPharmacien"));
                    LogUtils.debug(logger, "Pharmacien trouvé RPPS=" + rpps + " : " + p);
                    return p;
                }
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByRPPS Pharmacien rpps=" + rpps, e);
            throw e;
        }
        return null;
    }
}
