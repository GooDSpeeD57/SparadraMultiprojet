package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Pharmacien extends Personne {

    private static final Logger logger = LoggerFactory.getLogger(Pharmacien.class);
    private String rpps;

    public Pharmacien() {
        super();
    }

    public Pharmacien(String nom, String prenom, String adresse, String codePostal, String ville,
                      String telephone, String email, String rpps) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setRpps(rpps);
    }

    public Pharmacien(Integer id, String nom, String prenom, String adresse, String codePostal, String ville,
                      String telephone, String email, String rpps) throws SaisieException {
        super(id, nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setRpps(rpps);
    }

    public String getRpps() {
        return rpps;
    }

    public void setRpps(String rpps) throws SaisieException {
        if (!RegexValidator.validerRPPS(rpps)) {
            String message = "RPPS non valide ! Merci de saisir 11 chiffres commençant par 10.";
            LogUtils.warn(logger, message); // log WARN pour avertir l’utilisateur
            throw new SaisieException(message);
        }
        this.rpps = rpps;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nN° RPPS    : " + rpps;
    }
}