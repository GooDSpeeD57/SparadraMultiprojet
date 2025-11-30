package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Medecin extends Personne {
    private static final Logger logger = LoggerFactory.getLogger(Medecin.class);
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
            String message = "RPPS non valide ! Merci de saisir 11 chiffres commençant par 10.";
            LogUtils.warn(logger, message); // Log en WARN car l'utilisateur peut resaisir
            throw new SaisieException(message);
        }
        this.rPPS = rPPS;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nID Médecin : " + idMedecin
                + "\nN° RPPS    : " + rPPS ;
    }
}