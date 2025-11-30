package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Personne {
    private static final Logger logger = LoggerFactory.getLogger(Personne.class);
    private String nom, prenom, adresse, codePostal, ville, telephone, email;

    public Personne() {
    }

    public Personne(String nom, String prenom, String adresse, String codePostal,
                    String ville, String telephone, String email) throws SaisieException {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setAdresse(adresse);
        this.setCodePostal(codePostal);
        this.setVille(ville);
        this.setTelephone(telephone);
        this.setEmail(email);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws SaisieException {
        if (!RegexValidator.validerMots(nom)) {
            LogUtils.warn(logger, "Erreur dans le nom ! Merci de corriger");
            throw new SaisieException("Erreur dans le nom ! Merci de corriger");
        }
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) throws SaisieException {
        if (!RegexValidator.validerMots(prenom)) {
            LogUtils.warn(logger, "Erreur dans le prénom ! Merci de corriger");
            throw new SaisieException("Erreur dans le prénom ! Merci de corriger");
        }
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) throws SaisieException {
        if (!RegexValidator.validerAdresse(adresse)) {
            LogUtils.warn(logger, "Erreur dans l'adresse ! Merci de corriger");
            throw new SaisieException("Erreur dans l'adresse ! Merci de corriger");
        }
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) throws SaisieException {
        if (!RegexValidator.validerCodePostal(codePostal)) {
            LogUtils.warn(logger, "Erreur dans le code postal ! Merci de corriger");
            throw new SaisieException("Erreur dans le code postal ! Merci de corriger");
        }
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) throws SaisieException {
        if (!RegexValidator.validerVille(ville)) {
            LogUtils.warn(logger, "Erreur dans la ville ! Merci de corriger");
            throw new SaisieException("Erreur dans la ville ! Merci de corriger");
        }
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) throws SaisieException {
        if (!RegexValidator.validerTelephone(telephone)) {
            LogUtils.warn(logger, "Erreur le N° de téléphone est incorrect ! Merci de corriger");
            throw new SaisieException("Erreur le N° de téléphone est incorrect ! Merci de corriger");
        }
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws SaisieException {
        if (!RegexValidator.validerEmail(email)) {
            LogUtils.warn(logger, "Erreur Mail est incorrecte ! Merci de corriger");
            throw new SaisieException("Erreur Mail est incorrecte ! Merci de corriger");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "\nNom : " + nom
                + "\nPrénom        : " + prenom
                + "\nAdresse       : " + adresse
                + "\nCodePostal    : " + codePostal
                + "\nVille         : " + ville
                + "\nTéléphone     : " + telephone
                + "\nEmail         : " + email;
    }
}