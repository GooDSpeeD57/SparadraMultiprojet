package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Regime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegimeDAO extends AbstractDAO<Regime> {

    @Override
    protected String getTableName() {
        return "regime";
    }

    @Override
    protected String getPrimaryKey() {
        return "idRegime";
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
        String sql = "INSERT INTO regime (nomRegime, tauxRemboursement) VALUES (?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, r.getNomRegime());
        pst.setDouble(2, r.getTauxRemboursement());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Regime r, Connection conn) throws Exception {
        String sql = "UPDATE regime SET nomRegime=?, tauxRemboursement=? WHERE idRegime=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, r.getNomRegime());
        pst.setDouble(2, r.getTauxRemboursement());
        pst.setInt(3, r.getIdRegime());
        return pst;
    }

    public Regime findById(int idRegime) {
        String sql = "SELECT * FROM regime WHERE idRegime=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idRegime);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}