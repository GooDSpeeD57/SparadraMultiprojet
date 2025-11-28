package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.DateTimePatternFr;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;
import java.time.LocalDate;


public class Client extends Personne {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private String nss;
    private LocalDate dateNaissance;
    private Regime regime;
    private Medecin medecin;
    private Mutuelle mutuelle;
    private String idTitulaireMutuelle;

    public Client() {
        super();
    }

    public Client(Integer id, String nom, String prenom, String adresse, String codePostal, String ville,
                  String telephone, String email, String nss, String dateNaissance,
                  Regime regime, Medecin medecin, Mutuelle mutuelle,
                  String idTitulaireMutuelle) throws SaisieException {

        super(id, nom, prenom, adresse, codePostal, ville, telephone, email);

        this.setNss(nss);
        this.setDateNaissance(dateNaissance);
        this.setRegime(regime);
        this.setMedecin(medecin);
        this.setMutuelle(mutuelle);
        this.setIdTitulaireMutuelle(idTitulaireMutuelle);
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) throws SaisieException {
        if (!RegexValidator.validerNSS(nss)) {
            String message = "Numéro de Sécurité Sociale incorrect ! 15 chiffres attendus.";
            LogUtils.warn(logger, message);
            throw new SaisieException(message);
        }
        this.nss = nss;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public String getDateNaissanceFormate() {
        return dateNaissance != null ? DateTimePatternFr.formatDate(dateNaissance) : "Non définie";
    }

    public void setDateNaissance(String dateNaissance) throws SaisieException {
        if (!RegexValidator.validerDateNaissance(dateNaissance)) {
            String message = "Format de date incorrect ! Format attendu : Jour/Mois/Année (dd/MM/yyyy).";
            LogUtils.error(logger, message);
            throw new SaisieException(message);
        }

        LocalDate parsedDate = DateTimePatternFr.parseDateFromString(dateNaissance);
        if (parsedDate == null) {
            String message = "Impossible de parser la date de naissance : " + dateNaissance;
            LogUtils.error(logger, message);
            throw new SaisieException(message);
        }

        this.dateNaissance = parsedDate;
    }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Regime getRegime() {
        return regime;
    }

    public void setRegime(Regime regime) {
        if (regime == null) {
            LogUtils.warn(logger, "Régime non défini pour le client.");
        }
        this.regime = regime;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        if (medecin == null) {
            LogUtils.warn(logger, "Médecin non défini pour le client.");
        }
        this.medecin = medecin;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        if (mutuelle == null) {
            LogUtils.warn(logger, "Mutuelle non définie pour le client.");
        }
        this.mutuelle = mutuelle;
    }

    public String getIdTitulaireMutuelle() {
        return idTitulaireMutuelle;
    }

    public void setIdTitulaireMutuelle(String idTitulaireMutuelle) {
        if (idTitulaireMutuelle == null) {
            LogUtils.warn(logger, "ID titulaire de la mutuelle non défini pour le client.");
        }
        this.idTitulaireMutuelle = idTitulaireMutuelle;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nNuméro de Sécurité Sociale : " + (nss != null ? nss : "Non défini")
                + "\nDate de Naissance          : " + getDateNaissanceFormate()
                + "\nRégime                     : " + (regime != null ? regime.getNomRegime() : "Non défini")
                + "\nMutuelle                   : " + (mutuelle != null ? mutuelle.getNomMutuelle() : "Non défini")
                + "\nMédecin Référent           : " + (medecin != null ? medecin.getNom() : "Non défini")
                + "\nTitulaire Mutuelle         : " + (idTitulaireMutuelle != null ? idTitulaireMutuelle : "Non défini");
    }
}