package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Regime {
    private static final Logger logger = LoggerFactory.getLogger(Regime.class);
    private Integer idRegime;
    private String nomRegime;
    private double tauxRemboursement;

    public Regime() {
    }

    public Regime(Integer idRegime) {
        this.idRegime = idRegime;
    }

    public Regime(String nomRegime, double tauxRemboursement) throws SaisieException {
        this.setNomRegime(nomRegime);
        this.setTauxRemboursement(tauxRemboursement);
    }

    public Regime(Integer idRegime, String nomRegime, double tauxRemboursement) throws SaisieException {
        this.setIdRegime(idRegime);
        this.setNomRegime(nomRegime);
        this.setTauxRemboursement(tauxRemboursement);
    }

    public Integer getIdRegime() {
        return idRegime;
    }

    public void setIdRegime(Integer idRegime) {
        this.idRegime = idRegime;
    }

    public String getNomRegime() {
        return nomRegime;
    }

    public void setNomRegime(String nomRegime) throws SaisieException {
        if (!RegexValidator.validerMots(nomRegime)) {
            LogUtils.warn(logger, "Nom du régime invalide !");
            throw new SaisieException("Nom du régime invalide !");
        }
        this.nomRegime = nomRegime;
    }

    public double getTauxRemboursement() {
        return tauxRemboursement;
    }

    public void setTauxRemboursement(double tauxRemboursement) throws SaisieException {
        if (tauxRemboursement < 0 || tauxRemboursement > 100) {
            LogUtils.warn(logger, "Le taux de remboursement doit être entre 0 et 100 !");
            throw new SaisieException("Le taux de remboursement doit être entre 0 et 100 !");
        }
        this.tauxRemboursement = tauxRemboursement;
    }

    @Override
    public String toString() {
        return "\nRegime"
                +"\nidRegime            : " + idRegime
                +"\nnomRegime           : " + nomRegime
                + "\ntauxRemboursement   : " + String.format("%.2f", tauxRemboursement) + " %";
    }
}