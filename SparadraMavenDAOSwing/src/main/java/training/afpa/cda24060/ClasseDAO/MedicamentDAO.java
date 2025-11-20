package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medicament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO extends AbstractDAO<Medicament> {

    @Override
    protected String getTableName() {
        return "Medicament";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Medicament";
    }

    @Override
    protected Medicament map(ResultSet rs) {
        try {
            Medicament med = new Medicament();
            med.setIdMedicament(rs.getInt("id_Medicament"));
            med.setNomMedicament(rs.getString("nomMedicament"));
            med.setCategorieMedicament(rs.getString("categorie"));
            med.setPrixMedicament(rs.getDouble("prix"));
            med.setDateMiseEnCirculation(rs.getDate("dateCirculation").toLocalDate());
            med.setQuantiteMedicament(rs.getInt("stock"));
            med.setSansOrdonnanceMedicament(rs.getBoolean("sansOrdonnance"));

            String forme = rs.getString("forme");
            if (forme == null || forme.isBlank()) forme = "Comprimé";
            med.setFormeMedicament(forme);

            return med;
        } catch (Exception e) {
            System.err.println("Erreur mapping Medicament : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Medicament med, Connection conn) {
        try {
            String sql = "INSERT INTO Medicament (nomMedicament, categorie, prix, dateCirculation, stock, forme, sansOrdonnance) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, med.getNomMedicament());
            pst.setString(2, med.getCategorieMedicament());
            pst.setDouble(3, med.getPrixMedicament());
            pst.setDate(4, Date.valueOf(med.getDateMiseEnCirculation()));
            pst.setInt(5, med.getQuantiteMedicament());
            pst.setString(6, med.getFormeMedicament() != null ? med.getFormeMedicament() : "Comprimé");
            pst.setBoolean(7, med.isSansOrdonnanceMedicament());
            return pst;
        } catch (Exception e) {
            System.err.println("Erreur prepareInsert Medicament : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Medicament med, Connection conn) {
        try {
            String sql = "UPDATE Medicament SET nomMedicament=?, categorie=?, prix=?, dateCirculation=?, stock=?, forme=?, sansOrdonnance=? " +
                    "WHERE id_Medicament=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, med.getNomMedicament());
            pst.setString(2, med.getCategorieMedicament());
            pst.setDouble(3, med.getPrixMedicament());
            pst.setDate(4, Date.valueOf(med.getDateMiseEnCirculation()));
            pst.setInt(5, med.getQuantiteMedicament());
            pst.setString(6, med.getFormeMedicament() != null ? med.getFormeMedicament() : "Comprimé");
            pst.setBoolean(7, med.isSansOrdonnanceMedicament());
            pst.setInt(8, med.getIdMedicament());
            return pst;
        } catch (Exception e) {
            System.err.println("Erreur prepareUpdate Medicament : " + e.getMessage());
            return null;
        }
    }

    // Retrait de stock
    public int retirerDuStock(int idMedicament, int quantite) {
        Medicament med = findById(idMedicament);
        if (med == null) {
            System.err.println("Médicament introuvable !");
            return -1;
        }
        if (quantite > med.getQuantiteMedicament()) {
            System.err.println("Stock insuffisant !");
            return -1;
        }

        try {
            med.setQuantiteMedicament(med.getQuantiteMedicament() - quantite);
            update(med);
        } catch (SaisieException e) {
            System.err.println("Erreur retrait stock : " + e.getMessage());
        }

        return med.getQuantiteMedicament();
    }

    // Recherche de médicaments par nom (partiel ou complet)
    public List<Medicament> findByName(String nom) {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM Medicament WHERE nomMedicament LIKE ?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + nom + "%"); // recherche partielle
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    liste.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByName Medicament : " + e.getMessage());
        }
        return liste;
    }


    // Compter tous les médicaments
    public int countMedicaments() {
        String sql = "SELECT COUNT(*) AS total FROM Medicament";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            System.err.println("Erreur countMedicaments : " + e.getMessage());
        }
        return 0;
    }
}