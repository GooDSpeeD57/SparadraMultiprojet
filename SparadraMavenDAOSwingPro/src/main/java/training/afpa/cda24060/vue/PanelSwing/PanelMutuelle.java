package training.afpa.cda24060.vue.PanelSwing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import training.afpa.cda24060.ClasseDAO.MutuelleDAO;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Mutuelle;

public class PanelMutuelle extends JPanel {

    private final MutuelleDAO mutuelleDAO = new MutuelleDAO();

    private JTable tableMutuelle;
    private DefaultTableModel modelMutuelle;

    private JTextField txtNomMutuelle, txtAdresseMutuelle, txtCodePostalMutuelle;
    private JTextField txtVilleMutuelle, txtTelephoneMutuelle, txtEmailMutuelle;
    private JTextField txtDepartementMutuelle, txtTauxRbMutuelle;
    private JTextField txtRechercheNom, txtRechercheDepartement;

    private int mutuelleIdSelectionnee = -1;

    public PanelMutuelle() throws SQLException {
        initComponents();
    }

    private void initComponents() throws SQLException {
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

        chargerMutuelles();
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouvelle Mutuelle"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        ajouterChamp(panel, "Nom :", txtNomMutuelle = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Adresse :", txtAdresseMutuelle = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panel, "Code Postal :", txtCodePostalMutuelle = new JTextField(15), 0, 2, gbc);

