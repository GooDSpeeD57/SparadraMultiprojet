package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MutuelleDAO {

    private static final Logger logger = LoggerFactory.getLogger(MutuelleDAO.class);

    public boolean insert(Mutuelle m) throws SQLException {
        String sql = "INSERT INTO Mutuelle (nomMutuelle, adresseMutuelle, codePostalMutuelle, villeMutuelle, telephoneMutuelle, mailMutuelle, departementMutuelle, tRemboursement) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, m.getNomMutuelle());
            pst.setString(2, m.getAdresseMutuelle());
            pst.setString(3, m.getCodePostalMutuelle());
            pst.setString(4, m.getVilleMutuelle());
            pst.setString(5, m.getTelephoneMutuelle());
            pst.setString(6, m.getMailMutuelle());
            pst.setString(7, m.getDepartementMutuelle());
            pst.setDouble(8, m.getTRemboursement());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) m.setIdMutuelle(rs.getInt(1));
            }

            LogUtils.debug(logger, "Mutuelle insérée : " + m);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Mutuelle : " + m, e);
            throw e;
        }
    }

    public boolean update(Mutuelle m) throws SQLException {
        String sql = "UPDATE Mutuelle SET nomMutuelle=?, adresseMutuelle=?, codePostalMutuelle=?, villeMutuelle=?, telephoneMutuelle=?, mailMutuelle=?, departementMutuelle=?, tRemboursement=? WHERE id_Mutuelle=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, m.getNomMutuelle());
            pst.setString(2, m.getAdresseMutuelle());
            pst.setString(3, m.getCodePostalMutuelle());
            pst.setString(4, m.getVilleMutuelle());
            pst.setString(5, m.getTelephoneMutuelle());
            pst.setString(6, m.getMailMutuelle());
            pst.setString(7, m.getDepartementMutuelle());
            pst.setDouble(8, m.getTRemboursement());
            pst.setInt(9, m.getIdMutuelle());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Mutuelle mise à jour : " + m);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Mutuelle : " + m, e);
            throw e;
        }
    }

    public boolean delete(int idMutuelle) throws SQLException {
        String sql = "DELETE FROM Mutuelle WHERE id_Mutuelle=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMutuelle);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Mutuelle supprimée id=" + idMutuelle);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Mutuelle id=" + idMutuelle, e);
            throw e;
        }
    }

    public Mutuelle findById(int idMutuelle) {
        String sql = "SELECT * FROM Mutuelle WHERE id_Mutuelle=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMutuelle);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Mutuelle m = new Mutuelle();
                    m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    m.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
                    m.setAdresseMutuelle(rs.getString("adresseMutuelle") != null ? rs.getString("adresseMutuelle") : "");
                    m.setCodePostalMutuelle(rs.getString("codePostalMutuelle") != null ? rs.getString("codePostalMutuelle") : "");
                    m.setVilleMutuelle(rs.getString("villeMutuelle") != null ? rs.getString("villeMutuelle") : "");
                    m.setTelephoneMutuelle(rs.getString("telephoneMutuelle") != null ? rs.getString("telephoneMutuelle") : "");
                    m.setMailMutuelle(rs.getString("mailMutuelle") != null ? rs.getString("mailMutuelle") : "");
                    m.setDepartementMutuelle(rs.getString("departementMutuelle") != null ? rs.getString("departementMutuelle") : "");
                    m.setTRemboursement(rs.getDouble("tRemboursement"));

                    LogUtils.debug(logger, "Mutuelle trouvée id=" + idMutuelle + " : " + m);
                    return m;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Mutuelle id=" + idMutuelle, e);
        }
        return null;
    }

    public List<Mutuelle> findByNom(String nom) {
        List<Mutuelle> list = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE nomMutuelle LIKE ?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Mutuelle m = new Mutuelle();
                    m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    m.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
                    m.setAdresseMutuelle(rs.getString("adresseMutuelle") != null ? rs.getString("adresseMutuelle") : "");
                    m.setCodePostalMutuelle(rs.getString("codePostalMutuelle") != null ? rs.getString("codePostalMutuelle") : "");
                    m.setVilleMutuelle(rs.getString("villeMutuelle") != null ? rs.getString("villeMutuelle") : "");
                    m.setTelephoneMutuelle(rs.getString("telephoneMutuelle") != null ? rs.getString("telephoneMutuelle") : "");
                    m.setMailMutuelle(rs.getString("mailMutuelle") != null ? rs.getString("mailMutuelle") : "");
                    m.setDepartementMutuelle(rs.getString("departementMutuelle") != null ? rs.getString("departementMutuelle") : "");
                    m.setTRemboursement(rs.getDouble("tRemboursement"));

                    list.add(m);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByNom Mutuelle nom=" + nom, e);
        }
        return list;
    }

    public List<Mutuelle> findByDepartement(String departement) {
        List<Mutuelle> list = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE departementMutuelle LIKE ?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + departement + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Mutuelle m = new Mutuelle();
                    m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    m.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
                    m.setAdresseMutuelle(rs.getString("adresseMutuelle") != null ? rs.getString("adresseMutuelle") : "");
                    m.setCodePostalMutuelle(rs.getString("codePostalMutuelle") != null ? rs.getString("codePostalMutuelle") : "");
                    m.setVilleMutuelle(rs.getString("villeMutuelle") != null ? rs.getString("villeMutuelle") : "");
                    m.setTelephoneMutuelle(rs.getString("telephoneMutuelle") != null ? rs.getString("telephoneMutuelle") : "");
                    m.setMailMutuelle(rs.getString("mailMutuelle") != null ? rs.getString("mailMutuelle") : "");
                    m.setDepartementMutuelle(rs.getString("departementMutuelle") != null ? rs.getString("departementMutuelle") : "");
                    m.setTRemboursement(rs.getDouble("tRemboursement"));

                    list.add(m);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByDepartement Mutuelle departement=" + departement, e);
        }
        return list;
    }

    public List<Mutuelle> findAll() {
        List<Mutuelle> list = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Mutuelle m = new Mutuelle();
                m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                m.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
                m.setAdresseMutuelle(rs.getString("adresseMutuelle") != null ? rs.getString("adresseMutuelle") : "");
                m.setCodePostalMutuelle(rs.getString("codePostalMutuelle") != null ? rs.getString("codePostalMutuelle") : "");
                m.setVilleMutuelle(rs.getString("villeMutuelle") != null ? rs.getString("villeMutuelle") : "");
                m.setTelephoneMutuelle(rs.getString("telephoneMutuelle") != null ? rs.getString("telephoneMutuelle") : "");
                m.setMailMutuelle(rs.getString("mailMutuelle") != null ? rs.getString("mailMutuelle") : "");
                m.setDepartementMutuelle(rs.getString("departementMutuelle") != null ? rs.getString("departementMutuelle") : "");
                m.setTRemboursement(rs.getDouble("tRemboursement"));

                list.add(m);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Mutuelle", e);
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int countMutuelles() {
        String sql = "SELECT COUNT(*) AS total FROM Mutuelle";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur countMutuelles", e);
        }
        return 0;
    }
}
