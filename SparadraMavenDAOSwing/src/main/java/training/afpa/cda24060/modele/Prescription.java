package training.afpa.cda24060.modele;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Prescription {

    private static final Logger LOGGER = Logger.getLogger(Prescription.class.getName());

    private int idPrescription;
    private int idOrdonnance;
    private String nomMedicament;
    private double prixUnitaire;
    private int quantitePrescrite;

    // ===================== Constructeurs =====================
    public Prescription() {
    }

    public Prescription(Medicament medicament, int quantitePrescrite) {
        if (medicament == null) {
            LOGGER.log(Level.SEVERE, "Le médicament fourni pour la prescription est null !");
            throw new IllegalArgumentException("Le médicament ne peut pas être null.");
        }
        this.nomMedicament = medicament.getNomMedicament();
        this.prixUnitaire = medicament.getPrixMedicament();
        this.quantitePrescrite = quantitePrescrite;
    }

    // ===================== Getters / Setters =====================
    public int getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(int idPrescription) {
        this.idPrescription = idPrescription;
    }

    public int getIdOrdonnance() {
        return idOrdonnance;
    }

    public void setIdOrdonnance(int idOrdonnance) {
        this.idOrdonnance = idOrdonnance;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getQuantitePrescrite() {
        return quantitePrescrite;
    }

    public void setQuantitePrescrite(int quantitePrescrite) {
        this.quantitePrescrite = quantitePrescrite;
    }

    public double getPrixTotal() {
        return prixUnitaire * quantitePrescrite;
    }

    // ===================== toString =====================
    @Override
    public String toString() {
        return "\nPrescription : "
                + "\nMédicament          : " + nomMedicament
                + "\nQuantité prescrite  : " + quantitePrescrite
                + "\nPrix unitaire       : " + String.format("%.2f", prixUnitaire) + " €"
                + "\nPrix total          : " + String.format("%.2f", getPrixTotal()) + " €";
    }
}
