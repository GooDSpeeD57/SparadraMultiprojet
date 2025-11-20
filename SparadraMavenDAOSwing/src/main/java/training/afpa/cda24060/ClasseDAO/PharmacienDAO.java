package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.modele.Pharmacien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PharmacienDAO extends AbstractDAO<Pharmacien> {

    @Override
    protected String getTableName() {
        return "pharmacien";
    }

    @Override
    protected String getPrimaryKey() {
        return "idPharmacien";
    }

    @Override
    protected Pharmacien map(ResultSet rs) throws Exception {
        Pharmacien p = new Pharmacien();
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setRPPS(rs.getString("rPPS"));
        // si tu as un idPharmacien en BDD, tu peux ajouter un setter ici
        return p;
    }

    @Override
    protected PreparedStatement prepareInsert(Pharmacien p, Connection conn) throws Exception {
        String sql = "INSERT INTO pharmacien (nom, prenom, rPPS) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, p.getNom());
        pst.setString(2, p.getPrenom());
        pst.setString(3, p.getRPPS());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Pharmacien p, Connection conn) throws Exception {
        String sql = "UPDATE pharmacien SET nom=?, prenom=?, rPPS=? WHERE idPharmacien=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, p.getNom());
        pst.setString(2, p.getPrenom());
        pst.setString(3, p.getRPPS());
        // pst.setInt(4, p.getIdPharmacien()); // si tu as l'ID dans la classe
        return pst;
    }
}