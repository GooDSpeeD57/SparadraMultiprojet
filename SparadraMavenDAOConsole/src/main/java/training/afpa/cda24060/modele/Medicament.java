package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Medicament {

    private int idMedicament;
    private String nomMedicament;
    private String categorieMedicament;
    private double prixMedicament;
    private String dateMiseEnCirculation;
    private int quantiteMedicament;
    private String formeMedicament;
    private boolean sansOrdonnanceMedicament;

    public Medicament() {}

    public Medicament(int idMedicament,
                      String nomMedicament,
                      String categorieMedicament,
                      double prixMedicament,
                      String dateMiseEnCirculation,
                      int quantiteMedicament,
                      boolean sansOrdonnanceMedicament,
                      String formeMedicament) throws SaisieException {

        this.idMedicament = idMedicament;
        setNomMedicament(nomMedicament);
        setCategorieMedicament(categorieMedicament);
        setPrixMedicament(prixMedicament);
        setDateMiseEnCirculation(dateMiseEnCirculation);
        setQuantiteMedicament(quantiteMedicament);
        setSansOrdonnanceMedicament(sansOrdonnanceMedicament);
        setFormeMedicament(formeMedicament);
    }

    // ------------------ GETTERS / SETTERS ------------------

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
        if (!RegexValidator.validerNomMedicament(nomMedicament))
            throw new SaisieException("Nom du médicament invalide : " + nomMedicament);
        this.nomMedicament = nomMedicament;
    }

    public String getCategorieMedicament() {
        return categorieMedicament;
    }

    public void setCategorieMedicament(String categorieMedicament) throws SaisieException {
        if (!RegexValidator.validerCategorieMedicament(categorieMedicament))
            throw new SaisieException("Catégorie invalide : " + categorieMedicament);
        this.categorieMedicament = categorieMedicament;
    }

    public double getPrixMedicament() {
        return prixMedicament;
    }

    public void setPrixMedicament(double prixMedicament) throws SaisieException {
        if (!RegexValidator.validerPrix(prixMedicament))
            throw new SaisieException("Prix invalide : " + prixMedicament);
        this.prixMedicament = prixMedicament;
    }

    public String getDateMiseEnCirculation() {
        return dateMiseEnCirculation;
    }

    public void setDateMiseEnCirculation(String dateMiseEnCirculation) throws SaisieException {
        if (dateMiseEnCirculation == null || dateMiseEnCirculation.isBlank())
            throw new SaisieException("Date obligatoire.");
        this.dateMiseEnCirculation = dateMiseEnCirculation;
    }

    public int getQuantiteMedicament() {
        return quantiteMedicament;
    }

    public void setQuantiteMedicament(int quantiteMedicament) throws SaisieException {
        if (!RegexValidator.validerQuantite(quantiteMedicament))
            throw new SaisieException("Quantité invalide : " + quantiteMedicament);
        this.quantiteMedicament = quantiteMedicament;
    }

    public boolean isSansOrdonnanceMedicament() {
        return sansOrdonnanceMedicament;
    }

    public void setSansOrdonnanceMedicament(boolean sansOrdonnanceMedicament) {
        this.sansOrdonnanceMedicament = sansOrdonnanceMedicament;
    }

    public String getFormeMedicament() {
        return formeMedicament;
    }

    public void setFormeMedicament(String formeMedicament) throws SaisieException {
        if (formeMedicament == null || formeMedicament.isBlank())
            throw new SaisieException("La forme du médicament est obligatoire.");
        this.formeMedicament = formeMedicament;
    }

    @Override
    public String toString() {
        return "\nMédicament"
                + "\nID                     : " + idMedicament
                + "\nNom                    : " + nomMedicament
                + "\nCatégorie              : " + categorieMedicament
                + "\nPrix                   : " + prixMedicament
                + "\nDate mise en circulation : " + dateMiseEnCirculation
                + "\nStock                  : " + quantiteMedicament
                + "\nForme                  : " + formeMedicament
                + "\nSans ordonnance        : " + (sansOrdonnanceMedicament ? "Oui" : "Non");
    }
}