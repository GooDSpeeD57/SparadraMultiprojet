package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

public class Mutuelle {

    private int id_Mutuelle;
    private String nomMutuelle;
    private String adresseMutuelle;
    private String codePostalMutuelle;
    private String villeMutuelle;
    private String telephoneMutuelle;
    private String mailMutuelle;
    private String departementMutuelle;
    private double tRemboursement;

    public Mutuelle() {}

    public Mutuelle(int id_Mutuelle, String nomMutuelle, String adresseMutuelle,
                    String codePostalMutuelle, String villeMutuelle,
                    String telephoneMutuelle, String mailMutuelle,
                    String departementMutuelle, double tRemboursement)
            throws SaisieException {

        this.id_Mutuelle = id_Mutuelle;
        setNomMutuelle(nomMutuelle);
        setAdresseMutuelle(adresseMutuelle);
        setCodePostalMutuelle(codePostalMutuelle);
        setVilleMutuelle(villeMutuelle);
        setTelephoneMutuelle(telephoneMutuelle);
        setMailMutuelle(mailMutuelle);
        setDepartementMutuelle(departementMutuelle);
        setTRemboursement(tRemboursement);
    }

    public int getIdMutuelle() {
        return id_Mutuelle;
    }

    public void setIdMutuelle(int id_Mutuelle) {
        this.id_Mutuelle = id_Mutuelle;
    }

    public String getNomMutuelle() {
        return nomMutuelle;
    }

    public void setNomMutuelle(String nomMutuelle) throws SaisieException {
        if (!RegexValidator.validerMots(nomMutuelle)) {
            throw new SaisieException("Nom Mutuelle invalide : " + nomMutuelle);
        }
        this.nomMutuelle = nomMutuelle;
    }

    public String getAdresseMutuelle() {
        return adresseMutuelle;
    }

    public void setAdresseMutuelle(String adresseMutuelle) throws SaisieException {
        if (!RegexValidator.validerAdresse(adresseMutuelle)) {
            throw new SaisieException("Adresse Mutuelle invalide : " + adresseMutuelle);
        }
        this.adresseMutuelle = adresseMutuelle;
    }

    public String getCodePostalMutuelle() {
        return codePostalMutuelle;
    }

    public void setCodePostalMutuelle(String codePostalMutuelle) throws SaisieException {
        if (!RegexValidator.validerCodePostal(codePostalMutuelle)) {
            throw new SaisieException("Code postal Mutuelle invalide : " + codePostalMutuelle);
        }
        this.codePostalMutuelle = codePostalMutuelle;
    }

    public String getVilleMutuelle() {
        return villeMutuelle;
    }

    public void setVilleMutuelle(String villeMutuelle) throws SaisieException {
        if (!RegexValidator.validerVille(villeMutuelle)) {
            throw new SaisieException("Ville Mutuelle invalide : " + villeMutuelle);
        }
        this.villeMutuelle = villeMutuelle;
    }

    public String getTelephoneMutuelle() {
        return telephoneMutuelle;
    }

    public void setTelephoneMutuelle(String telephoneMutuelle) throws SaisieException {
        if (!RegexValidator.validerTelephone(telephoneMutuelle)) {
            throw new SaisieException("Téléphone Mutuelle invalide : " + telephoneMutuelle);
        }
        this.telephoneMutuelle = telephoneMutuelle;
    }

    public String getMailMutuelle() {
        return mailMutuelle;
    }

    public void setMailMutuelle(String mailMutuelle) throws SaisieException {
        if (!RegexValidator.validerEmail(mailMutuelle)) {
            throw new SaisieException("Email Mutuelle invalide : " + mailMutuelle);
        }
        this.mailMutuelle = mailMutuelle;
    }

    public String getDepartementMutuelle() {
        return departementMutuelle;
    }

    public void setDepartementMutuelle(String departementMutuelle) throws SaisieException {
        if (!RegexValidator.validerVille(departementMutuelle)) {
            throw new SaisieException("Département Mutuelle invalide : " + departementMutuelle);
        }
        this.departementMutuelle = departementMutuelle;
    }

    public double getTRemboursement() {
        return tRemboursement;
    }

    public void setTRemboursement(double tRemboursement) throws SaisieException {
        if (!RegexValidator.validerTauxRemboursement(tRemboursement)) {
            throw new SaisieException("Taux de remboursement invalide : " + tRemboursement);
        }
        this.tRemboursement = tRemboursement;
    }

    @Override
    public String toString() {
        return "Mutuelle { " +
                "id=" + id_Mutuelle +
                ", nom='" + nomMutuelle + '\'' +
                ", adresse='" + adresseMutuelle + '\'' +
                ", codePostal='" + codePostalMutuelle + '\'' +
                ", ville='" + villeMutuelle + '\'' +
                ", telephone='" + telephoneMutuelle + '\'' +
                ", email='" + mailMutuelle + '\'' +
                ", departement='" + departementMutuelle + '\'' +
                ", tauxRemboursement=" + tRemboursement +
                '}';
    }
}
