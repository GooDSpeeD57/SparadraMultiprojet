package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import training.afpa.cda24060.modele.Medicament;
import training.afpa.cda24060.exception.SaisieException;

public class PanelMedicament extends JPanel {

    private JTable tableMedicament;
    private DefaultTableModel modelMedicament;

    private JTextField txtNomMedicament, txtCategoriMedicament, txtPrixMedicament;
    private JTextField txtDateMiseCirculation, txtQuantiteMedicament;
    private JComboBox<String> cbSansOrdonnanceMedicament;
    private JTextField txtRechercheNomMedicament, txtRechercheCategorieMedicament;

    public PanelMedicament() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSaisieMedicament = creerPanelSaisie();
        JPanel panelRechercheMedicament = creerPanelRecherche();

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieMedicament);
        panelSuperior.add(panelRechercheMedicament);

        creerTable();
        JScrollPane scrollMedicament = new JScrollPane(tableMedicament);
        scrollMedicament.setBorder(new TitledBorder("Liste des Médicaments"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollMedicament, BorderLayout.CENTER);
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouveau Médicament"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Colonne 1
        ajouterChamp(panel, "Nom :", txtNomMedicament = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Catégorie :", txtCategoriMedicament = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panel, "Prix (€) :", txtPrixMedicament = new JTextField(15), 0, 2, gbc);

        // Colonne 2
        ajouterChamp(panel, "Date mise en circulation :", txtDateMiseCirculation = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panel, "Quantité :", txtQuantiteMedicament = new JTextField(15), 2, 1, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Sans ordonnance :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] options = {"Oui", "Non"};
        cbSansOrdonnanceMedicament = new JComboBox<>(options);
        panel.add(cbSansOrdonnanceMedicament, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterMedicament());
        btnModifier.addActionListener(e -> modifierMedicament());
        btnSupprimer.addActionListener(e -> supprimerMedicament());
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panel.add(panelBoutons, gbc);

        return panel;
    }

    private JPanel creerPanelRecherche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNomMedicament = new JTextField(15);
        panel.add(txtRechercheNomMedicament, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherParNom());
        panel.add(btnRechercheNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Par catégorie:"), gbc);
        gbc.gridx = 1;
        txtRechercheCategorieMedicament = new JTextField(15);
        panel.add(txtRechercheCategorieMedicament, gbc);
        gbc.gridx = 2;
        JButton btnRechercheCategorie = new JButton("🔍");
        btnRechercheCategorie.addActionListener(e -> rechercherParCategorie());
        panel.add(btnRechercheCategorie, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerMedicaments());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"Nom", "Catégorie", "Prix", "Date Circulation", "Quantité", "Sans Ordonnance"};
        modelMedicament = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMedicament = new JTable(modelMedicament);
        tableMedicament.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMedicament.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerMedicamentDansFormulaire();
                }
            }
        });
    }

    private void ajouterChamp(JPanel panel, String label, JTextField field, int colLabel, int row, GridBagConstraints gbc) {
        gbc.gridx = colLabel;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = colLabel + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void ajouterMedicament() {
        try {
            String nom = txtNomMedicament.getText().trim();
            String categorie = txtCategoriMedicament.getText().trim();
            String prixStr = txtPrixMedicament.getText().trim();
            String dateMiseCirculation = txtDateMiseCirculation.getText().trim();
            String quantiteStr = txtQuantiteMedicament.getText().trim();
            String sansOrdonnance = (String) cbSansOrdonnanceMedicament.getSelectedItem();

            if (nom.isEmpty() || categorie.isEmpty() || prixStr.isEmpty() ||
                    dateMiseCirculation.isEmpty() || quantiteStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double prix = Double.parseDouble(prixStr);
            int quantite = Integer.parseInt(quantiteStr);

            new Medicament(nom, categorie, prix, dateMiseCirculation, quantite, sansOrdonnance);
            chargerMedicaments();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médicament ajouté avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prix et quantité doivent être des nombres valides!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierMedicament() {
        int selectedRow = tableMedicament.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médicament à modifier.");
            return;
        }

        try {
            Medicament medicament = Medicament.getMedicaments().get(selectedRow);
            medicament.setNomMedicament(txtNomMedicament.getText());
            medicament.setCategorieMedicament(txtCategoriMedicament.getText());
            medicament.setPrixMedicament(Double.parseDouble(txtPrixMedicament.getText()));
            medicament.setDateMiseEnCirculation(txtDateMiseCirculation.getText());
            medicament.setQuantiteMedicament(Integer.parseInt(txtQuantiteMedicament.getText()));
            medicament.setSansOrdonnanceMedicament((String) cbSansOrdonnanceMedicament.getSelectedItem());

            chargerMedicaments();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médicament modifié avec succès!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void supprimerMedicament() {
        int selectedRow = tableMedicament.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médicament à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?",
                "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Medicament.getMedicaments().remove(selectedRow);
            chargerMedicaments();
            viderChamps();
        }
    }

    public void chargerMedicaments() {
        modelMedicament.setRowCount(0);
        for (Medicament medicament : Medicament.getMedicaments()) {
            modelMedicament.addRow(new Object[]{
                    medicament.getNomMedicament(),
                    medicament.getCategorieMedicament(),
                    medicament.getPrixMedicament() + " €",
                    medicament.getDateMiseEnCirculation(),
                    medicament.getQuantiteMedicament(),
                    medicament.getSansOrdonnanceMedicament()
            });
        }
    }

    private void chargerMedicamentDansFormulaire() {
        int row = tableMedicament.getSelectedRow();
        if (row != -1) {
            Medicament med = Medicament.getMedicaments().get(row);
            txtNomMedicament.setText(med.getNomMedicament());
            txtCategoriMedicament.setText(med.getCategorieMedicament());
            txtPrixMedicament.setText(String.valueOf(med.getPrixMedicament()));
            txtDateMiseCirculation.setText(med.getDateMiseEnCirculation());
            txtQuantiteMedicament.setText(String.valueOf(med.getQuantiteMedicament()));
            cbSansOrdonnanceMedicament.setSelectedItem(med.getSansOrdonnanceMedicament());
        }
    }

    private void rechercherParNom() {
        String nom = txtRechercheNomMedicament.getText().trim();
        if (nom.isEmpty()) {
            chargerMedicaments();
            return;
        }

        modelMedicament.setRowCount(0);
        for (Medicament m : Medicament.getMedicaments()) {
            if (m.getNomMedicament().toLowerCase().contains(nom.toLowerCase())) {
                modelMedicament.addRow(new Object[]{
                        m.getNomMedicament(), m.getCategorieMedicament(), m.getPrixMedicament() + " €",
                        m.getDateMiseEnCirculation(), m.getQuantiteMedicament(), m.getSansOrdonnanceMedicament()
                });
            }
        }
    }

    private void rechercherParCategorie() {
        String categorie = txtRechercheCategorieMedicament.getText().trim();
        if (categorie.isEmpty()) {
            chargerMedicaments();
            return;
        }

        modelMedicament.setRowCount(0);
        for (Medicament m : Medicament.getMedicaments()) {
            if (m.getCategorieMedicament().toLowerCase().contains(categorie.toLowerCase())) {
                modelMedicament.addRow(new Object[]{
                        m.getNomMedicament(), m.getCategorieMedicament(), m.getPrixMedicament() + " €",
                        m.getDateMiseEnCirculation(), m.getQuantiteMedicament(), m.getSansOrdonnanceMedicament()
                });
            }
        }
    }

    private void viderChamps() {
        txtNomMedicament.setText("");
        txtCategoriMedicament.setText("");
        txtPrixMedicament.setText("");
        txtDateMiseCirculation.setText("");
        txtQuantiteMedicament.setText("");
        cbSansOrdonnanceMedicament.setSelectedIndex(0);
    }
}