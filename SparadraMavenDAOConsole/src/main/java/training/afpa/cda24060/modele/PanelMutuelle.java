package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.exception.SaisieException;

public class PanelMutuelle extends JPanel {

    private JTable tableMutuelle;
    private DefaultTableModel modelMutuelle;

    private JTextField txtNomMutuelle, txtAdresseMutuelle, txtCodePostalMutuelle;
    private JTextField txtVilleMutuelle, txtTelephoneMutuelle, txtEmailMutuelle;
    private JTextField txtDepartementMutuelle, txtTauxRbMutuelle;
    private JTextField txtRechercheDepartement;

    public PanelMutuelle() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSaisieMutuelle = creerPanelSaisie();
        JPanel panelRechercheMutuelle = creerPanelRecherche();

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieMutuelle);
        panelSuperior.add(panelRechercheMutuelle);

        creerTable();
        JScrollPane scrollMutuelle = new JScrollPane(tableMutuelle);
        scrollMutuelle.setBorder(new TitledBorder("Liste des Mutuelles"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollMutuelle, BorderLayout.CENTER);
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouvelle Mutuelle"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Colonne 1
        ajouterChamp(panel, "Nom :", txtNomMutuelle = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Adresse :", txtAdresseMutuelle = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panel, "Code Postal :", txtCodePostalMutuelle = new JTextField(15), 0, 2, gbc);

        // Colonne 2
        ajouterChamp(panel, "Ville :", txtVilleMutuelle = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panel, "Téléphone :", txtTelephoneMutuelle = new JTextField(15), 2, 1, gbc);
        ajouterChamp(panel, "Email :", txtEmailMutuelle = new JTextField(15), 2, 2, gbc);
        ajouterChamp(panel, "Département :", txtDepartementMutuelle = new JTextField(15), 2, 3, gbc);
        ajouterChamp(panel, "Taux Remboursement (%) :", txtTauxRbMutuelle = new JTextField(15), 2, 4, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterMutuelle());
        btnModifier.addActionListener(e -> modifierMutuelle());
        btnSupprimer.addActionListener(e -> supprimerMutuelle());
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 5;
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
        JTextField txtRechercheNom = new JTextField(15);
        panel.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherParNom(txtRechercheNom.getText()));
        panel.add(btnRechercheNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Par département:"), gbc);
        gbc.gridx = 1;
        txtRechercheDepartement = new JTextField(15);
        panel.add(txtRechercheDepartement, gbc);
        gbc.gridx = 2;
        JButton btnRechercheDepartement = new JButton("🔍");
        btnRechercheDepartement.addActionListener(e -> rechercherParDepartement());
        panel.add(btnRechercheDepartement, gbc);

        JButton btnAfficherTous = new JButton("Afficher toutes");
        btnAfficherTous.addActionListener(e -> chargerMutuelles());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"Nom", "Adresse", "Code Postal", "Ville", "Téléphone",
                "Email", "Département", "Taux Remb."};
        modelMutuelle = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMutuelle = new JTable(modelMutuelle);
        tableMutuelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMutuelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerMutuelleDansFormulaire();
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

    private void ajouterMutuelle() {
        try {
            String nom = txtNomMutuelle.getText().trim();
            String adresse = txtAdresseMutuelle.getText().trim();
            String codePostal = txtCodePostalMutuelle.getText().trim();
            String ville = txtVilleMutuelle.getText().trim();
            String telephone = txtTelephoneMutuelle.getText().trim();
            String email = txtEmailMutuelle.getText().trim();
            String departement = txtDepartementMutuelle.getText().trim();
            String tauxStr = txtTauxRbMutuelle.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() || ville.isEmpty() ||
                    telephone.isEmpty() || email.isEmpty() || departement.isEmpty() || tauxStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int taux = Integer.parseInt(tauxStr);
            new Mutuelle(nom, adresse, codePostal, ville, telephone, email, departement, taux);
            chargerMutuelles();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Mutuelle ajoutée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le taux de remboursement doit être un nombre entier!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierMutuelle() {
        int selectedRow = tableMutuelle.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mutuelle à modifier.");
            return;
        }

        try {
            Mutuelle mutuelle = Mutuelle.getMutuelles().get(selectedRow);
            mutuelle.setNom(txtNomMutuelle.getText());
            mutuelle.setAdresse(txtAdresseMutuelle.getText());
            mutuelle.setCodePostal(txtCodePostalMutuelle.getText());
            mutuelle.setVille(txtVilleMutuelle.getText());
            mutuelle.setTelephone(txtTelephoneMutuelle.getText());
            mutuelle.setEmail(txtEmailMutuelle.getText());
            mutuelle.setDepartement(txtDepartementMutuelle.getText());
            mutuelle.setTRemboursement(Integer.parseInt(txtTauxRbMutuelle.getText()));

            chargerMutuelles();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Mutuelle modifiée avec succès!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void supprimerMutuelle() {
        int selectedRow = tableMutuelle.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mutuelle à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?",
                "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Mutuelle.getMutuelles().remove(selectedRow);
            chargerMutuelles();
            viderChamps();
        }
    }

    public void chargerMutuelles() {
        modelMutuelle.setRowCount(0);
        for (Mutuelle mutuelle : Mutuelle.getMutuelles()) {
            modelMutuelle.addRow(new Object[]{
                    mutuelle.getNom(), mutuelle.getAdresse(), mutuelle.getCodePostal(),
                    mutuelle.getVille(), mutuelle.getTelephone(), mutuelle.getEmail(),
                    mutuelle.getDepartement(), mutuelle.getTRemboursement() + "%"
            });
        }
    }

    private void chargerMutuelleDansFormulaire() {
        int row = tableMutuelle.getSelectedRow();
        if (row != -1) {
            Mutuelle m = Mutuelle.getMutuelles().get(row);
            txtNomMutuelle.setText(m.getNom());
            txtAdresseMutuelle.setText(m.getAdresse());
            txtCodePostalMutuelle.setText(m.getCodePostal());
            txtVilleMutuelle.setText(m.getVille());
            txtTelephoneMutuelle.setText(m.getTelephone());
            txtEmailMutuelle.setText(m.getEmail());
            txtDepartementMutuelle.setText(m.getDepartement());
            txtTauxRbMutuelle.setText(String.valueOf(m.getTRemboursement()));
        }
    }

    private void rechercherParNom(String nom) {
        if (nom.trim().isEmpty()) {
            chargerMutuelles();
            return;
        }

        modelMutuelle.setRowCount(0);
        for (Mutuelle m : Mutuelle.getMutuelles()) {
            if (m.getNom().toLowerCase().contains(nom.toLowerCase())) {
                modelMutuelle.addRow(new Object[]{
                        m.getNom(), m.getAdresse(), m.getCodePostal(), m.getVille(),
                        m.getTelephone(), m.getEmail(), m.getDepartement(), m.getTRemboursement() + "%"
                });
            }
        }
    }

    private void rechercherParDepartement() {
        String departement = txtRechercheDepartement.getText().trim();
        if (departement.isEmpty()) {
            chargerMutuelles();
            return;
        }

        modelMutuelle.setRowCount(0);
        for (Mutuelle m : Mutuelle.getMutuelles()) {
            if (m.getDepartement().toLowerCase().contains(departement.toLowerCase())) {
                modelMutuelle.addRow(new Object[]{
                        m.getNom(), m.getAdresse(), m.getCodePostal(), m.getVille(),
                        m.getTelephone(), m.getEmail(), m.getDepartement(), m.getTRemboursement() + "%"
                });
            }
        }
    }

    private void viderChamps() {
        txtNomMutuelle.setText("");
        txtAdresseMutuelle.setText("");
        txtCodePostalMutuelle.setText("");
        txtVilleMutuelle.setText("");
        txtTelephoneMutuelle.setText("");
        txtEmailMutuelle.setText("");
        txtDepartementMutuelle.setText("");
        txtTauxRbMutuelle.setText("");
    }
}