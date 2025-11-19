package modele;

import exception.SaisieException;
import utilitaires.RegexValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Mutuelle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nom,adresse,codePostal,ville,telephone,email,departement;
    private int tRemboursement;
    private static List<Mutuelle> mutuelles = new ArrayList<>();

    public Mutuelle(String nom, String adresse, String codePostal,
                     String ville, String telephone, String email,String departement,int tRemboursement)
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
            throw new SaisieException("Erreur dans le nom Mutuelle ! Merci de corriger"+nom);
        }
        this.nom = nom;
    }

    public String getAdresse() {return this.adresse;}

    public void setAdresse(String adresse) throws SaisieException {
        if (!RegexValidator.validerAdresse(adresse)) {
            throw new SaisieException("Erreur dans l'adresse  Mutuelle ! Merci de corriger"+adresse);
        }
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public void setCodePostal(String codePostal) throws SaisieException {
        if (!RegexValidator.validerCodePostal(codePostal)) {
            throw new SaisieException("Erreur dans le code postal Mutuelle ! Merci de corriger"+codePostal);
        }
        this.codePostal = codePostal;
    }

    public String getVille() {
        return this.ville;
    }

    public void setVille(String ville) throws SaisieException {
        if (!RegexValidator.validerVille(ville)) {
            throw new SaisieException("Erreur dans la ville Mutuelle ! Merci de corriger"+ville);
        }
        this.ville = ville;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) throws SaisieException {
        if (!RegexValidator.validerTelephone(telephone)) {
            throw new SaisieException("Erreur le N° de telephone est incorrecte  Mutuelle  ! Merci de corriger"+telephone);
        }
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) throws SaisieException {
        if (!RegexValidator.validerEmail(email)) {
            throw new SaisieException("Erreur Mail est incorrecte  Mutuelle  ! Merci de corriger"+email);
        }
        this.email = email;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) throws SaisieException {
        if (!RegexValidator.validerVille(departement)) {
            throw new SaisieException("Département inconnu  Mutuelle "+departement);
        }
        this.departement = departement;
    }

    public int getTRemboursement() {
        return tRemboursement;
    }

    public void setTRemboursement(int tRemboursement) throws SaisieException {
        if (!RegexValidator.validerTauxRemboursement(tRemboursement)) {
            throw new SaisieException("Taux de remboursement invalide ! Doit être entre 0 et 100."+tRemboursement);
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

    public String toString(){return
            "\nMutuelle"
            +"\nNom : "+this.nom
             +"\nAdresse : "+this.adresse
              +"\nCodePostal : "+this.codePostal
               +"\nVille : "+this.ville
                +"\nTelephone : "+this.telephone
                 +"\nEmail : "+this.email
                  +"\nDépartement : " + this.departement
                   +"\nTaux de remboursement : " + this.tRemboursement;
    }
}


