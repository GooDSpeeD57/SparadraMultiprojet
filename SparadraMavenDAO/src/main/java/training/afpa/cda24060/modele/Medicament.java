package training.afpa.cda24060.modele;

import training.afpa.cda24060.Connection.DatabaseConnectionSingleton;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Medicament {

    private int idMedicament;
    private String nomMedicament;
    private String categorieMedicament;
    private double prixMedicament;
    private String dateMiseEnCirculation;
    private int quantiteMedicament;
    private boolean sansOrdonnanceMedicament;

    public Medicament() {
    }

    public Medicament(int idMedicament, String nomMedicament, String categorieMedicament,
                      double prixMedicament, String dateMiseEnCirculation,
                      int quantiteMedicament, boolean sansOrdonnanceMedicament) throws SaisieException {

        this.idMedicament = idMedicament;
        this.setNomMedicament(nomMedicament);
        this.setCategorieMedicament(categorieMedicament);
        this.setPrixMedicament(prixMedicament);
        this.setDateMiseEnCirculation(dateMiseEnCirculation);
        this.setQuantiteMedicament(quantiteMedicament);
        this.setSansOrdonnanceMedicament(sansOrdonnanceMedicament);
    }

    // --- GETTERS / SETTERS ---
    public int getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(int idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) throws SaisieException {
        if (!RegexValidator.validerNomMedicament(nomMedicament)) {
            throw new SaisieException("Nom du médicament incorrect : " + nomMedicament);
        }
        this.nomMedicament = nomMedicament;
    }

    public String getCategorieMedicament() {
        return categorieMedicament;
    }

    public void setCategorieMedicament(String categorieMedicament) throws SaisieException {
        if (!RegexValidator.validerCategorieMedicament(categorieMedicament)) {
            throw new SaisieException("Catégorie du médicament incorrecte : " + categorieMedicament);
        }
        this.categorieMedicament = categorieMedicament;
    }

    public double getPrixMedicament() {
        return prixMedicament;
    }

    public void setPrixMedicament(double prixMedicament) throws SaisieException {
        if (!RegexValidator.validerPrix(prixMedicament)) {
            throw new SaisieException("Prix du médicament ne peut pas être négatif : " + prixMedicament);
        }
        this.prixMedicament = prixMedicament;
    }

    public String getDateMiseEnCirculation() {
        return dateMiseEnCirculation;
    }

    public void setDateMiseEnCirculation(String dateMiseEnCirculation) throws SaisieException {
        if (dateMiseEnCirculation == null || dateMiseEnCirculation.isBlank()) {
            throw new SaisieException("Date de mise en circulation obligatoire.");
        }
        this.dateMiseEnCirculation = dateMiseEnCirculation;
    }

    public int getQuantiteMedicament() {
        return quantiteMedicament;
    }

    public void setQuantiteMedicament(int quantiteMedicament) throws SaisieException {
        if (!RegexValidator.validerQuantite(quantiteMedicament)) {
            throw new SaisieException("Quantité du médicament ne peut pas être négative : " + quantiteMedicament);
        }
        this.quantiteMedicament = quantiteMedicament;
    }

    public boolean isSansOrdonnanceMedicament() {
        return sansOrdonnanceMedicament;
    }

    public void setSansOrdonnanceMedicament(boolean sansOrdonnanceMedicament) {
        this.sansOrdonnanceMedicament = sansOrdonnanceMedicament;
    }

    public int retirerDuStock(int idMedicament, int quantite) throws SaisieException {

        String select = "SELECT stock FROM Medicament WHERE id_Medicament = ?";
        String update = "UPDATE Medicament SET stock = stock - ? WHERE id_Medicament = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement psSelect = conn.prepareStatement(select);
             PreparedStatement psUpdate = conn.prepareStatement(update)) {

            psSelect.setInt(1, idMedicament);
            ResultSet rs = psSelect.executeQuery();

            if (!rs.next()) {
                throw new SaisieException("Médicament introuvable !");
            }

            int stockActuel = rs.getInt("stock");

            if (quantite > stockActuel) {
                throw new SaisieException("Stock insuffisant pour le médicament !");
            }

            psUpdate.setInt(1, quantite);
            psUpdate.setInt(2, idMedicament);
            psUpdate.executeUpdate();

            return stockActuel - quantite;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur SQL lors du retrait du stock");
        }
    }

    @Override
    public String toString() {
        return "\nMédicament"
                + "\nID                          : " + idMedicament
                + "\nNom                         : " + nomMedicament
                + "\nCatégorie                   : " + categorieMedicament
                + "\nPrix                        : " + prixMedicament
                + "\nDate mise en circulation    : " + dateMiseEnCirculation
                + "\nQuantité en stock           : " + quantiteMedicament
                + "\nSans ordonnance             : " + (sansOrdonnanceMedicament ? "Oui" : "Non");
    }
}