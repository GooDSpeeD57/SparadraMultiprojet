package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.modele.Pharmacien;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PharmacienDAO extends AbstractDAO<Pharmacien> {

    @Override
    protected String getTableName() {
        return "Pharmacien";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Pharmacien";
    }

    @Override
    protected Pharmacien map(ResultSet rs) {
        try {
            Pharmacien p = new Pharmacien();
            p.setNom(rs.getString("nomPharmacien"));
            p.setPrenom(rs.getString("prenomPharmacien"));
            p.setRPPS(rs.getString("rppsPharmacien"));
            // si tu as un idPharmacien en BDD, tu peux ajouter un setter ici
            return p;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Erreur mapping Pharmacien");
            LogUtils.error(logger, sb.toString(), e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Pharmacien p, Connection conn) {
        try {
            String sql = "INSERT INTO pharmacien (nomPharmacien, prenomPharmacien, rppsPharmacien) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getRPPS());
            return pst;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Erreur prepareInsert Pharmacien");
            LogUtils.error(logger, sb.toString(), e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Pharmacien p, Connection conn) {
        try {
            String sql = "UPDATE pharmacien SET nomPharmacien=?, prenomPharmacien=?, rppsPharmacien=? WHERE id_Pharmacien=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getRPPS());
            // pst.setInt(4, p.getIdPharmacien()); // décommente si tu as l'ID
            return pst;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Erreur prepareUpdate Pharmacien");
            LogUtils.error(logger, sb.toString(), e);
            return null;
        }
    }

    public Pharmacien findById(int idPharmacien) {
        String sql = "SELECT * FROM Pharmacien WHERE id_Pharmacien=?";
        try (Connection conn = training.afpa.cda24060.Connection.DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPharmacien);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }

        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Erreur findById Pharmacien id=").append(idPharmacien);
            LogUtils.error(logger, sb.toString(), e);
        }
        return null;
    }
}