package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mutuelle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Mutuelle.class);

    private String nom, adresse, codePostal, ville, telephone, email, departement;
    private int tRemboursement;
    private static List<Mutuelle> mutuelles = new ArrayList<>();

    public Mutuelle(String nom, String adresse, String codePostal,
                    String ville, String telephone, String email, String departement, int tRemboursement)
            throws SaisieException {
        this.setNom(nom);
        this.setAdresse(adresse);
        this.setCodePostal(codePostal);
        this.setVille(ville);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setDepartement(departement);
        this.setTRemboursement(tRemboursement);
        mutuelles.add(this);
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) throws SaisieException {
        if (!RegexValidator.validerMots(nom)) {
            SaisieException e = new SaisieException("Erreur dans le nom Mutuelle ! Merci de corriger " + nom);
            LogUtils.error(logger, "Nom invalide pour la mutuelle : " + nom, e);
            throw e;
        }
        this.nom = nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) throws SaisieException {
        if (!RegexValidator.validerAdresse(adresse)) {
            SaisieException e = new SaisieException("Erreur dans l'adresse Mutuelle ! Merci de corriger " + adresse);
            LogUtils.error(logger, "Adresse invalide pour la mutuelle : " + adresse, e);
            throw e;
        }
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public void setCodePostal(String codePostal) throws SaisieException {
        if (!RegexValidator.validerCodePostal(codePostal)) {
            SaisieException e = new SaisieException("Erreur dans le code postal Mutuelle ! Merci de corriger " + codePostal);
            LogUtils.error(logger, "Code postal invalide pour la mutuelle : " + codePostal, e);
            throw e;
        }
        this.codePostal = codePostal;
    }

    public String getVille() {
        return this.ville;
    }

    public void setVille(String ville) throws SaisieException {
        if (!RegexValidator.validerVille(ville)) {
            SaisieException e = new SaisieException("Erreur dans la ville Mutuelle ! Merci de corriger " + ville);
            LogUtils.error(logger, "Ville invalide pour la mutuelle : " + ville, e);
            throw e;
        }
        this.ville = ville;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) throws SaisieException {
        if (!RegexValidator.validerTelephone(telephone)) {
            SaisieException e = new SaisieException("Erreur le N° de telephone est incorrecte Mutuelle ! Merci de corriger " + telephone);
            LogUtils.error(logger, "Téléphone invalide pour la mutuelle : " + telephone, e);
            throw e;
        }
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) throws SaisieException {
        if (!RegexValidator.validerEmail(email)) {
            SaisieException e = new SaisieException("Erreur Mail est incorrecte Mutuelle ! Merci de corriger " + email);
            LogUtils.error(logger, "Email invalide pour la mutuelle : " + email, e);
            throw e;
        }
        this.email = email;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) throws SaisieException {
        if (!RegexValidator.validerVille(departement)) {
            SaisieException e = new SaisieException("Département inconnu Mutuelle " + departement);
            LogUtils.error(logger, "Département invalide pour la mutuelle : " + departement, e);
            throw e;
        }
        this.departement = departement;
    }

    public int getTRemboursement() {
        return tRemboursement;
    }

    public void setTRemboursement(int tRemboursement) throws SaisieException {
        if (!RegexValidator.validerTauxRemboursement(tRemboursement)) {
            SaisieException e = new SaisieException("Taux de remboursement invalide ! Doit être entre 0 et 100. " + tRemboursement);
            LogUtils.error(logger, "Taux de remboursement invalide pour la mutuelle : " + tRemboursement, e);
            throw e;
        }
        this.tRemboursement = tRemboursement;
    }

    public static List<Mutuelle> getMutuelles() {
        return mutuelles;
    }

    public static void setMutuelles(List<Mutuelle> mutuelles) {
        Mutuelle.mutuelles = mutuelles;
    }

    public static List<Mutuelle> rechercherMutuelleParNom(String nom) {
        List<Mutuelle> resultats = new ArrayList<>();
        for (Mutuelle m : mutuelles) {
            if (m.getNom().toLowerCase().contains(nom.trim().toLowerCase())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static List<Mutuelle> rechercherMutuelleParDepartement(String departement) {
        List<Mutuelle> resultats = new ArrayList<>();
        for (Mutuelle m : mutuelles) {
            if (m.getDepartement().toLowerCase().contains(departement.trim().toLowerCase())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static void supprimerMutuelle(Mutuelle mutuelle) {
        mutuelles.remove(mutuelle);
    }

    @Override
    public String toString() {
        return "\nMutuelle"
                + "\nNom : " + this.nom
                + "\nAdresse : " + this.adresse
                + "\nCodePostal : " + this.codePostal
                + "\nVille : " + this.ville
                + "\nTelephone : " + this.telephone
                + "\nEmail : " + this.email
                + "\nDépartement : " + this.departement
                + "\nTaux de remboursement : " + this.tRemboursement;
    }
}
