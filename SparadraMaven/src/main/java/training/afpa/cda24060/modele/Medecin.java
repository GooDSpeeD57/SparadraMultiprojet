package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.RegexValidator;

import java.util.ArrayList;
import java.util.List;

public class Medecin extends Personne {

    private static final Logger logger = LoggerFactory.getLogger(Medecin.class);

    private String rPPS;
    private static List<Medecin> medecins = new ArrayList<>();

    public Medecin(String nom, String prenom, String adresse, String codePostal, String ville,
                   String telephone, String email, String rPPS) throws SaisieException {
        super(nom, prenom, adresse, codePostal, ville, telephone, email);
        this.setRPPS(rPPS);
        medecins.add(this);
    }

    public String getRPPS() {
        return this.rPPS;
    }

    public void setRPPS(String rPPS) throws SaisieException {
        if (!RegexValidator.validerRPPS(rPPS)) {
            SaisieException e = new SaisieException("RPPS non valide ! Merci de saisir 11 chiffres commencent par 10");
            LogUtils.error(logger, "Erreur RPPS pour le médecin " + getNom(), e);
            throw e;
        }
        this.rPPS = rPPS;
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

    public static List<Medecin> rechercherParRpps(String rpps) {
        List<Medecin> resultats = new ArrayList<>();
        for (Medecin m : medecins) {
            if (m.getRPPS() != null && m.getRPPS().equals(rpps.trim())) {
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

    @Override
    public String toString() {
        return super.toString() + "\nN° RPPS: " + this.rPPS;
    }
}
