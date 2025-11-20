package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Mutuelle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MutuelleDAO extends AbstractDAO<Mutuelle> {

    @Override
    protected String getTableName() {
        return "mutuelle";
    }

    @Override
    protected String getPrimaryKey() {
        return "idMutuelle";
    }

    @Override
    protected Mutuelle map(ResultSet rs) throws Exception {
        Mutuelle m = new Mutuelle();
        m.setIdMutuelle(rs.getInt("idMutuelle"));
        m.setNom(rs.getString("nom"));
        m.setAdresse(rs.getString("adresse"));
        m.setCodePostal(rs.getString("codePostal"));
        m.setVille(rs.getString("ville"));
        m.setTelephone(rs.getString("telephone"));
        m.setEmail(rs.getString("email"));
        m.setDepartement(rs.getString("departement"));
        m.setTRemboursement(rs.getInt("tRemboursement"));
        return m;
    }

    @Override
    protected PreparedStatement prepareInsert(Mutuelle m, Connection conn) throws Exception {
        String sql = "INSERT INTO mutuelle (nom, adresse, codePostal, ville, telephone, email, departement, tRemboursement) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, m.getNom());
        pst.setString(2, m.getAdresse());
        pst.setString(3, m.getCodePostal());
        pst.setString(4, m.getVille());
        pst.setString(5, m.getTelephone());
        pst.setString(6, m.getEmail());
        pst.setString(7, m.getDepartement());
        pst.setInt(8, m.getTRemboursement());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Mutuelle m, Connection conn) throws Exception {
        String sql = "UPDATE mutuelle SET nom=?, adresse=?, codePostal=?, ville=?, telephone=?, email=?, departement=?, tRemboursement=? WHERE idMutuelle=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, m.getNom());
        pst.setString(2, m.getAdresse());
        pst.setString(3, m.getCodePostal());
        pst.setString(4, m.getVille());
        pst.setString(5, m.getTelephone());
        pst.setString(6, m.getEmail());
        pst.setString(7, m.getDepartement());
        pst.setInt(8, m.getTRemboursement());
        pst.setInt(9, m.getIdMutuelle());
        return pst;
    }
    public int countMutuelles() {
        String sql = "SELECT COUNT(*) AS total FROM mutuelle";
        try (Connection conn = DCSingletonHikaricp.getConnection();
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
