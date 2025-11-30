package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Medicament;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO extends AbstractDAO<Medicament> {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentDAO.class);

    @Override
    public boolean insert(Medicament med) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Medicament (");
        sb.append("nomMedicament, categorie, prix, dateCirculation, stock, forme, sansOrdonnance");
        sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?);");
        String sql = sb.toString();

        try (Connection con = getConnection();
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

    @Override
    public boolean update(Medicament med) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Medicament SET ");
        sb.append("nomMedicament=?, categorie=?, prix=?, dateCirculation=?, stock=?, forme=?, sansOrdonnance=? ");
        sb.append("WHERE id_Medicament=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
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
            LogUtils.debug(logger, "Medicament mis à jour : " + med + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Medicament : " + med, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer idMedicament) throws SQLException {
        String sql = "DELETE FROM Medicament WHERE id_Medicament=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMedicament);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Medicament supprimé id=" + idMedicament + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Medicament id=" + idMedicament, e);
            throw e;
        }
    }

    @Override
    public Medicament findById(Integer idMedicament) throws SQLException {
        Medicament med = null;
        String sql = "SELECT * FROM Medicament WHERE id_Medicament=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idMedicament);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nomMedicament");
                    String categorie = rs.getString("categorie");
                    double prix = rs.getDouble("prix");
                    Date sqlDate = rs.getDate("dateCirculation");
                    LocalDate date = sqlDate != null ? sqlDate.toLocalDate() : null;
                    int quantite = rs.getInt("stock");
                    boolean sansOrdonnance = rs.getBoolean("sansOrdonnance");
                    String forme = rs.getString("forme");
                    if (forme == null || forme.isBlank()) forme = "Comprimé";

                    med = new Medicament(nom, categorie, prix, date, quantite, forme, sansOrdonnance);
                    med.setIdMedicament(rs.getInt("id_Medicament"));

                    LogUtils.debug(logger, "Medicament trouvé id=" + idMedicament + " : " + med);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Medicament id=" + idMedicament, e);
            throw e;
        }
        return med;
    }

    @Override
    public List<Medicament> findAll() throws SQLException {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicament";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("nomMedicament");
                String categorie = rs.getString("categorie");
                double prix = rs.getDouble("prix");
                Date sqlDate = rs.getDate("dateCirculation");
                LocalDate date = sqlDate != null ? sqlDate.toLocalDate() : null;
                int quantite = rs.getInt("stock");
                boolean sansOrdonnance = rs.getBoolean("sansOrdonnance");
                String forme = rs.getString("forme");
                if (forme == null || forme.isBlank()) forme = "Comprimé";

                Medicament med = new Medicament(nom, categorie, prix, date, quantite, forme, sansOrdonnance);
                med.setIdMedicament(rs.getInt("id_Medicament"));
                list.add(med);
            }
            LogUtils.debug(logger, "Total Medicaments trouvés : " + list.size());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    // Méthode findByName
    public List<Medicament> findByName(String nomRecherche) throws SQLException {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicament WHERE nomMedicament LIKE ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nomRecherche + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String nom = rs.getString("nomMedicament");
                    String categorie = rs.getString("categorie");
                    double prix = rs.getDouble("prix");
                    Date sqlDate = rs.getDate("dateCirculation");
                    LocalDate date = sqlDate != null ? sqlDate.toLocalDate() : null;
                    int quantite = rs.getInt("stock");
                    boolean sansOrdonnance = rs.getBoolean("sansOrdonnance");
                    String forme = rs.getString("forme");
                    if (forme == null || forme.isBlank()) forme = "Comprimé";

                    Medicament med = new Medicament(nom, categorie, prix, date, quantite, forme, sansOrdonnance);
                    med.setIdMedicament(rs.getInt("id_Medicament"));
                    list.add(med);

                    LogUtils.debug(logger, "Medicament trouvé par nom=" + nomRecherche + " : " + med);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByName Medicament nom=" + nomRecherche, e);
            throw e;
        }
        return list;
    }


    // Méthode countMedicaments
    public int countMedicaments() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Medicament";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                LogUtils.debug(logger, "Nombre total de Medicaments=" + total);
                return total;
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur countMedicaments", e);
            throw e;
        }
        return 0;
    }

    // Méthode retirerDuStock
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
        LogUtils.debug(logger, "Stock retiré id=" + idMedicament + ", quantité=" + quantite + ", reste=" + med.getQuantiteMedicament());
        return med.getQuantiteMedicament();
    }
}
