package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Mutuelle {
    private static final Logger logger = LoggerFactory.getLogger(Mutuelle.class);
    private Integer idMutuelle;
    private String nomMutuelle;
    private String adresseMutuelle;
    private String codePostalMutuelle;
    private String villeMutuelle;
    private String telephoneMutuelle;
    private String mailMutuelle;
    private String departementMutuelle;
    private double tRemboursement;

    public Mutuelle() {}

    public Mutuelle(Integer idMutuelle) {
        this.idMutuelle = idMutuelle;
    }

    public Mutuelle( String nomMutuelle, String adresseMutuelle,
                    String codePostalMutuelle, String villeMutuelle,
                    String telephoneMutuelle, String mailMutuelle,
                    String departementMutuelle, double tRemboursement)
            throws SaisieException {
        setNomMutuelle(nomMutuelle);
        setAdresseMutuelle(adresseMutuelle);
        setCodePostalMutuelle(codePostalMutuelle);
        setVilleMutuelle(villeMutuelle);
        setTelephoneMutuelle(telephoneMutuelle);
        setMailMutuelle(mailMutuelle);
        setDepartementMutuelle(departementMutuelle);
        setTRemboursement(tRemboursement);
    }

    public Mutuelle(Integer idMutuelle, String nomMutuelle, String adresseMutuelle,
                    String codePostalMutuelle, String villeMutuelle,
                    String telephoneMutuelle, String mailMutuelle,
                    String departementMutuelle, double tRemboursement)
            throws SaisieException {
        this.idMutuelle = idMutuelle;
        setNomMutuelle(nomMutuelle);
        setAdresseMutuelle(adresseMutuelle);
        setCodePostalMutuelle(codePostalMutuelle);
        setVilleMutuelle(villeMutuelle);
        setTelephoneMutuelle(telephoneMutuelle);
        setMailMutuelle(mailMutuelle);
        setDepartementMutuelle(departementMutuelle);
        setTRemboursement(tRemboursement);
    }

    public Integer getIdMutuelle() {
        return idMutuelle;
    }

    public void setIdMutuelle(Integer idMutuelle) {
        this.idMutuelle = idMutuelle;
    }

    public String getNomMutuelle() {
        return nomMutuelle;
    }

    public void setNomMutuelle(String nomMutuelle) throws SaisieException {
        if (!RegexValidator.validerMots(nomMutuelle)) {
            String message = "Nom Mutuelle invalide : " + nomMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.nomMutuelle = nomMutuelle;
    }

    public String getAdresseMutuelle() {
        return adresseMutuelle;
    }

    public void setAdresseMutuelle(String adresseMutuelle) throws SaisieException {
        if (!RegexValidator.validerAdresse(adresseMutuelle)) {
            String message = "Adresse Mutuelle invalide : " + adresseMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.adresseMutuelle = adresseMutuelle;
    }

    public String getCodePostalMutuelle() {
        return codePostalMutuelle;
    }

    public void setCodePostalMutuelle(String codePostalMutuelle) throws SaisieException {
        if (!RegexValidator.validerCodePostal(codePostalMutuelle)) {
            String message = "Code postal Mutuelle invalide : " + codePostalMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.codePostalMutuelle = codePostalMutuelle;
    }

    public String getVilleMutuelle() {
        return villeMutuelle;
    }

    public void setVilleMutuelle(String villeMutuelle) throws SaisieException {
        if (!RegexValidator.validerVille(villeMutuelle)) {
            String message = "Ville Mutuelle invalide : " + villeMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.villeMutuelle = villeMutuelle;
    }

    public String getTelephoneMutuelle() {
        return telephoneMutuelle;
    }

    public void setTelephoneMutuelle(String telephoneMutuelle) throws SaisieException {
        if (!RegexValidator.validerTelephone(telephoneMutuelle)) {
            String message = "Téléphone Mutuelle invalide : " + telephoneMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.telephoneMutuelle = telephoneMutuelle;
    }

    public String getMailMutuelle() {
        return mailMutuelle;
    }

    public void setMailMutuelle(String mailMutuelle) throws SaisieException {
        if (!RegexValidator.validerEmail(mailMutuelle)) {
            String message = "Email Mutuelle invalide : " + mailMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.mailMutuelle = mailMutuelle;
    }

    public String getDepartementMutuelle() {
        return departementMutuelle;
    }

    public void setDepartementMutuelle(String departementMutuelle) throws SaisieException {
        if (!RegexValidator.validerVille(departementMutuelle)) {
            String message = "Département Mutuelle invalide : " + departementMutuelle;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.departementMutuelle = departementMutuelle;
    }

    public double getTRemboursement() {
        return tRemboursement;
    }

    public void setTRemboursement(double tRemboursement) throws SaisieException {
        if (!RegexValidator.validerTauxRemboursement(tRemboursement)) {
            String message = "Taux de remboursement invalide : " + tRemboursement;
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.tRemboursement = tRemboursement;
    }

    @Override
    public String toString() {
        return "\nMutuelle"
                +"\nid                      : " + idMutuelle
                +"\nNom                     : " + nomMutuelle
                +"\nAdresse                 : " + adresseMutuelle
                +"\nCodePostal              : " + codePostalMutuelle
                +"\nVille                   : " + villeMutuelle
                +"\nTelephone               : " + telephoneMutuelle
                +"\nEmail                   : " + mailMutuelle
                +"\nDépartement             : " + departementMutuelle
                +"\nTaux de Remboursement   : " + tRemboursement ;

    }
}