package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pharmacien implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Pharmacien.class.getName());
    private String nom, prenom, rPPS;
    private static List<Pharmacien> pharmacien = new ArrayList<>();

    public Pharmacien(String nom, String prenom, String rPPS) throws SaisieException {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setRPPS(rPPS);
        pharmacien.add(this);
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) throws SaisieException {
        if (!RegexValidator.validerMots(nom)) {
            LOGGER.log(Level.SEVERE, "Erreur dans le nom ! Pharmacien : {0}", nom);
            throw new SaisieException("Erreur dans le nom ! Pharmacien Merci de corriger " + nom);
        }
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) throws SaisieException {
        if (!RegexValidator.validerMots(prenom)) {
            LOGGER.log(Level.SEVERE, "Erreur dans le prénom ! Pharmacien : {0}", prenom);
            throw new SaisieException("Erreur dans le prénom ! Merci de corriger " + prenom);
        }
        this.prenom = prenom;
    }

    public String getRPPS() {
        return this.rPPS;
    }

    public void setRPPS(String rPPS) throws SaisieException {
        if (!RegexValidator.validerRPPS(rPPS)) {
            LOGGER.log(Level.SEVERE, "RPPS non valide Pharmacien : {0}", rPPS);
            throw new SaisieException("RPPS non valide Pharmacien ! Merci de saisir 11 chiffres commençant par 10 " + rPPS);
        }
        this.rPPS = rPPS;
    }

    public static List<Pharmacien> getPharmacien() {
        return pharmacien;
    }

    public static void setPharmacien(List<Pharmacien> pharmacien) {
        Pharmacien.pharmacien = pharmacien;
    }

    @Override
    public String toString() {
        return "\nNom : " + this.nom
                + "\nPrénom : " + this.prenom
                + "\nRépertoire Partagé des Professionnels de Santé : " + this.rPPS;
    }
}