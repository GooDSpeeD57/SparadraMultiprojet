package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Pharmacien extends Personne {

    private int idPharmacien; // auto-incrémenté en DB
    private String rPPS;

    public Pharmacien() {
        super();
    }

    public Pharmacien(String nom, String prenom, String adresse, String codePostal, String ville,
                      String telephone, String email, String rPPS) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setRPPS(rPPS);
    }

    public int getIdPharmacien() {
        return idPharmacien;
    }

    public void setIdPharmacien(int idPharmacien) {
        this.idPharmacien = idPharmacien;
    }

    public String getRPPS() {
        return rPPS;
    }

    public void setRPPS(String rPPS) throws SaisieException {
        if (!RegexValidator.validerRPPS(rPPS)) {
            throw new SaisieException("RPPS non valide ! Merci de saisir 11 chiffres commençant par 10.");
        }
        this.rPPS = rPPS;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nID Pharmacien : " + idPharmacien
                + "\nRPPS          : " + rPPS;
    }
}
