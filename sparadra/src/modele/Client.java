package modele;

import exception.SaisieException;
import utilitaires.RegexValidator;

import java.util.ArrayList;
import java.util.List;

public class Client extends Personne {
    private String nss,dateNaissance,mutuelle,medecinRef;
    private static List<Client> clients = new ArrayList<>();

    public Client(String nom, String prenom, String adresse, String codePostal, String ville,
                   String telephone, String email,String nss,String dateNaissance,
                   String mutuelle,String medecinRef)throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setNss(nss);
        this.setDateNaissance (dateNaissance);
        this.setMutuelle (mutuelle);
        this.setMedecinRef(medecinRef);
        clients.add(this);
    }

    public String getNss() {
        return this.nss;
    }

    public void setNss(String nss) throws SaisieException {
        if (!RegexValidator.validerNSS(nss)) {
            throw new SaisieException("Numéro de Sécurité Sociale incorrect ! 15 chiffres attendus.");
        }
        this.nss = nss;
    }

    public String getDateNaissance() {
        return this.dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) throws SaisieException {
        if (!RegexValidator.validerDateNaissance(dateNaissance)) {
            throw new SaisieException("Format de date incorrect ! Format attendu : Jour/Mois/Année.");
        }
        this.dateNaissance = dateNaissance;
    }

    public String getMutuelle() {
        return this.mutuelle;
    }

    public void setMutuelle(String mutuelle) throws SaisieException {
        if (!RegexValidator.validerMots(mutuelle)) {
            throw new SaisieException("Nom de mutuelle incorrect ! Si le client a oublié sa mutuelle, veuillez sélectionner \"Pas de Mutuelle\".");
        }
        this.mutuelle = mutuelle;
    }

    public String getMedecinRef() {
        return medecinRef;
    }

    public void setMedecinRef(String medecinRef) throws SaisieException {
        if (!RegexValidator.validerMots(medecinRef)) {
            throw new SaisieException("Sélectionnez un médecin ou créez-en un");
        }
        this.medecinRef = medecinRef;
    }

    public static List<Client> getClients() {
        return clients;
    }

    public static void setClients(List<Client> clients) {
        Client.clients = clients;
    }

    public static List<Client> rechercherClientParNom(String nom) {
        List<Client> resultats = new ArrayList<>();
        for (Client c : clients) {
            if (c.getNom() != null && c.getNom().toLowerCase().contains(nom.trim().toLowerCase())) {
                resultats.add(c);
            }
        }
        return resultats;
    }

    public static List<Client> rechercherClientParNss(String nss) {
        List<Client> resultats = new ArrayList<>();
        for (Client c : clients) {
            if (c.getNss() != null && c.getNss().equals(nss.trim())) {
                resultats.add(c);
            }
        }
        return resultats;
    }

    public static List<Client> rechercherClientParEmail(String email) {
        List<Client> resultats = new ArrayList<>();
        for (Client c : clients) {
            if (c.getEmail() != null && c.getEmail().equalsIgnoreCase(email.trim())) {
                resultats.add(c);
            }
        }
        return resultats;
    }
    public static void supprimerClient(Client client) {
        clients.remove(client);
    }

@Override
    public String toString(){
        return super.toString()
               +"\nNuméro de Sécurité Sociale : "+this.nss
               +"\nDate de Naissance          : "+this.dateNaissance
               +"\nMutuelle                   : "+this.mutuelle
               +"\nMédecin Référent           : "+this.medecinRef;
    }
}


