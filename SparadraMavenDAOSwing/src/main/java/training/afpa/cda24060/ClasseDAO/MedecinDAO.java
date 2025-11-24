package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Medecin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO extends AbstractDAO<Medecin> {

    @Override
    protected String getTableName() {
        return "Medecin";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Medecin";
    }

    @Override
    protected Medecin map(ResultSet rs) {
        try {
            Medecin m = new Medecin();
            m.setIdMedecin(rs.getInt("id_Medecin"));
            m.setNom(rs.getString("nomMedecin"));
            m.setPrenom(rs.getString("prenomMedecin"));
            m.setAdresse(rs.getString("adresseMedecin"));
            m.setCodePostal(rs.getString("codePostalMedecin"));
            m.setVille(rs.getString("villeMedecin"));
            m.setTelephone(rs.getString("telephoneMedecin"));
            m.setEmail(rs.getString("mailMedecin"));
            m.setRPPS(rs.getString("rppsMedecin"));
            return m;
        } catch (Exception e) {
            System.err.println("Erreur mapping Medecin : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Medecin m, Connection conn) {
        try {
            String sql = "INSERT INTO medecin (nomMedecin, prenomMedecin, adresseMedecin, codePostalMedecin, villeMedecin, " +
                    "telephoneMedecin, mailMedecin, rppsMedecin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
        } catch (Exception e) {
            System.err.println("Erreur prepareInsert Medecin : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Medecin m, Connection conn) {
        try {
            String sql = "UPDATE medecin SET nomMedecin=?, prenomMedecin=?, adresseMedecin=?, codePostalMedecin=?, villeMedecin=?, " +
                    "telephoneMedecin=?, mailMedecin=?, rppsMedecin=? WHERE id_Medecin=?";
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
        } catch (Exception e) {
            System.err.println("Erreur prepareUpdate Medecin : " + e.getMessage());
            return null;
        }
    }

    public Medecin findById(int idMedecin) {
        String sql = "SELECT * FROM medecin WHERE id_Medecin=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idMedecin);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            System.err.println("Erreur findById Medecin : " + e.getMessage());
        }
        return null;
    }

    public List<Medecin> findByNom(String nom) {
        List<Medecin> liste = new ArrayList<>();
        String sql = "SELECT * FROM medecin WHERE nomMedecin LIKE ?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur findByNom Medecin : " + e.getMessage());
        }
        return liste;
    }

    public List<Medecin> findByRPPS(String rpps) {
        List<Medecin> liste = new ArrayList<>();
        String sql = "SELECT * FROM medecin WHERE rppsMedecin LIKE ?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + rpps + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur findByRPPS Medecin : " + e.getMessage());
        }
        return liste;
    }

    public Medecin findOneByRPPS(String rpps) {
        String sql = "SELECT * FROM medecin WHERE rppsMedecin = ?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rpps);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            System.err.println("Erreur findOneByRPPS Medecin : " + e.getMessage());
        }
        return null;
    }

    public int countMedecins() {
        String sql = "SELECT COUNT(*) AS total FROM medecin";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            System.err.println("Erreur countMedecins : " + e.getMessage());
        }
        return 0;
    }
}
