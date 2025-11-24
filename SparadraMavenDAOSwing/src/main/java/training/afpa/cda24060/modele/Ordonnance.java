package training.afpa.cda24060.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.utilitaires.DateTimePaternFr;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ordonnance {

    private static final Logger logger = LoggerFactory.getLogger(Ordonnance.class);

    private int id_Ordonnance;  // <-- clé primaire
    private Medecin medecin;
    private Client client;
    private List<Prescription> prescriptions;
    private LocalDate dateOrdonnance;

    public Ordonnance() {
        prescriptions = new ArrayList<>();
    }

    public Ordonnance(Medecin medecin, Client client, List<Prescription> prescriptions, LocalDate dateOrdonnance) {
        this();
        setMedecin(medecin);
        setClient(client);
        setPrescriptions(prescriptions);
        setDateOrdonnance(dateOrdonnance);
    }

    public int getId_Ordonnance() {
        return id_Ordonnance;
    }

    public void setId_Ordonnance(int id_Ordonnance) {
        this.id_Ordonnance = id_Ordonnance;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        if (medecin == null) {
            LogUtils.error(logger, "Médecin nul dans Ordonnance");
        }
        this.medecin = medecin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (client == null) {
            LogUtils.error(logger, "Client nul dans Ordonnance");
        }
        this.client = client;
    }

    public List<Prescription> getPrescriptions() {
        return Collections.unmodifiableList(prescriptions);
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        if (prescriptions == null) {
            LogUtils.error(logger, "Liste de prescriptions nulle dans Ordonnance");
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
            LogUtils.error(logger, "Date de l'ordonnance nulle");
        }
        this.dateOrdonnance = dateOrdonnance;
    }

    public String getDateOrdonnanceFormatee() {
        return DateTimePaternFr.formatDate(dateOrdonnance, "dd/MM/yyyy");
    }

    // ===================== toString =====================
    @Override
    public String toString() {
        StringBuilder prescriptionsStr = new StringBuilder();
        for (Prescription p : prescriptions) {
            prescriptionsStr.append("\n    - ").append(p);
        }

        return "\nOrdonnance"
                + "\nID            : " + id_Ordonnance
                + "\nMédecin       : " + medecin
                + "\nClient        : " + client
                + "\nDate          : " + getDateOrdonnanceFormatee()
                + "\nPrescriptions : " + prescriptionsStr;
    }
}
