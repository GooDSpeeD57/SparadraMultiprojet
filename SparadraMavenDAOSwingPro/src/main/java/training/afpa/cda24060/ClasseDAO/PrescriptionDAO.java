package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Prescription;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO extends AbstractDAO<Prescription> {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionDAO.class);

    protected String getTableName() {
        return "Prescription";
    }

    protected String getPrimaryKey() {
        return "id_Prescription";
    }

    protected Prescription map(ResultSet rs) {
        try {
            Prescription prescription = new Prescription();
            prescription.setIdPrescription(rs.getInt("id_Prescription"));
            prescription.setIdOrdonnance(rs.getInt("id_Ordonnance"));
            prescription.setNomMedicament(rs.getString("nomMedicament"));
            prescription.setPrixUnitaire(rs.getDouble("prixUnitaire"));
            prescription.setQuantitePrescrite(rs.getInt("quantitePrescrite"));
            return prescription;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur mapping Prescription", e);
            return null;
        }
    }


    protected PreparedStatement prepareInsert(Prescription p, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("INSERT INTO Prescription (id_Ordonnance, nomMedicament, prixUnitaire, quantitePrescrite)")
                    .append("VALUES (?, ?, ?, ?)")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, p.getIdOrdonnance());
            pst.setString(2, p.getNomMedicament());
            pst.setDouble(3, p.getPrixUnitaire());
            pst.setInt(4, p.getQuantitePrescrite());

            LogUtils.debug(logger, "PreparedStatement insert Prescription prêt pour : " + p);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareInsert Prescription", e);
            return null;
        }
    }

    protected PreparedStatement prepareUpdate(Prescription p, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("UPDATE Prescription SET id_Ordonnance=?, nomMedicament=?, prixUnitaire=?, quantitePrescrite=?")
                    .append("WHERE id_Prescription=?")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, p.getIdOrdonnance());
            pst.setString(2, p.getNomMedicament());
            pst.setDouble(3, p.getPrixUnitaire());
            pst.setInt(4, p.getQuantitePrescrite());
            pst.setInt(5, p.getIdPrescription());

            LogUtils.debug(logger, "PreparedStatement update Prescription prêt pour : " + p);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareUpdate Prescription", e);
            return null;
        }
    }

    public List<Prescription> findByOrdonnance(int idOrdonnance) {
        List<Prescription> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Prescription WHERE id_Ordonnance=?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idOrdonnance);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Prescription p = map(rs);
                    if (p != null) list.add(p);
                }
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByOrdonnance idOrdonnance=" + idOrdonnance, e);
        }
        return list;
    }

    @Override
    public boolean insert(Prescription obj) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Prescription obj) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return false;
    }

    public Prescription findById(int idPrescription) {
        String sql = new SqlBuilder()
                .append("SELECT * FROM Prescription WHERE id_Prescription=?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPrescription);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById Prescription id=" + idPrescription, e);
        }
        return null;
    }

    public List<Prescription> findAll() {
        List<Prescription> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Prescription")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Prescription p = map(rs);
                if (p != null) list.add(p);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Prescription", e);
        }
        return list;
    }
}