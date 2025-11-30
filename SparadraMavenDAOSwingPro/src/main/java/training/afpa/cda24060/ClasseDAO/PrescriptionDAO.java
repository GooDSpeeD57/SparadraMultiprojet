package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.modele.Prescription;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO extends AbstractDAO<Prescription> {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionDAO.class);

    @Override
    public boolean insert(Prescription p) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Prescription (id_Ordonnance, nomMedicament, prixUnitaire, quantitePrescrite) ");
        sb.append("VALUES (?, ?, ?, ?);");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, p.getIdOrdonnance());
            pst.setString(2, p.getNomMedicament());
            pst.setDouble(3, p.getPrixUnitaire());
            pst.setInt(4, p.getQuantitePrescrite());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) p.setIdPrescription(rs.getInt(1));
            }

            LogUtils.debug(logger, "Prescription insérée : " + p);
            return true;

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Prescription : " + p, e);
            throw e;
        }
    }

    @Override
    public boolean update(Prescription p) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Prescription SET id_Ordonnance=?, nomMedicament=?, prixUnitaire=?, quantitePrescrite=? ");
        sb.append("WHERE id_Prescription=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, p.getIdOrdonnance());
            pst.setString(2, p.getNomMedicament());
            pst.setDouble(3, p.getPrixUnitaire());
            pst.setInt(4, p.getQuantitePrescrite());
            pst.setInt(5, p.getIdPrescription());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Prescription mise à jour : " + p + " -> succès=" + success);
            return success;

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Prescription : " + p, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM Prescription WHERE id_Prescription=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Prescription supprimée id=" + id + " -> succès=" + success);
            return success;

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Prescription id=" + id, e);
            throw e;
        }
    }

    public Prescription findById(Integer idPrescription) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Prescription WHERE id_Prescription=?;");
        String sql = sb.toString();

        Prescription p = null;

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPrescription);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    p = new Prescription();
                    p.setIdPrescription(rs.getInt("id_Prescription"));
                    p.setIdOrdonnance(rs.getInt("id_Ordonnance"));
                    p.setNomMedicament(rs.getString("nomMedicament"));
                    p.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                    p.setQuantitePrescrite(rs.getInt("quantitePrescrite"));
                    LogUtils.debug(logger, "Prescription trouvée id=" + idPrescription + " : " + p);
                }
            }

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Prescription id=" + idPrescription, e);
            throw e;
        }

        return p;
    }

    public List<Prescription> findByOrdonnance(Integer idOrdonnance) throws SQLException {
        List<Prescription> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Prescription WHERE id_Ordonnance=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idOrdonnance);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Prescription p = new Prescription();
                    p.setIdPrescription(rs.getInt("id_Prescription"));
                    p.setIdOrdonnance(rs.getInt("id_Ordonnance"));
                    p.setNomMedicament(rs.getString("nomMedicament"));
                    p.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                    p.setQuantitePrescrite(rs.getInt("quantitePrescrite"));
                    list.add(p);
                }
            }

            LogUtils.debug(logger, "Prescriptions trouvées pour ordonnance id=" + idOrdonnance + " : " + list.size());

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findByOrdonnance id=" + idOrdonnance, e);
            throw e;
        }

        return list;
    }

    public List<Prescription> findAll() throws SQLException {
        List<Prescription> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Prescription;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setIdPrescription(rs.getInt("id_Prescription"));
                p.setIdOrdonnance(rs.getInt("id_Ordonnance"));
                p.setNomMedicament(rs.getString("nomMedicament"));
                p.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                p.setQuantitePrescrite(rs.getInt("quantitePrescrite"));
                list.add(p);
            }

            LogUtils.debug(logger, "Nombre total de prescriptions trouvées : " + list.size());

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Prescription", e);
            throw e;
        }

        return list;
    }
}
