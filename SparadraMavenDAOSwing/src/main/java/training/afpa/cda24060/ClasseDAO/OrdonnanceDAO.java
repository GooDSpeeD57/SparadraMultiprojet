package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Ordonnance;
import training.afpa.cda24060.modele.Prescription;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class OrdonnanceDAO extends AbstractDAO<Ordonnance> {

    private static final Logger logger = LoggerFactory.getLogger(OrdonnanceDAO.class);

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final MedecinDAO medecinDAO = new MedecinDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    protected String getTableName() {
        return "Ordonnance";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Ordonnance";
    }

    @Override
    protected Ordonnance map(ResultSet rs) {
        try {
            int idClient = rs.getInt("id_Client");
            int idMedecin = rs.getInt("id_Medecin");

            Client client = clientDAO.findById(idClient);
            Medecin medecin = medecinDAO.findById(idMedecin);

            LocalDate dateOrdonnance = rs.getDate("dateCreation").toLocalDate();

            List<Prescription> prescriptions = prescriptionDAO.findByOrdonnance(rs.getInt("id_Ordonnance"));

            Ordonnance ordonnance = new Ordonnance(medecin, client, prescriptions, dateOrdonnance);
            ordonnance.setId_Ordonnance(rs.getInt("id_Ordonnance"));

            return ordonnance;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur mapping Ordonnance", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Ordonnance o, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("INSERT INTO Ordonnance (dateCreation, id_Client, id_Medecin)")
                    .append("VALUES (?, ?, ?)")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setDate(1, java.sql.Date.valueOf(o.getDateOrdonnance()));
            pst.setInt(2, o.getClient().getIdClient());
            pst.setInt(3, o.getMedecin().getIdMedecin());

            LogUtils.debug(logger, "PreparedStatement insert Ordonnance prêt pour : " + o);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareInsert Ordonnance", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Ordonnance o, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("UPDATE Ordonnance SET dateCreation=?, id_Client=?, id_Medecin=?")
                    .append("WHERE id_Ordonnance=?")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDate(1, java.sql.Date.valueOf(o.getDateOrdonnance()));
            pst.setInt(2, o.getClient().getIdClient());
            pst.setInt(3, o.getMedecin().getIdMedecin());
            pst.setInt(4, o.getId_Ordonnance());

            LogUtils.debug(logger, "PreparedStatement update Ordonnance prêt pour : " + o);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareUpdate Ordonnance", e);
            return null;
        }
    }

    @Override
    public boolean insert(Ordonnance o) {
        boolean success = super.insert(o);

        if (success) {
            // Récupérer l'ID généré
            try (Connection conn = DCSingletonHikaricp.getConnection();
                 PreparedStatement pst = conn.prepareStatement("SELECT LAST_INSERT_ID()");
                 ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    o.setId_Ordonnance(rs.getInt(1));

                    for (Prescription p : o.getPrescriptions()) {
                        p.setIdOrdonnance(o.getId_Ordonnance());
                        prescriptionDAO.insert(p); // <-- un seul argument, corrigé
                    }
                }

            } catch (Exception e) {
                LogUtils.error(logger, "Erreur insertion prescriptions Ordonnance", e);
                return false;
            }
        }

        return success;
    }

    public Ordonnance findById(int id) {
        String sql = "SELECT * FROM Ordonnance WHERE id_Ordonnance=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById Ordonnance id=" + id, e);
        }
        return null;
    }

    public List<Ordonnance> findAll() {
        List<Ordonnance> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Ordonnance";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Ordonnance o = map(rs);
                if (o != null) list.add(o);
            }

        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Ordonnance", e);
        }

        return list;
    }
}
