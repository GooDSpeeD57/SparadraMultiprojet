package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.DateTimePatternFr;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

import java.time.LocalDate;

public class Medicament {
    private static final Logger logger = LoggerFactory.getLogger(Medicament.class);
    private Integer idMedicament;
    private String nomMedicament;
    private String categorieMedicament;
    private double prixMedicament;
    private LocalDate dateMiseEnCirculation;
    private int quantiteMedicament;
    private String formeMedicament;
    private boolean sansOrdonnanceMedicament;

    public Medicament() {}

    public Medicament(String nomMedicament,
                      String categorieMedicament,
                      double prixMedicament,
                      LocalDate dateMiseEnCirculation,
                      int quantiteMedicament,
                      String formeMedicament,
                      boolean sansOrdonnanceMedicament
                      ) throws SaisieException {
        setNomMedicament(nomMedicament);
        setCategorieMedicament(categorieMedicament);
        setPrixMedicament(prixMedicament);
        setDateMiseEnCirculation(dateMiseEnCirculation);
        setQuantiteMedicament(quantiteMedicament);
        setFormeMedicament(formeMedicament);
        setSansOrdonnanceMedicament(sansOrdonnanceMedicament);
    }

    public Medicament(Integer idMedicament,
                      String nomMedicament,
                      String categorieMedicament,
                      double prixMedicament,
                      LocalDate dateMiseEnCirculation,
                      int quantiteMedicament,
                      String formeMedicament,
                      boolean sansOrdonnanceMedicament
                      ) throws SaisieException {
        this.idMedicament = idMedicament;
        setNomMedicament(nomMedicament);
        setCategorieMedicament(categorieMedicament);
        setPrixMedicament(prixMedicament);
        setDateMiseEnCirculation(dateMiseEnCirculation);
        setQuantiteMedicament(quantiteMedicament);
        setFormeMedicament(formeMedicament);
        setSansOrdonnanceMedicament(sansOrdonnanceMedicament);

    }

    public Integer getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(Integer idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) throws SaisieException {
        if (!RegexValidator.validerNomMedicament(nomMedicament)) {
            String message = "Nom du médicament invalide : " + nomMedicament;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.nomMedicament = nomMedicament;
    }

    public String getCategorieMedicament() {
        return categorieMedicament;
    }

    public void setCategorieMedicament(String categorieMedicament) throws SaisieException {
        if (!RegexValidator.validerCategorieMedicament(categorieMedicament)) {
            String message = "Catégorie invalide : " + categorieMedicament;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.categorieMedicament = categorieMedicament;
    }

    public double getPrixMedicament() {
        return prixMedicament;
    }

    public void setPrixMedicament(double prixMedicament) throws SaisieException {
        if (!RegexValidator.validerPrix(prixMedicament)) {
            String message = "Prix invalide : " + prixMedicament;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.prixMedicament = prixMedicament;
    }

    public LocalDate getDateMiseEnCirculation() {
        return dateMiseEnCirculation;
    }

    public void setDateMiseEnCirculation(LocalDate dateMiseEnCirculation) throws SaisieException {
        if (dateMiseEnCirculation == null) {
            String message = "Date de mise en circulation obligatoire.";
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.dateMiseEnCirculation = dateMiseEnCirculation;
    }

    public int getQuantiteMedicament() {
        return quantiteMedicament;
    }

    public void setQuantiteMedicament(int quantiteMedicament) throws SaisieException {
        if (quantiteMedicament < 0) {
            String message = "Le stock doit être supérieur à 0 : " + quantiteMedicament;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.quantiteMedicament = quantiteMedicament;
    }

    public boolean SansOrdonnanceMedicament() {
        return sansOrdonnanceMedicament;
    }

    public void setSansOrdonnanceMedicament(boolean sansOrdonnanceMedicament) {
        this.sansOrdonnanceMedicament = sansOrdonnanceMedicament;
    }

    public String getFormeMedicament() {
        return formeMedicament;
    }

    public void setFormeMedicament(String formeMedicament) throws SaisieException {
        if (formeMedicament == null || formeMedicament.isBlank()) {
            String message = "La forme du médicament est obligatoire.";
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.formeMedicament = formeMedicament;
    }

    @Override
    public String toString() {
        return "\nMédicament"
                + "\nID                         : " + idMedicament
                + "\nNom                        : " + (nomMedicament != null ? nomMedicament : "Non défini")
                + "\nCatégorie                  : " + (categorieMedicament != null ? categorieMedicament : "Non définie")
                + "\nPrix                       : " + String.format("%.2f", prixMedicament)
                + "\nDate mise en circulation   : " + (dateMiseEnCirculation != null ? DateTimePatternFr.formatDate(dateMiseEnCirculation) : "Non définie")
                + "\nStock                      : " + quantiteMedicament
                + "\nForme                      : " + (formeMedicament != null ? formeMedicament : "Non définie")
                + "\nSans ordonnance            : " + (sansOrdonnanceMedicament ? "Oui" : "Non");
    }
}