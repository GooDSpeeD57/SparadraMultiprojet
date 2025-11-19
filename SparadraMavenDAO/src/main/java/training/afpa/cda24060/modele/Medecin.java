package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Medecin extends Personne {

    private int idMedecin;
    private String rPPS;

    public Medecin() {
        super();
    }

    public Medecin(String nom, String prenom, String adresse, String codePostal, String ville,
                   String telephone, String email, String rPPS) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setRPPS(rPPS);
    }


    public int getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(int idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getRPPS() {
        return rPPS;
    }

    public void setRPPS(String rPPS) throws SaisieException {
        if (!RegexValidator.validerRPPS(rPPS)) {
            throw new SaisieException("RPPS non valide ! Merci de saisir 11 chiffres commencent par 10.");
        }
        this.rPPS = rPPS;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nID Médecin : " + idMedecin
                + "\nN° RPPS    : " + rPPS;
    }
}