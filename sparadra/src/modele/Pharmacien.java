package modele;

import exception.SaisieException;
import utilitaires.RegexValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Pharmacien implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nom,prenom,rPPS;

    private static List<Pharmacien> pharmacien = new ArrayList<>();

    public Pharmacien(String nom, String prenom,String rPPS)
            throws SaisieException {
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
            throw new SaisieException("Erreur dans le nom ! Pharmacien Merci de corriger"+nom);
        }
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) throws SaisieException {
        if (!RegexValidator.validerMots(prenom)) {
            throw new SaisieException("Erreur dans le prénom !  Merci de corriger"+prenom);
        }
        this.prenom = prenom;
    }

    public String getRPPS() {
        return this.rPPS;
    }

    public void setRPPS(String rPPS) throws SaisieException {
        if (!RegexValidator.validerRPPS(rPPS)) {
            throw new SaisieException("RPPS non valide  Pharmacien ! Merci de saisir 11 chiffres commencent par 10 "+rPPS);
        } else {
            this.rPPS = rPPS;
        }
    }

    public static List<Pharmacien> getPharmacien() {
        return pharmacien;
    }

    public static void setPharmacien(List<Pharmacien> pharmacien) {
        Pharmacien.pharmacien=pharmacien;
    }

    public String toString(){return
            "\nNom : "+this.nom
            +"\nPrénom : "+this.prenom
             +"\nRépertoire Partagé des Professionnels de Santé : "+this.rPPS;
    }
}


