package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Mutuelle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MutuelleDAO extends AbstractDAO<Mutuelle> {

    @Override
    protected String getTableName() {
        return "Mutuelle";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Mutuelle";
    }

    @Override
    protected Mutuelle map(ResultSet rs) {
        try {
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
            return m;

        } catch (Exception e) {
            System.err.println("Erreur mapping Mutuelle : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Mutuelle m, Connection conn) {
        try {
            String sql = "INSERT INTO Mutuelle (" +
                    "nomMutuelle, adresseMutuelle, codePostalMutuelle, villeMutuelle, " +
                    "telephoneMutuelle, mailMutuelle, departementMutuelle, tRemboursement" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, m.getNomMutuelle());
            pst.setString(2, m.getAdresseMutuelle());
            pst.setString(3, m.getCodePostalMutuelle());
            pst.setString(4, m.getVilleMutuelle());
            pst.setString(5, m.getTelephoneMutuelle());
            pst.setString(6, m.getMailMutuelle());
            pst.setString(7, m.getDepartementMutuelle());
            pst.setDouble(8, m.getTRemboursement());
            return pst;

        } catch (Exception e) {
            System.err.println("Erreur prepareInsert Mutuelle : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Mutuelle m, Connection conn) {
        try {
            String sql = "UPDATE Mutuelle SET " +
                    "nomMutuelle=?, adresseMutuelle=?, codePostalMutuelle=?, villeMutuelle=?, " +
                    "telephoneMutuelle=?, mailMutuelle=?, departementMutuelle=?, tRemboursement=? " +
                    "WHERE id_Mutuelle=?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, m.getNomMutuelle());
            pst.setString(2, m.getAdresseMutuelle());
            pst.setString(3, m.getCodePostalMutuelle());
            pst.setString(4, m.getVilleMutuelle());
            pst.setString(5, m.getTelephoneMutuelle());
            pst.setString(6, m.getMailMutuelle());
            pst.setString(7, m.getDepartementMutuelle());
            pst.setDouble(8, m.getTRemboursement());
            pst.setInt(9, m.getIdMutuelle());
            return pst;

        } catch (Exception e) {
            System.err.println("Erreur prepareUpdate Mutuelle : " + e.getMessage());
            return null;
        }
    }

    public Mutuelle findById(int idMutuelle) {
        String sql = "SELECT * FROM Mutuelle WHERE id_Mutuelle=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMutuelle);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }

        } catch (Exception e) {
            System.err.println("Erreur findById Mutuelle : " + e.getMessage());
        }
        return null;
    }

    public List<Mutuelle> findByNom(String nom) {
        List<Mutuelle> liste = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE nomMutuelle LIKE ?";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }

        } catch (Exception e) {
            System.err.println("Erreur findByNom Mutuelle : " + e.getMessage());
        }
        return liste;
    }

    public List<Mutuelle> findByDepartement(String departement) {
        List<Mutuelle> liste = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE departementMutuelle LIKE ?";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + departement + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }

        } catch (Exception e) {
            System.err.println("Erreur findByDepartement Mutuelle : " + e.getMessage());
        }
        return liste;
    }

    public int countMutuelles() {
        String sql = "SELECT COUNT(*) AS total FROM Mutuelle";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (Exception e) {
            System.err.println("Erreur countMutuelles : " + e.getMessage());
        }
        return 0;
    }
}
