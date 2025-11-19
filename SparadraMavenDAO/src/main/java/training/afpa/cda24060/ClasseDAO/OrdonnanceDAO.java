package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.modele.Ordonnance;
import training.afpa.cda24060.modele.Prescription;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class OrdonnanceDAO extends AbstractDAO<Ordonnance> {

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final MedecinDAO medecinDAO = new MedecinDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    protected String getTableName() {
        return "Ordonnance";
    }

    @Override
    protected String getPrimaryKey() {
        return "idOrdonnance";
    }

    @Override
    protected Ordonnance map(ResultSet rs) throws Exception {
        int idClient = rs.getInt("id_Client");
        int idMedecin = rs.getInt("id_Medecin");

        Client client = clientDAO.findById(idClient);
        Medecin medecin = medecinDAO.findById(idMedecin);

        LocalDate dateOrdonnance = rs.getDate("dateCreation").toLocalDate();

        List<Prescription> prescriptions = prescriptionDAO.findByOrdonnance(rs.getInt("idOrdonnance"));

        Ordonnance ordonnance = new Ordonnance(medecin, client, prescriptions, dateOrdonnance);
        ordonnance.setIdOrdonnance(rs.getInt("idOrdonnance")); // ajout de l'id
        return ordonnance;
    }

    @Override
    protected PreparedStatement prepareInsert(Ordonnance obj, Connection conn) throws Exception {
        String sql = "INSERT INTO Ordonnance (dateCreation, id_Client, id_Medecin) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setDate(1, java.sql.Date.valueOf(obj.getDateOrdonnance()));
        pst.setInt(2, obj.getClient().getIdClient());
        pst.setInt(3, obj.getMedecin().getIdMedecin());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Ordonnance obj, Connection conn) throws Exception {
        String sql = "UPDATE Ordonnance SET dateCreation=?, id_Client=?, id_Medecin=? WHERE idOrdonnance=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDate(1, java.sql.Date.valueOf(obj.getDateOrdonnance()));
        pst.setInt(2, obj.getClient().getIdClient());
        pst.setInt(3, obj.getMedecin().getIdMedecin());
        pst.setInt(4, obj.getIdOrdonnance());
        return pst;
    }

    // Surcharge de insert pour gérer aussi les prescriptions
    @Override
    public boolean insert(Ordonnance obj) {
        boolean success = super.insert(obj);

        if (success) {
            // récupérer l'id généré
            try (Connection conn = training.afpa.cda24060.Connection.DCSingletonHikaricp.getConnection();
                 PreparedStatement pst = conn.prepareStatement("SELECT LAST_INSERT_ID()")) {
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        obj.setIdOrdonnance(rs.getInt(1));
                        for (Prescription p : obj.getPrescriptions()) {
                            p.setIdOrdonnance(obj.getIdOrdonnance());
                            prescriptionDAO.insert(p);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur insertion prescriptions Ordonnance : " + e.getMessage());
                return false;
            }
        }

        return success;
    }
}
