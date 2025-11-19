package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.utilitaires.DateTimePaternFr;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ordonnance implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Ordonnance.class);

    private static List<Ordonnance> ordonnances = new ArrayList<>();

    private Medecin medecin;
    private Client client;
    private List<Prescription> prescriptions;
    private LocalDate dateOrdonnance;

    public Ordonnance(Medecin medecin, Client client, List<Prescription> prescriptions, LocalDate dateOrdonnance) {
        setMedecin(medecin);
        setClient(client);
        setPrescriptions(prescriptions);
        setDateOrdonnance(dateOrdonnance);
        ordonnances.add(this);
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        if (medecin == null) {
            LogUtils.error(logger, "Médecin nul dans Ordonnance", null);
        }
        this.medecin = medecin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (client == null) {
            LogUtils.error(logger, "Client nul dans Ordonnance", null);
        }
        this.client = client;
    }

    public List<Prescription> getPrescriptions() {
        return Collections.unmodifiableList(prescriptions);
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        if (prescriptions == null) {
            LogUtils.error(logger, "Liste de prescriptions nulle dans Ordonnance", null);
            this.prescriptions = new ArrayList<>();
        } else {
            this.prescriptions = new ArrayList<>(prescriptions);
        }
    }

    public LocalDate getDateOrdonnance() {
        return dateOrdonnance;
    }

    public void setDateOrdonnance(LocalDate dateOrdonnance) {
        if (dateOrdonnance == null) {
            LogUtils.error(logger, "Date de l'ordonnance nulle", null);
        }
        this.dateOrdonnance = dateOrdonnance;
    }

    public String getDateOrdonnanceFormatee() {
        return DateTimePaternFr.formatDate(dateOrdonnance, "dd/MM/yyyy");
    }

    public static List<Ordonnance> getOrdonnances() {
        return ordonnances;
    }

    public static void setOrdonnances(List<Ordonnance> ordonnances) {
        Ordonnance.ordonnances = ordonnances;
    }

    public static List<Ordonnance> rechercherOrdonnanceParClient(Client client) {
        List<Ordonnance> resultats = new ArrayList<>();
        for (Ordonnance o : ordonnances) {
            if (o.getClient().equals(client)) {
                resultats.add(o);
            }
        }
        return resultats;
    }

    public static List<Ordonnance> rechercherOrdonnanceParMedecin(Medecin medecin) {
        List<Ordonnance> resultats = new ArrayList<>();
        for (Ordonnance o : ordonnances) {
            if (o.getMedecin().equals(medecin)) {
                resultats.add(o);
            }
        }
        return resultats;
    }

    public static List<Ordonnance> rechercherOrdonnanceParDate(LocalDate date) {
        List<Ordonnance> resultats = new ArrayList<>();
        for (Ordonnance o : ordonnances) {
            if (o.getDateOrdonnance().equals(date)) {
                resultats.add(o);
            }
        }
        return resultats;
    }

    @Override
    public String toString() {
        StringBuilder prescriptionsStr = new StringBuilder();
        for (Prescription p : prescriptions) {
            prescriptionsStr.append("\n    - ").append(p);
        }

        return "\nOrdonnance"
                + "\nMédecin       : " + medecin
                + "\nClient        : " + client
                + "\nDate          : " + getDateOrdonnanceFormatee()
                + "\nPrescriptions : " + prescriptionsStr;
    }
}
