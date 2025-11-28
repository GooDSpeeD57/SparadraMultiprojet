package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medicament;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentDAO.class);

    public boolean insert(Medicament med) throws SQLException {
        String sql = "INSERT INTO Medicament (nomMedicament, categorie, prix, dateCirculation, stock, forme, sansOrdonnance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, med.getNomMedicament());
            pst.setString(2, med.getCategorieMedicament());
            pst.setDouble(3, med.getPrixMedicament());
            pst.setDate(4, Date.valueOf(med.getDateMiseEnCirculation()));
            pst.setInt(5, med.getQuantiteMedicament());
            pst.setString(6, med.getFormeMedicament() != null ? med.getFormeMedicament() : "Comprimé");
            pst.setBoolean(7, med.SansOrdonnanceMedicament());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) med.setIdMedicament(rs.getInt(1));
            }

            LogUtils.debug(logger, "Medicament inséré : " + med);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Medicament : " + med, e);
            throw e;
        }
    }

    public boolean update(Medicament med) throws SQLException {
        String sql = "UPDATE Medicament SET nomMedicament=?, categorie=?, prix=?, dateCirculation=?, stock=?, forme=?, sansOrdonnance=? WHERE id_Medicament=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, med.getNomMedicament());
            pst.setString(2, med.getCategorieMedicament());
            pst.setDouble(3, med.getPrixMedicament());
            pst.setDate(4, Date.valueOf(med.getDateMiseEnCirculation()));
            pst.setInt(5, med.getQuantiteMedicament());
            pst.setString(6, med.getFormeMedicament() != null ? med.getFormeMedicament() : "Comprimé");
            pst.setBoolean(7, med.SansOrdonnanceMedicament());
            pst.setInt(8, med.getIdMedicament());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Medicament mis à jour : " + med);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Medicament : " + med, e);
            throw e;
        }
    }

    public boolean delete(int idMedicament) throws SQLException {
        String sql = "DELETE FROM Medicament WHERE id_Medicament=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMedicament);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Medicament supprimé id=" + idMedicament);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Medicament id=" + idMedicament, e);
            throw e;
        }
    }

    public Medicament findById(int idMedicament) {
        String sql = "SELECT * FROM Medicament WHERE id_Medicament=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMedicament);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
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

                    LogUtils.debug(logger, "Medicament trouvé par id=" + idMedicament + " : " + med);
                    return med;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Medicament id=" + idMedicament, e);
        }
        return null;
    }

    public List<Medicament> findByName(String nom) {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicament WHERE nomMedicament LIKE ?";

        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
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

                    list.add(med);
                    LogUtils.debug(logger, "Medicament trouvé par nom=" + nom + " : " + med);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByName Medicament nom=" + nom, e);
        }

        return list;
    }

    public List<Medicament> findAll() {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicament";

        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
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

                list.add(med);
            }

            LogUtils.debug(logger, "Tous les Medicaments récupérés, total=" + list.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Medicament", e);
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public int countMedicaments() {
        String sql = "SELECT COUNT(*) AS total FROM Medicament";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                LogUtils.debug(logger, "Nombre total de Medicaments=" + total);
                return total;
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur countMedicaments", e);
        }

        return 0;
    }

    public int retirerDuStock(int idMedicament, int quantite) throws SaisieException, SQLException {
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
