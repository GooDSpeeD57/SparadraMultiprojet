package training.afpa.cda24060.ClasseDAO;

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
        r.setIdRegime(rs.getInt("idRegime"));
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
}
