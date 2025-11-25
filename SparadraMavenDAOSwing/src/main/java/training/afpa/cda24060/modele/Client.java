package training.afpa.cda24060.modele;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.RegexValidator;

import java.time.LocalDate;

public class Client extends Personne {

    private int id_Client;
    private String nssClient;
    private LocalDate dateNaissance;

    private Regime regime;
    private Medecin medecin;
    private Mutuelle mutuelle;

    private String idTitulaireMutuelle;

    public Client() { super(); }

    public Client(String nom, String prenom, String adresse, String codePostal, String ville,
                  String telephone, String email, String nssClient, LocalDate dateNaissance,
                  Regime regime, Medecin medecin, Mutuelle mutuelle,
                  String idTitulaireMutuelle) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        setNssClient(nssClient);
        setDateNaissance(dateNaissance);
        this.regime = regime;
        this.medecin = medecin;
        this.mutuelle = mutuelle;
        this.idTitulaireMutuelle = idTitulaireMutuelle;
    }

    // ID
    public int getId_Client() { return id_Client; }
    public void setId_Client(int id_Client) { this.id_Client = id_Client; }

    // NSS
    public String getNssClient() { return nssClient; }
    public void setNssClient(String nssClient) throws SaisieException {
        if (!RegexValidator.validerNSS(nssClient))
            throw new SaisieException("NSS incorrect ! 12 chiffres attendus.");
        this.nssClient = nssClient;
    }

    // Date de naissance
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    // Objets associés
    public Regime getRegime() { return regime; }
    public void setRegime(Regime regime) { this.regime = regime; }

    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }

    public Mutuelle getMutuelle() { return mutuelle; }
    public void setMutuelle(Mutuelle mutuelle) { this.mutuelle = mutuelle; }

    // Titulaire mutuelle
    public String getIdTitulaireMutuelle() { return idTitulaireMutuelle; }
    public void setIdTitulaireMutuelle(String idTitulaireMutuelle) { this.idTitulaireMutuelle = idTitulaireMutuelle; }

    @Override
    public String toString() {
        return super.toString() +
                "\nID Client       : " + id_Client +
                "\nNSS             : " + nssClient +
                "\nDate Naissance  : " + (dateNaissance != null ? dateNaissance : "Non défini") +
                "\nRégime          : " + (regime != null ? regime.getNomRegime() : "Non défini") +
                "\nMutuelle        : " + (mutuelle != null ? mutuelle.getNomMutuelle() : "Non défini") +
                "\nMédecin         : " + (medecin != null ? medecin.getNom() : "Non défini") +
                "\nTitulaire Mut.  : " + (idTitulaireMutuelle != null ? idTitulaireMutuelle : "Non défini");
    }
}
