package modele;

import java.io.Serializable;

public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomMedicament;
    private double prixUnitaire;
    private int quantitePrescrite;

    public Prescription(Medicament medicament, int quantitePrescrite) {
        if (medicament == null) throw new IllegalArgumentException("Le médicament ne peut pas être null.");
        this.nomMedicament = medicament.getNomMedicament();
        this.prixUnitaire = medicament.getPrixMedicament();
        this.quantitePrescrite = quantitePrescrite;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
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

    @Override
    public String toString() {
        return "Prescription : " +
                "Médicament = " + nomMedicament +
                ", Quantité prescrite = " + quantitePrescrite +
                ", Prix unitaire = " + String.format("%.2f", prixUnitaire) + "€" +
                ", Prix total = " + String.format("%.2f", getPrixTotal()) + "€";
    }
}