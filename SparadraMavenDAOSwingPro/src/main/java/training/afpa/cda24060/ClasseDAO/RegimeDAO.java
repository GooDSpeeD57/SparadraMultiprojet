package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Regime;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegimeDAO extends AbstractDAO<Regime> {

    private static final Logger logger = LoggerFactory.getLogger(RegimeDAO.class);

    @Override
    protected String getTableName() {
        return "Regime";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Regime";
    }

    @Override
    protected Regime map(ResultSet rs) throws Exception {
        Regime r = new Regime();
        r.setIdRegime(rs.getInt("id_Regime"));
        r.setNomRegime(rs.getString("nomRegime"));
        r.setTauxRemboursement(rs.getDouble("tauxRemboursement"));
        return r;
    }

    @Override
    protected PreparedStatement prepareInsert(Regime r, Connection conn) throws Exception {
        String sql = new SqlBuilder()
                .append("INSERT INTO regime (nomRegime, tauxRemboursement)")
                .append("VALUES (?, ?)")
                .toString();

        PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pst.setString(1, r.getNomRegime());
        pst.setDouble(2, r.getTauxRemboursement());

        LogUtils.debug(logger, "PreparedStatement insert Regime prêt pour : " + r);
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Regime r, Connection conn) throws Exception {
        String sql = new SqlBuilder()
                .append("UPDATE regime SET nomRegime=?, tauxRemboursement=? WHERE id_Regime=?")
                .toString();

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, r.getNomRegime());
        pst.setDouble(2, r.getTauxRemboursement());
        pst.setInt(3, r.getIdRegime());

        LogUtils.debug(logger, "PreparedStatement update Regime prêt pour : " + r);
        return pst;
    }

    public Regime findById(int id) {
        String sql = "SELECT * FROM regime WHERE id_Regime=?";
        LogUtils.info(logger, "Recherche du régime ID=" + id);

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    LogUtils.debug(logger, "Régime trouvé pour ID=" + id);
                    return map(rs);
                } else {
                    LogUtils.warn(logger, "Aucun régime trouvé pour ID=" + id);
                }
            }

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur lors de findById(" + id + ")", e);
        }

        return null;
    }

    public List<Regime> findByNom(String nom) {
        List<Regime> resultats = new ArrayList<>();
        String sql = "SELECT * FROM regime WHERE nomRegime LIKE ?";

        LogUtils.info(logger, "Recherche des régimes avec nom contenant : " + nom);

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    resultats.add(map(rs));
                }
            }

            LogUtils.debug(logger,
                    "Nombre de régimes trouvés pour '" + nom + "': " + resultats.size());

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur lors de findByNom(" + nom + ")", e);
        }

        return resultats;
    }

    // Optionnel : récupérer tous les régimes
    public List<Regime> findAll() {
        List<Regime> list = new ArrayList<>();
        String sql = "SELECT * FROM regime";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

            LogUtils.debug(logger, "Nombre total de régimes trouvés : " + list.size());

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Regime", e);
        }

        return list;
    }
}