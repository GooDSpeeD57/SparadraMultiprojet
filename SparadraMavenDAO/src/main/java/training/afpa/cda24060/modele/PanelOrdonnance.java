package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import training.afpa.cda24060.modele.*;

public class PanelOrdonnance extends JPanel {

    private JTable tableOrdonnance;
    private DefaultTableModel modelOrdonnance;
    private JFrame parentFrame;

    public PanelOrdonnance(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel haut (boutons)
        JPanel panelCreationOrdonnance = new JPanel(new FlowLayout());
        panelCreationOrdonnance.setBorder(new TitledBorder("➕ Nouvelle Ordonnance"));

        JButton btnNouvelleOrdo = new JButton("\uD83D\uDCCB Créer une ordonnance");
        btnNouvelleOrdo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnNouvelleOrdo.addActionListener(e -> ouvrirDialogueCreationOrdonnance());

        JButton btnActualiserOrdo = new JButton("🔄 Actualiser");
        btnActualiserOrdo.addActionListener(e -> chargerOrdonnances());

        JButton btnVoirMedicaments = new JButton("📦 Voir les médicaments");
        btnVoirMedicaments.addActionListener(e -> voirMedicamentsDeLOrdonnance());

        panelCreationOrdonnance.add(btnNouvelleOrdo);
        panelCreationOrdonnance.add(btnActualiserOrdo);
        panelCreationOrdonnance.add(btnVoirMedicaments);

        // Table des ordonnances
        String[] colonnesOrdo = {"Client", "Médecin", "Date", "Nb Médicaments"};
        modelOrdonnance = new DefaultTableModel(colonnesOrdo, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableOrdonnance = new JTable(modelOrdonnance);
        tableOrdonnance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollOrdonnance = new JScrollPane(tableOrdonnance);
        scrollOrdonnance.setBorder(new TitledBorder("\uD83D\uDCCB Liste des Ordonnances"));

        add(panelCreationOrdonnance, BorderLayout.NORTH);
        add(scrollOrdonnance, BorderLayout.CENTER);
    }

    public void chargerOrdonnances() {
        modelOrdonnance.setRowCount(0);
        for (Ordonnance ordonnance : Ordonnance.getOrdonnances()) {
            modelOrdonnance.addRow(new Object[]{
                    ordonnance.getClient().getNom() + " " + ordonnance.getClient().getPrenom(),
                    ordonnance.getMedecin().getNom() + " " + ordonnance.getMedecin().getPrenom(),
                    ordonnance.getDateOrdonnanceFormatee(),
                    ordonnance.getPrescriptions().size()
            });
        }
    }

    private void voirMedicamentsDeLOrdonnance() {
        int selectedRow = tableOrdonnance.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ordonnance.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Ordonnance ordonnance = Ordonnance.getOrdonnances().get(selectedRow);
        afficherMedicamentsDeLOrdonnance(ordonnance);
    }

    private void afficherMedicamentsDeLOrdonnance(Ordonnance ordonnance) {
        JDialog dialog = new JDialog(parentFrame, "📦 Médicaments de l'ordonnance", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parentFrame);

        String[] colonnes = {"Nom", "Quantité", "Prix Unitaire (€)", "Prix Total (€)"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        for (Prescription p : ordonnance.getPrescriptions()) {
            model.addRow(new Object[]{
                    p.getNomMedicament(),
                    p.getQuantitePrescrite(),
                    String.format("%.2f", p.getPrixUnitaire()),
                    String.format("%.2f", p.getPrixTotal())
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private void ouvrirDialogueCreationOrdonnance() {
        JDialog dialogOrdonnance = new JDialog(parentFrame, "📋 Créer une Ordonnance", true);
        dialogOrdonnance.setSize(1200, 800);
        dialogOrdonnance.setLocationRelativeTo(parentFrame);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        final Client[] clientSelectionne = {null};
        final Medecin[] medecinSelectionne = {null};

        // Panel Client
        JPanel panelClient = new JPanel(new BorderLayout());
        panelClient.setBorder(new TitledBorder("👤 Sélection du Client"));
        String[] colonnesClientsOrdo = {"Nom", "Prénom", "NSS"};
        DefaultTableModel modelClientsOrdo = new DefaultTableModel(colonnesClientsOrdo, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableClientsOrdo = new JTable(modelClientsOrdo);
        tableClientsOrdo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Client client : Client.getClients()) {
            modelClientsOrdo.addRow(new Object[]{ client.getNom(), client.getPrenom(), client.getNss() });
        }
        JScrollPane scrollClientsOrdo = new JScrollPane(tableClientsOrdo);
        scrollClientsOrdo.setPreferredSize(new Dimension(650, 150));
        panelClient.add(scrollClientsOrdo, BorderLayout.CENTER);

        // Panel Medecin
        JPanel panelMedecin = new JPanel(new BorderLayout());
        panelMedecin.setBorder(new TitledBorder("👨‍⚕️ Sélection du Médecin"));
        String[] colonnesMedecinsOrdo = {"Nom", "Prénom", "RPPS"};
        DefaultTableModel modelMedecinsOrdo = new DefaultTableModel(colonnesMedecinsOrdo, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableMedecinsOrdo = new JTable(modelMedecinsOrdo);
        tableMedecinsOrdo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Medecin medecin : Medecin.getMedecins()) {
            modelMedecinsOrdo.addRow(new Object[]{ medecin.getNom(), medecin.getPrenom(), medecin.getRPPS() });
        }
        JScrollPane scrollMedecinsOrdo = new JScrollPane(tableMedecinsOrdo);
        scrollMedecinsOrdo.setPreferredSize(new Dimension(650, 150));
        panelMedecin.add(scrollMedecinsOrdo, BorderLayout.CENTER);

        // Panel Medicament
        JPanel panelMedicament = new JPanel(new BorderLayout());
        panelMedicament.setBorder(new TitledBorder("📦 Sélection du Médicament"));
        String[] colonnesMedicamentsOrdo = {"Nom", "Prix", "Quantité Disponible"};
        DefaultTableModel modelMedicamentsOrdo = new DefaultTableModel(colonnesMedicamentsOrdo, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableMedicamentsOrdo = new JTable(modelMedicamentsOrdo);
        tableMedicamentsOrdo.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        for (Medicament medicament : Medicament.getMedicaments()) {
            modelMedicamentsOrdo.addRow(new Object[]{
                    medicament.getNomMedicament(),
                    medicament.getPrixMedicament(),
                    medicament.getQuantiteMedicament()
            });
        }
        JScrollPane scrollMedicamentsOrdo = new JScrollPane(tableMedicamentsOrdo);
        scrollMedicamentsOrdo.setPreferredSize(new Dimension(650, 150));
        panelMedicament.add(scrollMedicamentsOrdo, BorderLayout.CENTER);

        JPanel panelTables = new JPanel();
        panelTables.setLayout(new BoxLayout(panelTables, BoxLayout.Y_AXIS));
        panelTables.add(panelClient);
        panelTables.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTables.add(panelMedecin);
        panelTables.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTables.add(panelMedicament);

        // Boutons
        JPanel panelBoutonsOrdo = new JPanel(new FlowLayout());
        JButton btnCreerOrdo = new JButton("✅ Créer l'Ordonnance");
        JButton btnAnnulerOrdo = new JButton("❌ Annuler");

        btnCreerOrdo.addActionListener(e -> {
            creerOrdonnance(dialogOrdonnance, tableClientsOrdo, tableMedecinsOrdo,
                    tableMedicamentsOrdo, modelClientsOrdo, modelMedecinsOrdo,
                    modelMedicamentsOrdo, clientSelectionne, medecinSelectionne);
        });

        btnAnnulerOrdo.addActionListener(e -> dialogOrdonnance.dispose());

        panelBoutonsOrdo.add(btnCreerOrdo);
        panelBoutonsOrdo.add(btnAnnulerOrdo);

        panelPrincipal.add(panelTables, BorderLayout.CENTER);
        panelPrincipal.add(panelBoutonsOrdo, BorderLayout.SOUTH);

        dialogOrdonnance.add(panelPrincipal);
        dialogOrdonnance.setVisible(true);
    }

    private void creerOrdonnance(JDialog dialog, JTable tableClients, JTable tableMedecins,
                                 JTable tableMedicaments, DefaultTableModel modelClients,
                                 DefaultTableModel modelMedecins, DefaultTableModel modelMedicaments,
                                 Client[] clientSelectionne, Medecin[] medecinSelectionne) {

        // Sélection du client
        int ligneClient = tableClients.getSelectedRow();
        if (ligneClient == -1) {
            JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner un client!",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nssSelectionne = (String) modelClients.getValueAt(ligneClient, 2);
        for (Client client : Client.getClients()) {
            if (client.getNss().equals(nssSelectionne)) {
                clientSelectionne[0] = client;
                break;
            }
        }

        // Sélection du médecin
        int ligneMedecin = tableMedecins.getSelectedRow();
        if (ligneMedecin == -1) {
            JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner un médecin!",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String rppsSelectionne = (String) modelMedecins.getValueAt(ligneMedecin, 2);
        for (Medecin medecin : Medecin.getMedecins()) {
            if (medecin.getRPPS().equals(rppsSelectionne)) {
                medecinSelectionne[0] = medecin;
                break;
            }
        }

        // Sélection des médicaments
        int[] lignesMedicaments = tableMedicaments.getSelectedRows();
        if (lignesMedicaments.length == 0) {
            JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner au moins un médicament!",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Prescription> prescriptions = new ArrayList<>();

        for (int ligne : lignesMedicaments) {
            String nomMedicament = (String) modelMedicaments.getValueAt(ligne, 0);
            Medicament medicamentChoisi = null;
            for (Medicament m : Medicament.getMedicaments()) {
                if (m.getNomMedicament().equals(nomMedicament)) {
                    medicamentChoisi = m;
                    break;
                }
            }
            if (medicamentChoisi == null) continue;

            int quantitePrescrite = demanderQuantite(dialog, medicamentChoisi);
            if (quantitePrescrite == -1) return; // Annulation

            prescriptions.add(new Prescription(medicamentChoisi, quantitePrescrite));
        }

        LocalDate dateOrdonnance = LocalDate.now();
        try {
            new Ordonnance(medecinSelectionne[0], clientSelectionne[0], prescriptions, dateOrdonnance);
            chargerOrdonnances();
            dialog.dispose();

            StringBuilder medicamentList = new StringBuilder();
            for (Prescription p : prescriptions) {
                medicamentList.append("• ").append(p.getNomMedicament())
                        .append(" x").append(p.getQuantitePrescrite()).append("\n");
            }

            JOptionPane.showMessageDialog(parentFrame,
                    "Ordonnance créée avec succès!\n" +
                            "👤 Client: " + clientSelectionne[0].getNom() + " " + clientSelectionne[0].getPrenom() + "\n" +
                            "👨‍⚕️ Médecin: " + medecinSelectionne[0].getNom() + " " + medecinSelectionne[0].getPrenom() + "\n" +
                            "📦 Médicaments:\n" + medicamentList.toString(),
                    "✅ Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de la création de l'ordonnance: " + ex.getMessage(),
                    "❌ Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int demanderQuantite(JDialog parent, Medicament medicament) {
        while (true) {
            String input = JOptionPane.showInputDialog(parent,
                    "Quantité pour le médicament \"" + medicament.getNomMedicament() +
                            "\" (max " + medicament.getQuantiteMedicament() + ") :",
                    "Quantité Médicament",
                    JOptionPane.QUESTION_MESSAGE);

            if (input == null) return -1; // Annulation

            try {
                int quantite = Integer.parseInt(input);
                if (quantite <= 0) {
                    JOptionPane.showMessageDialog(parent, "La quantité doit être supérieure à 0.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if (quantite > medicament.getQuantiteMedicament()) {
                    JOptionPane.showMessageDialog(parent, "Quantité supérieure au stock disponible.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    return quantite;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Veuillez entrer un nombre entier valide.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}