package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.exception.SaisieException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MutuelleDAO extends AbstractDAO<Mutuelle> {

    private static final Logger logger = LoggerFactory.getLogger(MutuelleDAO.class);

    @Override
    public boolean insert(Mutuelle m) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Mutuelle (");
        sb.append("nomMutuelle, adresseMutuelle, codePostalMutuelle, villeMutuelle, ");
        sb.append("telephoneMutuelle, mailMutuelle, departementMutuelle, tRemboursement");
        sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

    @Override
    public boolean update(Mutuelle m) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Mutuelle SET ");
        sb.append("nomMutuelle=?, adresseMutuelle=?, codePostalMutuelle=?, villeMutuelle=?, ");
        sb.append("telephoneMutuelle=?, mailMutuelle=?, departementMutuelle=?, tRemboursement=? ");
        sb.append("WHERE id_Mutuelle=?;");

        String sql = sb.toString();

        try (Connection con = getConnection();
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
            LogUtils.debug(logger, "Mutuelle mise à jour : " + m + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Mutuelle : " + m, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Mutuelle WHERE id_Mutuelle=?;";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Mutuelle supprimée id=" + id + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Mutuelle id=" + id, e);
            throw e;
        }
    }

    @Override
    public Mutuelle findById(Integer id) throws SQLException {
        Mutuelle m = null;
        String sql = "SELECT * FROM Mutuelle WHERE id_Mutuelle=?;";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    m = new Mutuelle();
                    m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    m.setNomMutuelle(rs.getString("nomMutuelle"));
                    m.setAdresseMutuelle(rs.getString("adresseMutuelle"));
                    m.setCodePostalMutuelle(rs.getString("codePostalMutuelle"));
                    m.setVilleMutuelle(rs.getString("villeMutuelle"));
                    m.setTelephoneMutuelle(rs.getString("telephoneMutuelle"));
                    m.setMailMutuelle(rs.getString("mailMutuelle"));
                    m.setDepartementMutuelle(rs.getString("departementMutuelle"));
                    m.setTRemboursement(rs.getDouble("tRemboursement"));
                    LogUtils.debug(logger, "Mutuelle trouvée id=" + id + " : " + m);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return m;
    }

    @Override
    public List<Mutuelle> findAll() throws SQLException {
        List<Mutuelle> list = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle;";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Mutuelle m = new Mutuelle();
                m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                m.setNomMutuelle(rs.getString("nomMutuelle"));
                m.setAdresseMutuelle(rs.getString("adresseMutuelle"));
                m.setCodePostalMutuelle(rs.getString("codePostalMutuelle"));
                m.setVilleMutuelle(rs.getString("villeMutuelle"));
                m.setTelephoneMutuelle(rs.getString("telephoneMutuelle"));
                m.setMailMutuelle(rs.getString("mailMutuelle"));
                m.setDepartementMutuelle(rs.getString("departementMutuelle"));
                m.setTRemboursement(rs.getDouble("tRemboursement"));
                list.add(m);
            }
            LogUtils.debug(logger, "Total mutuelles trouvées : " + list.size());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Mutuelle> findByNom(String nom) throws SQLException {
        List<Mutuelle> list = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE nomMutuelle LIKE ?;";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Mutuelle m = new Mutuelle();
                    m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    m.setNomMutuelle(rs.getString("nomMutuelle"));
                    m.setAdresseMutuelle(rs.getString("adresseMutuelle"));
                    m.setCodePostalMutuelle(rs.getString("codePostalMutuelle"));
                    m.setVilleMutuelle(rs.getString("villeMutuelle"));
                    m.setTelephoneMutuelle(rs.getString("telephoneMutuelle"));
                    m.setMailMutuelle(rs.getString("mailMutuelle"));
                    m.setDepartementMutuelle(rs.getString("departementMutuelle"));
                    m.setTRemboursement(rs.getDouble("tRemboursement"));
                    list.add(m);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
            LogUtils.debug(logger, "Mutuelles trouvées avec nom LIKE '" + nom + "' : " + list.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByNom Mutuelle nom=" + nom, e);
            throw e;
        }
        return list;
    }

    public List<Mutuelle> findByDepartement(String dep) throws SQLException {
        List<Mutuelle> list = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE departementMutuelle LIKE ?;";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + dep + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Mutuelle m = new Mutuelle();
                    m.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    m.setNomMutuelle(rs.getString("nomMutuelle"));
                    m.setAdresseMutuelle(rs.getString("adresseMutuelle"));
                    m.setCodePostalMutuelle(rs.getString("codePostalMutuelle"));
                    m.setVilleMutuelle(rs.getString("villeMutuelle"));
                    m.setTelephoneMutuelle(rs.getString("telephoneMutuelle"));
                    m.setMailMutuelle(rs.getString("mailMutuelle"));
                    m.setDepartementMutuelle(rs.getString("departementMutuelle"));
                    m.setTRemboursement(rs.getDouble("tRemboursement"));
                    list.add(m);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
            LogUtils.debug(logger, "Mutuelles trouvées pour departement LIKE '" + dep + "' : " + list.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByDepartement Mutuelle departement=" + dep, e);
            throw e;
        }
        return list;
    }

    public int countMutuelles() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Mutuelle;";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur countMutuelles", e);
            throw e;
        }
        return 0;
    }
}
