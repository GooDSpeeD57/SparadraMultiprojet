package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Medecin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MedecinDAO extends AbstractDAO<Medecin> {

    @Override
    protected String getTableName() {
        return "medecin";
    }

    @Override
    protected String getPrimaryKey() {
        return "idMedecin";
    }

    @Override
    protected Medecin map(ResultSet rs) throws Exception {
        Medecin m = new Medecin();
        m.setIdMedecin(rs.getInt("idMedecin"));
        m.setNom(rs.getString("nom"));
        m.setPrenom(rs.getString("prenom"));
        m.setAdresse(rs.getString("adresse"));
        m.setCodePostal(rs.getString("codePostal"));
        m.setVille(rs.getString("ville"));
        m.setTelephone(rs.getString("telephone"));
        m.setEmail(rs.getString("email"));
        m.setRPPS(rs.getString("rPPS"));
        return m;
    }

    @Override
    protected PreparedStatement prepareInsert(Medecin m, Connection conn) throws Exception {
        String sql = "INSERT INTO medecin (nom, prenom, adresse, codePostal, ville, telephone, email, rPPS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, m.getNom());
        pst.setString(2, m.getPrenom());
        pst.setString(3, m.getAdresse());
        pst.setString(4, m.getCodePostal());
        pst.setString(5, m.getVille());
        pst.setString(6, m.getTelephone());
        pst.setString(7, m.getEmail());
        pst.setString(8, m.getRPPS());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Medecin m, Connection conn) throws Exception {
        String sql = "UPDATE medecin SET nom=?, prenom=?, adresse=?, codePostal=?, ville=?, telephone=?, email=?, rPPS=? WHERE idMedecin=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, m.getNom());
        pst.setString(2, m.getPrenom());
        pst.setString(3, m.getAdresse());
        pst.setString(4, m.getCodePostal());
        pst.setString(5, m.getVille());
        pst.setString(6, m.getTelephone());
        pst.setString(7, m.getEmail());
        pst.setString(8, m.getRPPS());
        pst.setInt(9, m.getIdMedecin());
        return pst;
    }
    public int countMedecins() {
        String sql = "SELECT COUNT(*) AS total FROM medecin";
        try (Connection conn = DCSingletonHikaricp.getInstanceDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}

