package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Mutuelle {

    private int idMutuelle;  // nouvel attribut
    private String nom;
    private String adresse;
    private String codePostal;
    private String ville;
    private String telephone;
    private String email;
    private String departement;
    private int tRemboursement;

    // Constructeur vide
    public Mutuelle() {
    }

    // Constructeur complet avec ID
    public Mutuelle(int idMutuelle, String nom, String adresse, String codePostal, String ville,
                    String telephone, String email, String departement, int tRemboursement) throws SaisieException {
        this.idMutuelle = idMutuelle;
        this.setNom(nom);
        this.setAdresse(adresse);
        this.setCodePostal(codePostal);
        this.setVille(ville);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setDepartement(departement);
        this.setTRemboursement(tRemboursement);
    }

    // Getters et setters
    public int getIdMutuelle() {
        return idMutuelle;
    }

    public void setIdMutuelle(int idMutuelle) {
        this.idMutuelle = idMutuelle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws SaisieException {
        if (!RegexValidator.validerMots(nom)) {
            throw new SaisieException("Nom Mutuelle invalide : " + nom);
        }
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) throws SaisieException {
        if (!RegexValidator.validerAdresse(adresse)) {
            throw new SaisieException("Adresse Mutuelle invalide : " + adresse);
        }
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) throws SaisieException {
        if (!RegexValidator.validerCodePostal(codePostal)) {
            throw new SaisieException("Code postal Mutuelle invalide : " + codePostal);
        }
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) throws SaisieException {
        if (!RegexValidator.validerVille(ville)) {
            throw new SaisieException("Ville Mutuelle invalide : " + ville);
        }
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) throws SaisieException {
        if (!RegexValidator.validerTelephone(telephone)) {
            throw new SaisieException("Téléphone Mutuelle invalide : " + telephone);
        }
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws SaisieException {
        if (!RegexValidator.validerEmail(email)) {
            throw new SaisieException("Email Mutuelle invalide : " + email);
        }
        this.email = email;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) throws SaisieException {
        if (!RegexValidator.validerVille(departement)) {
            throw new SaisieException("Département Mutuelle invalide : " + departement);
        }
        this.departement = departement;
    }

    public int getTRemboursement() {
        return tRemboursement;
    }

    public void setTRemboursement(int tRemboursement) throws SaisieException {
        if (!RegexValidator.validerTauxRemboursement(tRemboursement)) {
            throw new SaisieException("Taux de remboursement invalide : " + tRemboursement);
        }
        this.tRemboursement = tRemboursement;
    }

    @Override
    public String toString() {
        return "\nMutuelle"
                + "\nID Mutuelle            : " + idMutuelle
                + "\nNom                    : " + nom
                + "\nAdresse                : " + adresse
                + "\nCodePostal             : " + codePostal
                + "\nVille                  : " + ville
                + "\nTelephone              : " + telephone
                + "\nEmail                  : " + email
                + "\nDépartement            : " + departement
                + "\nTaux de remboursement  : " + tRemboursement;
    }
}
