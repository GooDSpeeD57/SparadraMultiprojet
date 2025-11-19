package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medicament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MedicamentDAO extends AbstractDAO<Medicament> {

    @Override
    protected String getTableName() {
        return "medicament";
    }

    @Override
    protected String getPrimaryKey() {
        return "idMedicament";
    }

    @Override
    protected Medicament map(ResultSet rs) throws Exception {
        return new Medicament(
                rs.getInt("idMedicament"),
                rs.getString("nom"),
                rs.getString("categorie"),
                rs.getDouble("prix"),
                rs.getString("dateMiseEnCirculation"),
                rs.getInt("stock"),
                rs.getBoolean("sansOrdonnance")
        );
    }

    @Override
    protected PreparedStatement prepareInsert(Medicament med, Connection conn) throws Exception {
        String sql = "INSERT INTO medicament (nom, categorie, prix, dateMiseEnCirculation, stock, sansOrdonnance) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, med.getNomMedicament());
        pst.setString(2, med.getCategorieMedicament());
        pst.setDouble(3, med.getPrixMedicament());
        pst.setString(4, med.getDateMiseEnCirculation());
        pst.setInt(5, med.getQuantiteMedicament());
        pst.setBoolean(6, med.isSansOrdonnanceMedicament());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Medicament med, Connection conn) throws Exception {
        String sql = "UPDATE medicament SET nom=?, categorie=?, prix=?, dateMiseEnCirculation=?, stock=?, sansOrdonnance=? " +
                "WHERE idMedicament=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, med.getNomMedicament());
        pst.setString(2, med.getCategorieMedicament());
        pst.setDouble(3, med.getPrixMedicament());
        pst.setString(4, med.getDateMiseEnCirculation());
        pst.setInt(5, med.getQuantiteMedicament());
        pst.setBoolean(6, med.isSansOrdonnanceMedicament());
        pst.setInt(7, med.getIdMedicament());
        return pst;
    }

    // Méthode spécifique pour retirer du stock
    public int retirerDuStock(int idMedicament, int quantite) throws SaisieException {
        Medicament med = findById(idMedicament);
        if (med == null) throw new SaisieException("Médicament introuvable !");
        if (quantite > med.getQuantiteMedicament()) throw new SaisieException("Stock insuffisant !");
        med.setQuantiteMedicament(med.getQuantiteMedicament() - quantite);
        update(med);
        return med.getQuantiteMedicament();
    }

    public int countMedicaments() {
        String sql = "SELECT COUNT(*) AS total FROM medicament";
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

