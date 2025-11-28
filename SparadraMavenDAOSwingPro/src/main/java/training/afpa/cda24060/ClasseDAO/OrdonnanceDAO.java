package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Ordonnance;
import training.afpa.cda24060.modele.Prescription;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdonnanceDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrdonnanceDAO.class);

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final MedecinDAO medecinDAO = new MedecinDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    public boolean insert(Ordonnance o) throws SQLException {
        String sql = "INSERT INTO Ordonnance (dateCreation, id_Client, id_Medecin) VALUES (?, ?, ?)";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pst.setDate(1, java.sql.Date.valueOf(o.getDateOrdonnance()));
            pst.setInt(2, o.getClient().getId());
            pst.setInt(3, o.getMedecin().getId());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) o.setId_Ordonnance(rs.getInt(1));
            }

            for (Prescription p : o.getPrescriptions()) {
                p.setIdOrdonnance(o.getId_Ordonnance());
                prescriptionDAO.insert(p);
            }

            LogUtils.debug(logger, "Ordonnance insérée : " + o);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Ordonnance : " + o, e);
            throw e;
        }
    }

    public boolean update(Ordonnance o) throws SQLException {
        String sql = "UPDATE Ordonnance SET dateCreation=?, id_Client=?, id_Medecin=? WHERE id_Ordonnance=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setDate(1, java.sql.Date.valueOf(o.getDateOrdonnance()));
            pst.setInt(2, o.getClient().getId());
            pst.setInt(3, o.getMedecin().getId());
            pst.setInt(4, o.getId_Ordonnance());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Ordonnance mise à jour : " + o);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Ordonnance : " + o, e);
            throw e;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Ordonnance WHERE id_Ordonnance=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Ordonnance supprimée id=" + id);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Ordonnance id=" + id, e);
            throw e;
        }
    }

    public Ordonnance findById(int id) {
        String sql = "SELECT * FROM Ordonnance WHERE id_Ordonnance=?";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Ordonnance id=" + id, e);
        }
        return null;
    }

    public List<Ordonnance> findAll() {
        List<Ordonnance> list = new ArrayList<>();
        String sql = "SELECT * FROM Ordonnance";
        try (Connection con = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Ordonnance o = map(rs);
                if (o != null) list.add(o);
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Ordonnance", e);
        }
        return list;
    }

    private Ordonnance map(ResultSet rs) {
        try {
            int idClient = rs.getInt("id_Client");
            int idMedecin = rs.getInt("id_Medecin");

            Client client = clientDAO.findById(idClient);
            Medecin medecin = medecinDAO.findById(idMedecin);

            LocalDate dateOrdonnance = rs.getDate("dateCreation").toLocalDate();
            List<Prescription> prescriptions = prescriptionDAO.findByOrdonnance(rs.getInt("id_Ordonnance"));

            Ordonnance o = new Ordonnance(medecin, client, prescriptions, dateOrdonnance);
            o.setId_Ordonnance(rs.getInt("id_Ordonnance"));
            return o;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur mapping Ordonnance", e);
            return null;
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
    }
}