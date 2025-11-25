package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medicament;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO extends AbstractDAO<Medicament> {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentDAO.class);

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

            Date sqlDate = rs.getDate("dateCirculation");
            if (sqlDate != null) med.setDateMiseEnCirculation(sqlDate.toLocalDate());

            med.setQuantiteMedicament(rs.getInt("stock"));
            med.setSansOrdonnanceMedicament(rs.getBoolean("sansOrdonnance"));

            String forme = rs.getString("forme");
            if (forme == null || forme.isBlank()) forme = "Comprimé";
            med.setFormeMedicament(forme);

            return med;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur mapping Medicament", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Medicament med, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("INSERT INTO Medicament (")
                    .append("nomMedicament, categorie, prix, dateCirculation, stock, forme, sansOrdonnance)")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?)")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, med.getNomMedicament());
            pst.setString(2, med.getCategorieMedicament());
            pst.setDouble(3, med.getPrixMedicament());
            pst.setDate(4, Date.valueOf(med.getDateMiseEnCirculation()));
            pst.setInt(5, med.getQuantiteMedicament());
            pst.setString(6, med.getFormeMedicament() != null ? med.getFormeMedicament() : "Comprimé");
            pst.setBoolean(7, med.isSansOrdonnanceMedicament());

            LogUtils.debug(logger, "PreparedStatement insert Medicament prêt pour : " + med);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareInsert Medicament", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Medicament med, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("UPDATE Medicament SET")
                    .append("nomMedicament=?, categorie=?, prix=?, dateCirculation=?, stock=?, forme=?, sansOrdonnance=?")
                    .append("WHERE id_Medicament=?")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, med.getNomMedicament());
            pst.setString(2, med.getCategorieMedicament());
            pst.setDouble(3, med.getPrixMedicament());
            pst.setDate(4, Date.valueOf(med.getDateMiseEnCirculation()));
            pst.setInt(5, med.getQuantiteMedicament());
            pst.setString(6, med.getFormeMedicament() != null ? med.getFormeMedicament() : "Comprimé");
            pst.setBoolean(7, med.isSansOrdonnanceMedicament());
            pst.setInt(8, med.getIdMedicament());

            LogUtils.debug(logger, "PreparedStatement update Medicament prêt pour : " + med);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareUpdate Medicament", e);
            return null;
        }
    }

    public Medicament findById(int idMedicament) {
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medicament WHERE id_Medicament=?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idMedicament);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById Medicament id=" + idMedicament, e);
        }
        return null;
    }

    public List<Medicament> findByName(String nom) {
        List<Medicament> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medicament WHERE nomMedicament LIKE ?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Medicament med = map(rs);
                    if (med != null) list.add(med);
                }
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByName Medicament nom=" + nom, e);
        }
        return list;
    }

    public List<Medicament> findAll() {
        List<Medicament> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medicament")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Medicament med = map(rs);
                if (med != null) list.add(med);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Medicament", e);
        }
        return list;
    }

    public int countMedicaments() {
        String sql = new SqlBuilder()
                .append("SELECT COUNT(*) AS total FROM Medicament")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur countMedicaments", e);
        }
        return 0;
    }

    public int retirerDuStock(int idMedicament, int quantite) throws SaisieException {
        Medicament med = findById(idMedicament);
        if (med == null) {
            LogUtils.error(logger, "Médicament introuvable id=" + idMedicament);
            return -1;
        }
        if (quantite > med.getQuantiteMedicament()) {
            LogUtils.error(logger, "Stock insuffisant pour id=" + idMedicament);
            return -1;
        }

        med.setQuantiteMedicament(med.getQuantiteMedicament() - quantite);
        update(med);

        return med.getQuantiteMedicament();
    }
}