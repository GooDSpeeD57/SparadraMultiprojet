package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.modele.Pharmacien;

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
    protected Pharmacien map(ResultSet rs) throws Exception {
        Pharmacien p = new Pharmacien();
        p.setNom(rs.getString("nomPharmacien"));
        p.setPrenom(rs.getString("prenomPharmacien"));
        p.setRPPS(rs.getString("rPPSPharmacien"));
        // si tu as un idPharmacien en BDD, tu peux ajouter un setter ici
        return p;
    }

    @Override
    protected PreparedStatement prepareInsert(Pharmacien p, Connection conn) throws Exception {
        String sql = "INSERT INTO pharmacien (nomPharmacien, prenomPharmacien, rppsPharmacien) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, p.getNom());
        pst.setString(2, p.getPrenom());
        pst.setString(3, p.getRPPS());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Pharmacien p, Connection conn) throws Exception {
        String sql = "UPDATE pharmacien SET nomPharmacien=?, prenomPharmacien=?, rPPSPharmacien=? WHERE idPharmacien=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, p.getNom());
        pst.setString(2, p.getPrenom());
        pst.setString(3, p.getRPPS());
        // pst.setInt(4, p.getIdPharmacien()); // si tu as l'ID dans la classe
        return pst;
    }
}