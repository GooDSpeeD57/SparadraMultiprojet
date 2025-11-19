package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Medicament implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Medicament.class);

    private String nomMedicament;
    private String categorieMedicament;
    private double prixMedicament;
    private String dateMiseEnCirculation;
    private int quantiteMedicament;
    private String sansOrdonnanceMedicament;
    private static List<Medicament> medicaments = new ArrayList<>();

    public Medicament(String nomMedicament, String categorieMedicament,
                      double prixMedicament, String dateMiseEnCirculation,
                      int quantiteMedicament, String sansOrdonnanceMedicament) throws SaisieException {
        this.setNomMedicament(nomMedicament);
        this.setCategorieMedicament(categorieMedicament);
        this.setPrixMedicament(prixMedicament);
        this.setDateMiseEnCirculation(dateMiseEnCirculation);
        this.setQuantiteMedicament(quantiteMedicament);
        this.setSansOrdonnanceMedicament(sansOrdonnanceMedicament);
        medicaments.add(this);
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) throws SaisieException {
        if (!RegexValidator.validerNomMedicament(nomMedicament)) {
            SaisieException e = new SaisieException("Nom du medicament incorrecte !");
            LogUtils.error(logger, "Erreur sur le nom du médicament : " + nomMedicament, e);
            throw e;
        }
        this.nomMedicament = nomMedicament;
    }

    public String getCategorieMedicament() {
        return categorieMedicament;
    }

    public void setCategorieMedicament(String categorieMedicament) throws SaisieException {
        if (!RegexValidator.validerCategorieMedicament(categorieMedicament)) {
            SaisieException e = new SaisieException("Catégorie incorrecte !");
            LogUtils.error(logger, "Erreur sur la catégorie du médicament : " + categorieMedicament, e);
            throw e;
        }
        this.categorieMedicament = categorieMedicament;
    }

    public double getPrixMedicament() {
        return prixMedicament;
    }

    public void setPrixMedicament(double prixMedicament) throws SaisieException {
        if (!RegexValidator.validerPrix(prixMedicament)) {
            SaisieException e = new SaisieException("Prix ne peut pas être négatif !");
            LogUtils.error(logger, "Erreur sur le prix du médicament : " + prixMedicament, e);
            throw e;
        }
        this.prixMedicament = prixMedicament;
    }

    public String getDateMiseEnCirculation() {
        return dateMiseEnCirculation;
    }

    public void setDateMiseEnCirculation(String dateMiseEnCirculation) throws SaisieException {
        if (dateMiseEnCirculation == null) {
            SaisieException e = new SaisieException("Date doit être rentrée !");
            LogUtils.error(logger, "Erreur sur la date de mise en circulation", e);
            throw e;
        }
        this.dateMiseEnCirculation = dateMiseEnCirculation;
    }

    public int getQuantiteMedicament() {
        return quantiteMedicament;
    }

    public void setQuantiteMedicament(int quantiteMedicament) throws SaisieException {
        if (!RegexValidator.validerQuantite(quantiteMedicament)) {
            SaisieException e = new SaisieException("Quantité ne peut pas être négatif !");
            LogUtils.error(logger, "Erreur sur la quantité du médicament : " + quantiteMedicament, e);
            throw e;
        }
        this.quantiteMedicament = quantiteMedicament;
    }

    public String getSansOrdonnanceMedicament() {
        return sansOrdonnanceMedicament;
    }

    public void setSansOrdonnanceMedicament(String sansOrdonnanceMedicament) {
        if (!RegexValidator.validerSansOrdonnance(sansOrdonnanceMedicament)) {
            this.sansOrdonnanceMedicament = sansOrdonnanceMedicament.toLowerCase();
        } else {
            this.sansOrdonnanceMedicament = sansOrdonnanceMedicament;
        }
    }

    public static List<Medicament> getMedicaments() {
        return medicaments;
    }

    public static void setMedicaments(List<Medicament> medicaments) {
        Medicament.medicaments = medicaments;
    }

    public static List<Medicament> rechercherMedicamentParNom(String nom) {
        List<Medicament> resultats = new ArrayList<>();
        for (Medicament m : medicaments) {
            if (m.getNomMedicament().toLowerCase().contains(nom.trim().toLowerCase())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static List<Medicament> rechercherMedicamentParCategorie(String categorie) {
        List<Medicament> resultats = new ArrayList<>();
        for (Medicament m : medicaments) {
            if (m.getCategorieMedicament().toLowerCase().contains(categorie.trim().toLowerCase())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static List<Medicament> rechercherMedicamentParDisponibiliteSansOrdonnance(String disponibilite) {
        List<Medicament> resultats = new ArrayList<>();
        for (Medicament m : medicaments) {
            if (m.getSansOrdonnanceMedicament().equalsIgnoreCase(disponibilite.trim())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public void retirerDuStock(int quantite) {
        if (quantite <= quantiteMedicament) {
            this.quantiteMedicament -= quantite;
        } else {
            LogUtils.error(logger, "Stock insuffisant pour le médicament : " + nomMedicament, null);
        }
    }

    public static void supprimerMedicament(Medicament medicament) {
        medicaments.remove(medicament);
    }

    @Override
    public String toString() {
        return "Nom du medicament : " + this.nomMedicament +
                "\nCatégorie du medicament : " + this.categorieMedicament +
                "\nPrix du medicament : " + this.prixMedicament +
                "\nDate de mise sur le marché : " + this.dateMiseEnCirculation +
                "\nQuantité du medicament : " + this.quantiteMedicament +
                "\nDisponible sans ordonnance : " + this.sansOrdonnanceMedicament;
    }
}