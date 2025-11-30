package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.modele.Ordonnance;
import training.afpa.cda24060.modele.Prescription;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdonnanceDAO extends AbstractDAO<Ordonnance> {

    private static final Logger logger = LoggerFactory.getLogger(OrdonnanceDAO.class);

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final MedecinDAO medecinDAO = new MedecinDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    public boolean insert(Ordonnance o) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Ordonnance (dateCreation, id_Client, id_Medecin) ");
        sb.append("VALUES (?, ?, ?);");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setDate(1, Date.valueOf(o.getDateOrdonnance()));
            pst.setInt(2, o.getClient().getId());
            pst.setInt(3, o.getMedecin().getId());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) o.setId_Ordonnance(rs.getInt(1));
            }

            // Insertion des prescriptions
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

    @Override
    public boolean update(Ordonnance o) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Ordonnance SET dateCreation=?, id_Client=?, id_Medecin=? ");
        sb.append("WHERE id_Ordonnance=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setDate(1, Date.valueOf(o.getDateOrdonnance()));
            pst.setInt(2, o.getClient().getId());
            pst.setInt(3, o.getMedecin().getId());
            pst.setInt(4, o.getId_Ordonnance());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Ordonnance mise à jour : " + o + " -> succès=" + success);
            return success;

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Ordonnance : " + o, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM Ordonnance WHERE id_Ordonnance=?;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Ordonnance supprimée id=" + id + " -> succès=" + success);
            return success;

        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Ordonnance id=" + id, e);
            throw e;
        }
    }

    @Override
    public Ordonnance findById(Integer id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Ordonnance WHERE id_Ordonnance=?;");
        String sql = sb.toString();

        Ordonnance o = null;

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int idClient = rs.getInt("id_Client");
                    int idMedecin = rs.getInt("id_Medecin");

                    Client client = clientDAO.findById(idClient);
                    Medecin medecin = medecinDAO.findById(idMedecin);

                    LocalDate dateOrdonnance = rs.getDate("dateCreation").toLocalDate();
                    List<Prescription> prescriptions = prescriptionDAO.findByOrdonnance(id);

                    o = new Ordonnance(medecin, client, prescriptions, dateOrdonnance);
                    o.setId_Ordonnance(id);

                    LogUtils.debug(logger, "Ordonnance trouvée id=" + id + " : " + o);
                }
            }
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findById Ordonnance id=" + id, e);
            throw e;
        }
        return o;
    }

    @Override
    public List<Ordonnance> findAll() throws SQLException {
        List<Ordonnance> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Ordonnance;");
        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_Ordonnance");
                int idClient = rs.getInt("id_Client");
                int idMedecin = rs.getInt("id_Medecin");

                Client client = clientDAO.findById(idClient);
                Medecin medecin = medecinDAO.findById(idMedecin);
                LocalDate dateOrdonnance = rs.getDate("dateCreation").toLocalDate();
                List<Prescription> prescriptions = prescriptionDAO.findByOrdonnance(id);

                Ordonnance o = new Ordonnance(medecin, client, prescriptions, dateOrdonnance);
                o.setId_Ordonnance(id);

                list.add(o);
            }
            LogUtils.debug(logger, "Total ordonnances trouvées : " + list.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findAll Ordonnance", e);
            throw e;
        }
        return list;
    }
}
