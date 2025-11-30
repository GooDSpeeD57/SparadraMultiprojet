package training.afpa.cda24060.modele;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Prescription {

    private static final Logger LOGGER = Logger.getLogger(Prescription.class.getName());

    private Integer idPrescription;
    private Integer idOrdonnance;
    private String nomMedicament;
    private double prixUnitaire;
    private int quantitePrescrite;

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

    public Prescription(Medicament medicament, int quantitePrescrite, Integer idPrescription) {
        if (medicament == null) {
            LOGGER.log(Level.SEVERE, "Le médicament fourni pour la prescription est null !");
            throw new IllegalArgumentException("Le médicament ne peut pas être null.");
        }
        this.nomMedicament = medicament.getNomMedicament();
        this.prixUnitaire = medicament.getPrixMedicament();
        this.quantitePrescrite = quantitePrescrite;
        this.idPrescription = idPrescription;
    }

    public Prescription(Medicament medicament, int quantitePrescrite, Integer idPrescription, Integer idOrdonnance) {
        if (medicament == null) {
            LOGGER.log(Level.SEVERE, "Le médicament fourni pour la prescription est null !");
            throw new IllegalArgumentException("Le médicament ne peut pas être null.");
        }
        if (quantitePrescrite <= 0) {
            throw new IllegalArgumentException("La quantité prescrite doit être supérieure à 0.");
        }
        this.nomMedicament = medicament.getNomMedicament();
        this.prixUnitaire = medicament.getPrixMedicament();
        this.quantitePrescrite = quantitePrescrite;
        this.idPrescription = idPrescription;
        this.idOrdonnance = idOrdonnance;
    }

    public Integer getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(Integer idPrescription) {
        this.idPrescription = idPrescription;
    }

    public Integer getIdOrdonnance() {
        return idOrdonnance;
    }

    public void setIdOrdonnance(Integer idOrdonnance) {
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
                + "\nID Prescription     : " + idPrescription
                + "\nID Ordonnance       : " + idOrdonnance
                + "\nMédicament          : " + nomMedicament
                + "\nQuantité prescrite  : " + quantitePrescrite
                + "\nPrix unitaire       : " + String.format("%.2f", prixUnitaire) + " €"
                + "\nPrix total          : " + String.format("%.2f", getPrixTotal()) + " €";
    }
}