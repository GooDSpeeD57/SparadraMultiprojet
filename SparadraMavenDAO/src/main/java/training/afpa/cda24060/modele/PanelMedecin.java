package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.exception.SaisieException;

public class PanelMedecin extends JPanel {

    private JTable tableMedecin;
    private DefaultTableModel modelMedecin;

    private JTextField txtNomMedecin, txtPrenomMedecin, txtAdresseMedecin;
    private JTextField txtCodePostalMedecin, txtVilleMedecin, txtTelephoneMedecin;
    private JTextField txtEmailMedecin, txtRPPSMedecin;
    private JTextField txtRechercheRpps;

    public PanelMedecin() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSaisieMedecin = creerPanelSaisie();
        JPanel panelRechercheMedecin = creerPanelRecherche();

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieMedecin);
        panelSuperior.add(panelRechercheMedecin);

        creerTable();
        JScrollPane scrollMedecin = new JScrollPane(tableMedecin);
        scrollMedecin.setBorder(new TitledBorder("Liste des Médecins"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollMedecin, BorderLayout.CENTER);
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouveau Médecin"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Colonne 1
        ajouterChamp(panel, "Nom :", txtNomMedecin = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Prénom :", txtPrenomMedecin = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panel, "Adresse :", txtAdresseMedecin = new JTextField(15), 0, 2, gbc);
        ajouterChamp(panel, "Code Postal :", txtCodePostalMedecin = new JTextField(15), 0, 3, gbc);

        // Colonne 2
        ajouterChamp(panel, "Ville :", txtVilleMedecin = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panel, "Téléphone :", txtTelephoneMedecin = new JTextField(15), 2, 1, gbc);
        ajouterChamp(panel, "Email :", txtEmailMedecin = new JTextField(15), 2, 2, gbc);
        ajouterChamp(panel, "N° RPPS :", txtRPPSMedecin = new JTextField(15), 2, 3, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterMedecin());
        btnModifier.addActionListener(e -> modifierMedecin());
        btnSupprimer.addActionListener(e -> supprimerMedecin());
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 4;
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
        panel.add(new JLabel("Par RPPS:"), gbc);
        gbc.gridx = 1;
        txtRechercheRpps = new JTextField(15);
        panel.add(txtRechercheRpps, gbc);
        gbc.gridx = 2;
        JButton btnRechercheRpps = new JButton("🔍");
        btnRechercheRpps.addActionListener(e -> rechercherParRpps());
        panel.add(btnRechercheRpps, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerMedecins());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"Nom", "Prénom", "Adresse", "Code Postal", "Ville",
                "Téléphone", "Email", "RPPS"};
        modelMedecin = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMedecin = new JTable(modelMedecin);
        tableMedecin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMedecin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerMedecinDansFormulaire();
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

    private void ajouterMedecin() {
        try {
            String nom = txtNomMedecin.getText().trim();
            String prenom = txtPrenomMedecin.getText().trim();
            String adresse = txtAdresseMedecin.getText().trim();
            String codePostal = txtCodePostalMedecin.getText().trim();
            String ville = txtVilleMedecin.getText().trim();
            String telephone = txtTelephoneMedecin.getText().trim();
            String email = txtEmailMedecin.getText().trim();
            String rPPS = txtRPPSMedecin.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() ||
                    ville.isEmpty() || telephone.isEmpty() || email.isEmpty() || rPPS.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new Medecin(nom, prenom, adresse, codePostal, ville, telephone, email, rPPS);
            chargerMedecins();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médecin ajouté avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierMedecin() {
        int selectedRow = tableMedecin.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à modifier.");
            return;
        }

        try {
            Medecin medecin = Medecin.getMedecins().get(selectedRow);
            medecin.setNom(txtNomMedecin.getText());
            medecin.setPrenom(txtPrenomMedecin.getText());
            medecin.setAdresse(txtAdresseMedecin.getText());
            medecin.setCodePostal(txtCodePostalMedecin.getText());
            medecin.setVille(txtVilleMedecin.getText());
            medecin.setTelephone(txtTelephoneMedecin.getText());
            medecin.setEmail(txtEmailMedecin.getText());
            medecin.setRPPS(txtRPPSMedecin.getText());

            chargerMedecins();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médecin modifié avec succès!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void supprimerMedecin() {
        int selectedRow = tableMedecin.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?",
                "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Medecin.getMedecins().remove(selectedRow);
            chargerMedecins();
            viderChamps();
        }
    }

    public void chargerMedecins() {
        modelMedecin.setRowCount(0);
        for (Medecin medecin : Medecin.getMedecins()) {
            modelMedecin.addRow(new Object[]{
                    medecin.getNom(), medecin.getPrenom(), medecin.getAdresse(),
                    medecin.getCodePostal(), medecin.getVille(), medecin.getTelephone(),
                    medecin.getEmail(), medecin.getRPPS()
            });
        }
    }

    private void chargerMedecinDansFormulaire() {
        int row = tableMedecin.getSelectedRow();
        if (row != -1) {
            Medecin m = Medecin.getMedecins().get(row);
            txtNomMedecin.setText(m.getNom());
            txtPrenomMedecin.setText(m.getPrenom());
            txtAdresseMedecin.setText(m.getAdresse());
            txtCodePostalMedecin.setText(m.getCodePostal());
            txtVilleMedecin.setText(m.getVille());
            txtTelephoneMedecin.setText(m.getTelephone());
            txtEmailMedecin.setText(m.getEmail());
            txtRPPSMedecin.setText(m.getRPPS());
        }
    }

    private void rechercherParNom(String nom) {
        if (nom.trim().isEmpty()) {
            chargerMedecins();
            return;
        }

        modelMedecin.setRowCount(0);
        List<Medecin> resultats = Medecin.rechercherParNom(nom);
        for (Medecin m : resultats) {
            modelMedecin.addRow(new Object[]{
                    m.getNom(), m.getPrenom(), m.getAdresse(), m.getCodePostal(),
                    m.getVille(), m.getTelephone(), m.getEmail(), m.getRPPS()
            });
        }
    }

    private void rechercherParRpps() {
        String rpps = txtRechercheRpps.getText().trim();
        if (rpps.isEmpty()) {
            chargerMedecins();
            return;
        }

        modelMedecin.setRowCount(0);
        List<Medecin> resultats = Medecin.rechercherParRpps(rpps);
        for (Medecin m : resultats) {
            modelMedecin.addRow(new Object[]{
                    m.getNom(), m.getPrenom(), m.getAdresse(), m.getCodePostal(),
                    m.getVille(), m.getTelephone(), m.getEmail(), m.getRPPS()
            });
        }
    }

    private void viderChamps() {
        txtNomMedecin.setText("");
        txtPrenomMedecin.setText("");
        txtAdresseMedecin.setText("");
        txtCodePostalMedecin.setText("");
        txtVilleMedecin.setText("");
        txtTelephoneMedecin.setText("");
        txtEmailMedecin.setText("");
        txtRPPSMedecin.setText("");
    }
}