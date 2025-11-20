package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Client extends Personne {

    private int idClient;
    private String nss;
    private LocalDate dateNaissance;  // 🔹 Changement ici
    private Regime regime;
    private Medecin medecin;
    private Mutuelle mutuelle;
    private String idTitulaireMutuelle;

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Client() {
        super();
    }

    public Client(String nom, String prenom, String adresse, String codePostal, String ville,
                  String telephone, String email, String nss, String dateNaissance,
                  Regime regime, Medecin medecin, Mutuelle mutuelle,
                  String idTitulaireMutuelle) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setNss(nss);
        this.setDateNaissance(dateNaissance);  // Utilisation du setter
        this.setRegime(regime);
        this.setMedecin(medecin);
        this.setMutuelle(mutuelle);
        this.setIdTitulaireMutuelle(idTitulaireMutuelle);
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) throws SaisieException {
        if (!RegexValidator.validerNSS(nss)) {
            throw new SaisieException("Numéro de Sécurité Sociale incorrect ! 15 chiffres attendus.");
        }
        this.nss = nss;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) throws SaisieException {
        if (!RegexValidator.validerDateNaissance(dateNaissance)) {
            throw new SaisieException("Format de date incorrect ! Format attendu : Jour/Mois/Année.");
        }
        try {
            this.dateNaissance = LocalDate.parse(dateNaissance, FORMAT_DATE);
        } catch (DateTimeParseException e) {
            throw new SaisieException("Impossible de parser la date de naissance.");
        }
    }

    public Regime getRegime() {
        return regime;
    }

    public void setRegime(Regime regime) {
        this.regime = regime;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getIdTitulaireMutuelle() {
        return idTitulaireMutuelle;
    }

    public void setIdTitulaireMutuelle(String idTitulaireMutuelle) {
        this.idTitulaireMutuelle = idTitulaireMutuelle;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nID Client                  : " + idClient
                + "\nNuméro de Sécurité Sociale : " + nss
                + "\nDate de Naissance          : " + (dateNaissance != null ? dateNaissance.format(FORMAT_DATE) : "Non défini")
                + "\nRégime                     : " + (regime != null ? regime.getNomRegime() : "Non défini")
                + "\nMutuelle                   : " + (mutuelle != null ? mutuelle.getNom() : "Non défini")
                + "\nMédecin Référent           : " + (medecin != null ? medecin.getNom() : "Non défini")
                + "\nTitulaire Mutuelle         : " + (idTitulaireMutuelle != null ? idTitulaireMutuelle : "Non défini");
    }
}
