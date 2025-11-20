package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Regime {

    private int idRegime;
    private String nomRegime;
    private double tauxRemboursement;

    public Regime() {
    }

    public Regime(String nomRegime, double tauxRemboursement) throws SaisieException {
        this.setNomRegime(nomRegime);
        this.setTauxRemboursement(tauxRemboursement);
    }

    public int getIdRegime() {
        return idRegime;
    }

    public void setIdRegime(int idRegime) {
        this.idRegime = idRegime;
    }

    public String getNomRegime() {
        return nomRegime;
    }

    public void setNomRegime(String nomRegime) throws SaisieException {
        if (!RegexValidator.validerMots(nomRegime)) {
            throw new SaisieException("Nom du régime invalide !");
        }
        this.nomRegime = nomRegime;
    }

    public double getTauxRemboursement() {
        return tauxRemboursement;
    }

    public void setTauxRemboursement(double tauxRemboursement) throws SaisieException {
        if (tauxRemboursement < 0 || tauxRemboursement > 100) {
            throw new SaisieException("Le taux de remboursement doit être entre 0 et 100 !");
        }
        this.tauxRemboursement = tauxRemboursement;
    }

    @Override
    public String toString() {
        return "\nRegime"
                +"\nidRegime            : " + idRegime
                +"\nnomRegime           : " + nomRegime
                +"\ntauxRemboursement   : " + tauxRemboursement;
    }
}
