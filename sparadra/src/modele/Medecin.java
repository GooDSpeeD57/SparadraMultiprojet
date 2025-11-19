package modele;

import exception.SaisieException;
import utilitaires.RegexValidator;

import java.util.ArrayList;
import java.util.List;

public class Medecin extends Personne {
    private String rPPS;

    private static List<Medecin> medecins = new ArrayList<>();

    public Medecin(String nom, String prenom, String adresse, String codePostal, String ville, String telephone, String email, String rPPS) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setRPPS(rPPS);
        medecins.add(this);
    }

    public String getRPPS() {
        return this.rPPS;
    }

    public void setRPPS(String rPPS) throws SaisieException {
        if (!RegexValidator.validerRPPS(rPPS)) {
            throw new SaisieException("RPPS non valide ! Merci de saisir 11 chiffres commencent par 10 ");
        } else {
            this.rPPS = rPPS;
        }
    }

    public static List<Medecin> getMedecins() {
        return medecins;
    }

    public static void setMedecins(List<Medecin> medecins) {
        Medecin.medecins = medecins;
    }

    public static List<Medecin> rechercherParNom(String nom) {
        List<Medecin> resultats = new ArrayList<>();
        for (Medecin m : medecins) {
            if (m.getNom() != null && m.getNom().toLowerCase().contains(nom.trim().toLowerCase())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static List<Medecin> rechercherParRpps(String Rpps) {
        List <Medecin> resultats = new ArrayList<>();
        for (Medecin m : medecins) {
            if (m.getRPPS() != null && m.getRPPS().equals(Rpps.trim())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static List<Medecin> rechercherParEmail(String email) {
        List<Medecin> resultats = new ArrayList<>();
        for (Medecin m : medecins) {
            if (m.getEmail() != null && m.getEmail().equalsIgnoreCase(email.trim())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    public static void supprimerMedecin(Medecin medecin) {
        medecins.remove(medecin);
    }

    public String toString() {
        return super.toString()+"\nN° RPPS: "+this.rPPS;
    }
}
