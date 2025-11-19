package modele;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Facture implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final List<Facture> factures = new ArrayList<>();


    private Client client;
    private List<Prescription> prescriptions;
    private LocalDate dateFacture;
    private double montantTotal;


    public Facture( Client client, List<Prescription> prescriptions, LocalDate dateFacture) {
        this.client = client;
        this.prescriptions = new ArrayList<>(prescriptions);
        this.dateFacture = dateFacture;
        this.montantTotal = calculerMontantTotal();
                factures.add(this);
    }

    public Client getClient() {
        return client; }

    public void setClient(Client client) {
        this.client = client; }

    public List<Prescription> getPrescriptions() {
        return prescriptions; }

    public LocalDate getDateFacture() {
        return dateFacture; }

    public double getMontantTotal() {
        return montantTotal; }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = new ArrayList<>(prescriptions);
        this.montantTotal = calculerMontantTotal(); // Recalculer le total
    }


    public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }
    private double calculerMontantTotal() {
        double total = 0;
        for (Prescription p : prescriptions) {
            total += p.getPrixUnitaire() * p.getQuantitePrescrite();
        }
        return total;
    }

        public static List<Facture> getFactures() {
        return factures;
    }

}
