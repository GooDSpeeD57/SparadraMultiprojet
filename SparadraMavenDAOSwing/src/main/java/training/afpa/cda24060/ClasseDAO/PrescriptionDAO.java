package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.modele.Prescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrescriptionDAO extends AbstractDAO<Prescription> {

    @Override
    protected String getTableName() {
        return "Prescription";
    }

    @Override
    protected String getPrimaryKey() {
        return "idPrescription";
    }

    @Override
    protected Prescription map(ResultSet rs) throws Exception {
        Prescription prescription = new Prescription();
        prescription.setIdPrescription(rs.getInt("idPrescription"));
        prescription.setIdOrdonnance(rs.getInt("idOrdonnance"));
        prescription.setNomMedicament(rs.getString("nomMedicament"));
        prescription.setPrixUnitaire(rs.getDouble("prixUnitaire"));
        prescription.setQuantitePrescrite(rs.getInt("quantitePrescrite"));
        return prescription;
    }

    @Override
    protected PreparedStatement prepareInsert(Prescription obj, Connection conn) throws Exception {
        String sql = "INSERT INTO Prescription (idOrdonnance, nomMedicament, prixUnitaire, quantitePrescrite) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setInt(1, obj.getIdOrdonnance());
        pst.setString(2, obj.getNomMedicament());
        pst.setDouble(3, obj.getPrixUnitaire());
        pst.setInt(4, obj.getQuantitePrescrite());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Prescription obj, Connection conn) throws Exception {
        String sql = "UPDATE Prescription SET idOrdonnance=?, nomMedicament=?, prixUnitaire=?, quantitePrescrite=? " +
                "WHERE idPrescription=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, obj.getIdOrdonnance());
        pst.setString(2, obj.getNomMedicament());
        pst.setDouble(3, obj.getPrixUnitaire());
        pst.setInt(4, obj.getQuantitePrescrite());
        pst.setInt(5, obj.getIdPrescription());
        return pst;
    }

    // Optionnel : récupérer toutes les prescriptions d'une ordonnance
    public java.util.List<Prescription> findByOrdonnance(int idOrdonnance) {
        java.util.List<Prescription> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Prescription WHERE idOrdonnance=?";
        try (Connection conn = training.afpa.cda24060.Connection.DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idOrdonnance);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur findByOrdonnance : " + e.getMessage());
        }
        return list;
    }
}
