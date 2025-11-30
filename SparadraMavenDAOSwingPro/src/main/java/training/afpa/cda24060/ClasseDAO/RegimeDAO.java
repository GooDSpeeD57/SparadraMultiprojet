package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.modele.Regime;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.exception.SaisieException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegimeDAO extends AbstractDAO<Regime> {

    private static final Logger logger = LoggerFactory.getLogger(RegimeDAO.class);

    @Override
    public boolean insert(Regime r) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Regime (nomRegime, tauxRemboursement) VALUES (?, ?);");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, r.getNomRegime());
            pst.setDouble(2, r.getTauxRemboursement());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) r.setIdRegime(rs.getInt(1));
            }

            LogUtils.debug(logger, "Régime inséré : " + r);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Régime : " + r, e);
            throw e;
        }
    }

    @Override
    public boolean update(Regime r) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Regime SET nomRegime=?, tauxRemboursement=? WHERE id_Regime=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, r.getNomRegime());
            pst.setDouble(2, r.getTauxRemboursement());
            pst.setInt(3, r.getIdRegime());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Régime mis à jour : " + r + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Régime : " + r, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Regime WHERE id_Regime=?;";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Régime supprimé id=" + id + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Régime id=" + id, e);
            throw e;
        }
    }

    @Override
    public Regime findById(Integer id) throws SQLException {
        Regime r = null;
        String sql = "SELECT * FROM Regime WHERE id_Regime=?;";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    r = new Regime();
                    r.setIdRegime(rs.getInt("id_Regime"));
                    r.setNomRegime(rs.getString("nomRegime"));
                    r.setTauxRemboursement(rs.getDouble("tauxRemboursement"));
                    LogUtils.debug(logger, "Régime trouvé id=" + id + " : " + r);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return r;
    }

    @Override
    public List<Regime> findAll() throws SQLException {
        List<Regime> list = new ArrayList<>();
        String sql = "SELECT * FROM Regime;";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Regime r = new Regime();
                r.setIdRegime(rs.getInt("id_Regime"));
                r.setNomRegime(rs.getString("nomRegime"));
                r.setTauxRemboursement(rs.getDouble("tauxRemboursement"));
                list.add(r);
            }
            LogUtils.debug(logger, "Total régimes trouvés : " + list.size());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Regime> findByNom(String nom) throws SQLException {
        List<Regime> list = new ArrayList<>();
        String sql = "SELECT * FROM Regime WHERE nomRegime LIKE ?;";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Regime r = new Regime();
                    r.setIdRegime(rs.getInt("id_Regime"));
                    r.setNomRegime(rs.getString("nomRegime"));
                    r.setTauxRemboursement(rs.getDouble("tauxRemboursement"));
                    list.add(r);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
            LogUtils.debug(logger, "Régimes trouvés avec nom LIKE '" + nom + "' : " + list.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByNom Régime nom=" + nom, e);
            throw e;
        }
        return list;
    }

    public int countRegimes() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Regime;";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur countRegimes", e);
            throw e;
        }
        return 0;
    }
}
