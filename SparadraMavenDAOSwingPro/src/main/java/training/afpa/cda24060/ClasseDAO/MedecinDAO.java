package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO extends AbstractDAO<Medecin> {

    private static final Logger logger = LoggerFactory.getLogger(MedecinDAO.class);

    @Override
    public boolean insert(Medecin medecin) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Medecin (");
        sb.append("nomMedecin, prenomMedecin, adresseMedecin, codePostalMedecin, villeMedecin, ");
        sb.append("telephoneMedecin, mailMedecin, rppsMedecin");
        sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

        String sql = sb.toString();

        try (Connection con = getConnection();
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

            LogUtils.debug(logger, "Médecin inséré : " + medecin);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Médecin : " + medecin, e);
            throw e;
        }
    }

    @Override
    public boolean update(Medecin medecin) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Medecin SET ");
        sb.append("nomMedecin=?, prenomMedecin=?, adresseMedecin=?, codePostalMedecin=?, villeMedecin=?, ");
        sb.append("telephoneMedecin=?, mailMedecin=?, rppsMedecin=? ");
        sb.append("WHERE id_Medecin=?;");

        String sql = sb.toString();

        try (Connection con = getConnection();
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
            LogUtils.debug(logger, "Médecin mis à jour : " + medecin + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Médecin : " + medecin, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Medecin WHERE id_Medecin=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Médecin supprimé id=" + id + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Médecin id=" + id, e);
            throw e;
        }
    }

    @Override
    public Medecin findById(Integer id) throws SQLException {
        Medecin medecin = null;
        String sql = "SELECT * FROM Medecin WHERE id_Medecin=?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    medecin = new Medecin();
                    medecin.setId(rs.getInt("id_Medecin"));
                    medecin.setNom(rs.getString("nomMedecin"));
                    medecin.setPrenom(rs.getString("prenomMedecin"));
                    medecin.setAdresse(rs.getString("adresseMedecin"));
                    medecin.setCodePostal(rs.getString("codePostalMedecin"));
                    medecin.setVille(rs.getString("villeMedecin"));
                    medecin.setTelephone(rs.getString("telephoneMedecin"));
                    medecin.setEmail(rs.getString("mailMedecin"));
                    medecin.setRpps(rs.getString("rppsMedecin"));

                    LogUtils.debug(logger, "Médecin trouvé id=" + id + " : " + medecin);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return medecin;
    }

    @Override
    public List<Medecin> findAll() throws SQLException {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM Medecin";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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

                medecins.add(m);
            }

            LogUtils.debug(logger, "Total médecins trouvés : " + medecins.size());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return medecins;
    }

    public Medecin findByRPPS(String rpps) {
        Medecin medecin = null;
        String sql = "SELECT * FROM Medecin WHERE rppsMedecin=?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, rpps);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    medecin = new Medecin();
                    medecin.setId(rs.getInt("id_Medecin"));
                    medecin.setNom(rs.getString("nomMedecin"));
                    medecin.setPrenom(rs.getString("prenomMedecin"));
                    medecin.setAdresse(rs.getString("adresseMedecin"));
                    medecin.setCodePostal(rs.getString("codePostalMedecin"));
                    medecin.setVille(rs.getString("villeMedecin"));
                    medecin.setTelephone(rs.getString("telephoneMedecin"));
                    medecin.setEmail(rs.getString("mailMedecin"));
                    medecin.setRpps(rs.getString("rppsMedecin"));

                    LogUtils.debug(logger, "Médecin trouvé RPPS=" + rpps + " : " + medecin);
                }
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByRPPS Médecin rpps=" + rpps, e);
        }

        return medecin;
    }

    public List<Medecin> findByNom(String nom) {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM Medecin WHERE nomMedecin LIKE ?";

        try (Connection con = getConnection();
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

                    medecins.add(m);
                }
            }
            LogUtils.debug(logger, "Médecins trouvés avec nom LIKE '" + nom + "' : " + medecins.size());
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByNom Médecin nom=" + nom, e);
        }

        return medecins;
    }
    public int countMedecins() {
        String sql = "SELECT COUNT(*) AS total FROM Medecin";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur countMedecins", e);
        }
        return 0;
    }
}