        ajouterChamp(panel, "Ville :", txtVilleMutuelle = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panel, "Téléphone :", txtTelephoneMutuelle = new JTextField(15), 2, 1, gbc);
        ajouterChamp(panel, "Email :", txtEmailMutuelle = new JTextField(15), 2, 2, gbc);
        ajouterChamp(panel, "Département :", txtDepartementMutuelle = new JTextField(15), 2, 3, gbc);
        ajouterChamp(panel, "Taux Remboursement (%) :", txtTauxRbMutuelle = new JTextField(15), 2, 4, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterMutuelle());
        btnModifier.addActionListener(e -> modifierMutuelle());
        btnSupprimer.addActionListener(e -> {
            try {
                supprimerMutuelle();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panel.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> {
            try {
                rechercherParNom();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(btnRechercheNom, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Par département:"), gbc);
        gbc.gridx = 1;
        txtRechercheDepartement = new JTextField(15);
        panel.add(txtRechercheDepartement, gbc);
        gbc.gridx = 2;
        JButton btnRechercheDep = new JButton("🔍");
        btnRechercheDep.addActionListener(e -> {
            try {
                rechercherParDepartement();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(btnRechercheDep, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JButton btnAfficherTous = new JButton("Afficher toutes");
        btnAfficherTous.addActionListener(e -> {
            try {
                chargerMutuelles();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"ID", "Nom", "Adresse", "Code Postal", "Ville", "Téléphone",
                "Email", "Département", "Taux Remb."};
        modelMutuelle = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMutuelle = new JTable(modelMutuelle);
        tableMutuelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMutuelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        chargerMutuelleDansFormulaire();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void ajouterChamp(JPanel panel, String label, JTextField field, int colLabel, int row, GridBagConstraints gbc) {
        gbc.gridx = colLabel; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = colLabel + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void ajouterMutuelle() {
        try {
            Mutuelle m = new Mutuelle();
            m.setNomMutuelle(txtNomMutuelle.getText().trim());
            m.setAdresseMutuelle(txtAdresseMutuelle.getText().trim());
            m.setCodePostalMutuelle(txtCodePostalMutuelle.getText().trim());
            m.setVilleMutuelle(txtVilleMutuelle.getText().trim());
            m.setTelephoneMutuelle(txtTelephoneMutuelle.getText().trim());
            m.setMailMutuelle(txtEmailMutuelle.getText().trim());
            m.setDepartementMutuelle(txtDepartementMutuelle.getText().trim());
            m.setTRemboursement(Double.parseDouble(txtTauxRbMutuelle.getText().trim()));

            if (mutuelleDAO.insert(m)) {
                chargerMutuelles();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Mutuelle ajoutée avec succès!");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Taux de remboursement doit être un nombre.");
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void modifierMutuelle() {
        if (mutuelleIdSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mutuelle à modifier.");
            return;
        }
        try {
            Mutuelle m = mutuelleDAO.findById(mutuelleIdSelectionnee);
            if (m != null) {
                m.setNomMutuelle(txtNomMutuelle.getText().trim());
                m.setAdresseMutuelle(txtAdresseMutuelle.getText().trim());
                m.setCodePostalMutuelle(txtCodePostalMutuelle.getText().trim());
                m.setVilleMutuelle(txtVilleMutuelle.getText().trim());
                m.setTelephoneMutuelle(txtTelephoneMutuelle.getText().trim());
                m.setMailMutuelle(txtEmailMutuelle.getText().trim());
                m.setDepartementMutuelle(txtDepartementMutuelle.getText().trim());
                m.setTRemboursement(Double.parseDouble(txtTauxRbMutuelle.getText().trim()));

                if (mutuelleDAO.update(m)) {
                    chargerMutuelles();
                    viderChamps();
                    mutuelleIdSelectionnee = -1;
                    JOptionPane.showMessageDialog(this, "Mutuelle modifiée avec succès!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerMutuelle() throws SQLException {
        if (mutuelleIdSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mutuelle à supprimer.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (mutuelleDAO.delete(mutuelleIdSelectionnee)) {
                chargerMutuelles();
                viderChamps();
                mutuelleIdSelectionnee = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    public void chargerMutuelles() throws SQLException {
        modelMutuelle.setRowCount(0);
        List<Mutuelle> mutuelles = mutuelleDAO.findAll();
        for (Mutuelle m : mutuelles) {
            modelMutuelle.addRow(new Object[]{
                    m.getIdMutuelle(),
                    m.getNomMutuelle(),
                    m.getAdresseMutuelle(),
                    m.getCodePostalMutuelle(),
                    m.getVilleMutuelle(),
                    m.getTelephoneMutuelle(),
                    m.getMailMutuelle(),
                    m.getDepartementMutuelle(),
                    m.getTRemboursement() + "%"
            });
        }
    }

    private void chargerMutuelleDansFormulaire() throws SQLException {
        int row = tableMutuelle.getSelectedRow();
        if (row != -1) {
            mutuelleIdSelectionnee = (int) modelMutuelle.getValueAt(row, 0);
            Mutuelle m = mutuelleDAO.findById(mutuelleIdSelectionnee);
            if (m != null) {
                txtNomMutuelle.setText(m.getNomMutuelle());
                txtAdresseMutuelle.setText(m.getAdresseMutuelle());
                txtCodePostalMutuelle.setText(m.getCodePostalMutuelle());
                txtVilleMutuelle.setText(m.getVilleMutuelle());
                txtTelephoneMutuelle.setText(m.getTelephoneMutuelle());
                txtEmailMutuelle.setText(m.getMailMutuelle());
                txtDepartementMutuelle.setText(m.getDepartementMutuelle());
                txtTauxRbMutuelle.setText(String.valueOf(m.getTRemboursement()));
            }
        }
    }

    private void rechercherParNom() throws SQLException {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) { chargerMutuelles(); return; }
        modelMutuelle.setRowCount(0);
        List<Mutuelle> mutuelles = mutuelleDAO.findByNom(nom);
        for (Mutuelle m : mutuelles) {
            modelMutuelle.addRow(new Object[]{
                    m.getIdMutuelle(),
                    m.getNomMutuelle(),
                    m.getAdresseMutuelle(),
                    m.getCodePostalMutuelle(),
                    m.getVilleMutuelle(),
                    m.getTelephoneMutuelle(),
                    m.getMailMutuelle(),
                    m.getDepartementMutuelle(),
                    m.getTRemboursement() + "%"
            });
        }
    }

    private void rechercherParDepartement() throws SQLException {
        String dep = txtRechercheDepartement.getText().trim();
        if (dep.isEmpty()) {
            chargerMutuelles();
            return;
        }

        modelMutuelle.setRowCount(0);

        List<Mutuelle> mutuelles = mutuelleDAO.findByDepartement(dep);

        for (Mutuelle m : mutuelles) {
            modelMutuelle.addRow(new Object[]{
                    m.getIdMutuelle(),
                    m.getNomMutuelle(),
                    m.getAdresseMutuelle(),
                    m.getCodePostalMutuelle(),
                    m.getVilleMutuelle(),
                    m.getTelephoneMutuelle(),
                    m.getMailMutuelle(),
                    m.getDepartementMutuelle(),
                    m.getTRemboursement() + "%"
            });
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
        mutuelleIdSelectionnee = -1;
    }
}
