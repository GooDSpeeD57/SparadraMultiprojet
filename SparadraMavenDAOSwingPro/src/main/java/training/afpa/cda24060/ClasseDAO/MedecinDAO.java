package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    private static final Logger logger = LoggerFactory.getLogger(MedecinDAO.class);

    public boolean insert(Medecin medecin) throws SQLException {
        String sql = "INSERT INTO Medecin (" +
                "nomMedecin, prenomMedecin, adresseMedecin, codePostalMedecin, villeMedecin," +
                "telephoneMedecin, mailMedecin, rppsMedecin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, medecin.getNom());
            pst.setString(2, medecin.getPrenom());
            pst.setString(3, medecin.getAdresse());
            pst.setString(4, medecin.getCodePostal());
            pst.setString(5, medecin.getVille());
            pst.setString(6, medecin.getTelephone());
            pst.setString(7, medecin.getEmail());
            pst.setString(8, medecin.getRpps());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) medecin.setId(rs.getInt(1));
            }

            LogUtils.debug(logger, "Medecin inséré : " + medecin);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Medecin : " + medecin, e);
            throw e;
        }
    }

    public boolean update(Medecin medecin) throws SQLException {
        String sql = "UPDATE Medecin SET " +
                "nomMedecin=?, prenomMedecin=?, adresseMedecin=?, codePostalMedecin=?, villeMedecin=?," +
                "telephoneMedecin=?, mailMedecin=?, rppsMedecin=? " +
                "WHERE id_Medecin=?";

        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, medecin.getNom());
            pst.setString(2, medecin.getPrenom());
            pst.setString(3, medecin.getAdresse());
            pst.setString(4, medecin.getCodePostal());
            pst.setString(5, medecin.getVille());
            pst.setString(6, medecin.getTelephone());
            pst.setString(7, medecin.getEmail());
            pst.setString(8, medecin.getRpps());
            pst.setInt(9, medecin.getId());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Medecin mis à jour : " + medecin);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Medecin : " + medecin, e);
            throw e;
        }
    }

    public boolean delete(int idMedecin) throws SQLException {
        String sql = "DELETE FROM Medecin WHERE id_Medecin=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMedecin);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Medecin supprimé id=" + idMedecin);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Medecin id=" + idMedecin, e);
            throw e;
        }
    }

    public Medecin findById(int idMedecin) {
        String sql = "SELECT * FROM Medecin WHERE id_Medecin=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMedecin);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Medecin m = new Medecin();
                    m.setId(rs.getInt("id_Medecin"));
                    m.setNom(rs.getString("nomMedecin"));
                    m.setPrenom(rs.getString("prenomMedecin"));
                    m.setAdresse(rs.getString("adresseMedecin"));
                    m.setCodePostal(rs.getString("codePostalMedecin"));
                    m.setVille(rs.getString("villeMedecin"));
                    m.setTelephone(rs.getString("telephoneMedecin"));
                    m.setEmail(rs.getString("mailMedecin"));
                    m.setRpps(rs.getString("rppsMedecin"));

                    LogUtils.debug(logger, "Medecin trouvé par id=" + idMedecin + " : " + m);
                    return m;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Medecin id=" + idMedecin, e);
        }
        return null;
    }

    public List<Medecin> findAll() {
        List<Medecin> list = new ArrayList<>();
        String sql = "SELECT * FROM Medecin";

        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Medecin m = new Medecin();
                m.setId(rs.getInt("id_Medecin"));
                m.setNom(rs.getString("nomMedecin"));
                m.setPrenom(rs.getString("prenomMedecin"));
                m.setAdresse(rs.getString("adresseMedecin"));
                m.setCodePostal(rs.getString("codePostalMedecin"));
                m.setVille(rs.getString("villeMedecin"));
                m.setTelephone(rs.getString("telephoneMedecin"));
                m.setEmail(rs.getString("mailMedecin"));
                m.setRpps(rs.getString("rppsMedecin"));

                list.add(m);
            }

            LogUtils.debug(logger, "Tous les Medecins récupérés, total=" + list.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Medecin", e);
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Medecin> findByNom(String nom) {
        List<Medecin> list = new ArrayList<>();
        String sql = "SELECT * FROM Medecin WHERE nomMedecin LIKE ?";

        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Medecin m = new Medecin();
                    m.setId(rs.getInt("id_Medecin"));
                    m.setNom(rs.getString("nomMedecin"));
                    m.setPrenom(rs.getString("prenomMedecin"));
                    m.setAdresse(rs.getString("adresseMedecin"));
                    m.setCodePostal(rs.getString("codePostalMedecin"));
                    m.setVille(rs.getString("villeMedecin"));
                    m.setTelephone(rs.getString("telephoneMedecin"));
                    m.setEmail(rs.getString("mailMedecin"));
                    m.setRpps(rs.getString("rppsMedecin"));

                    list.add(m);
                    LogUtils.debug(logger, "Medecin trouvé par nom=" + nom + " : " + m);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByNom Medecin nom=" + nom, e);
        }

        return list;
    }

    public Medecin findByRPPS(String rpps) {
        String sql = "SELECT * FROM Medecin WHERE rppsMedecin=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, rpps);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Medecin m = new Medecin();
                    m.setId(rs.getInt("id_Medecin"));
                    m.setNom(rs.getString("nomMedecin"));
                    m.setPrenom(rs.getString("prenomMedecin"));
                    m.setAdresse(rs.getString("adresseMedecin"));
                    m.setCodePostal(rs.getString("codePostalMedecin"));
                    m.setVille(rs.getString("villeMedecin"));
                    m.setTelephone(rs.getString("telephoneMedecin"));
                    m.setEmail(rs.getString("mailMedecin"));
                    m.setRpps(rs.getString("rppsMedecin"));

                    LogUtils.debug(logger, "Medecin trouvé par RPPS=" + rpps + " : " + m);
                    return m;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByRPPS Medecin rpps=" + rpps, e);
        }

        return null;
    }

    public int countMedecins() {
        String sql = "SELECT COUNT(*) AS total FROM Medecin";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                LogUtils.debug(logger, "Nombre total de Medecins=" + total);
                return total;
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur countMedecins", e);
        }
        return 0;
    }
}
