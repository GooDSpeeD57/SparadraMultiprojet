package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import training.afpa.cda24060.ClasseDAO.MedecinDAO;
import training.afpa.cda24060.exception.SaisieException;

public class PanelMedecin extends JPanel {

    private final MedecinDAO medecinDAO = new MedecinDAO();

    private JTable tableMedecin;
    private DefaultTableModel modelMedecin;

    private JTextField txtNomMedecin, txtPrenomMedecin, txtAdresseMedecin;
    private JTextField txtCodePostalMedecin, txtVilleMedecin, txtTelephoneMedecin;
    private JTextField txtEmailMedecin, txtRPPSMedecin;
    private JTextField txtRechercheNom, txtRechercheRPPS;

    private int medecinIdSelectionne = -1;

    public PanelMedecin() {
        initComponents();
        chargerMedecins();
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
        panel.add(new JLabel("Par nom :"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panel.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherParNom(txtRechercheNom.getText().trim()));
        panel.add(btnRechercheNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Par RPPS :"), gbc);
        gbc.gridx = 1;
        txtRechercheRPPS = new JTextField(15);
        panel.add(txtRechercheRPPS, gbc);
        gbc.gridx = 2;
        JButton btnRechercheRPPS = new JButton("🔍");
        btnRechercheRPPS.addActionListener(e -> rechercherParRPPS(txtRechercheRPPS.getText().trim()));
        panel.add(btnRechercheRPPS, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerMedecins());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"ID", "Nom", "Prénom", "Adresse", "Code Postal", "Ville",
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
            Medecin medecin = new Medecin();
            medecin.setNom(txtNomMedecin.getText().trim());
            medecin.setPrenom(txtPrenomMedecin.getText().trim());
            medecin.setAdresse(txtAdresseMedecin.getText().trim());
            medecin.setCodePostal(txtCodePostalMedecin.getText().trim());
            medecin.setVille(txtVilleMedecin.getText().trim());
            medecin.setTelephone(txtTelephoneMedecin.getText().trim());
            medecin.setEmail(txtEmailMedecin.getText().trim());
            medecin.setRPPS(txtRPPSMedecin.getText().trim());

            if (medecinDAO.insert(medecin)) {
                chargerMedecins();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Médecin ajouté avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du médecin.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierMedecin() {
        if (medecinIdSelectionne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à modifier.");
            return;
        }

        try {
            Medecin medecin = medecinDAO.findById(medecinIdSelectionne);
            if (medecin != null) {
                medecin.setNom(txtNomMedecin.getText().trim());
                medecin.setPrenom(txtPrenomMedecin.getText().trim());
                medecin.setAdresse(txtAdresseMedecin.getText().trim());
                medecin.setCodePostal(txtCodePostalMedecin.getText().trim());
                medecin.setVille(txtVilleMedecin.getText().trim());
                medecin.setTelephone(txtTelephoneMedecin.getText().trim());
                medecin.setEmail(txtEmailMedecin.getText().trim());
                medecin.setRPPS(txtRPPSMedecin.getText().trim());

                if (medecinDAO.update(medecin)) {
                    chargerMedecins();
                    viderChamps();
                    medecinIdSelectionne = -1;
                    JOptionPane.showMessageDialog(this, "Médecin modifié avec succès!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void supprimerMedecin() {
        if (medecinIdSelectionne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?",
                "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (medecinDAO.delete(medecinIdSelectionne)) {
                chargerMedecins();
                viderChamps();
                medecinIdSelectionne = -1;
                JOptionPane.showMessageDialog(this, "Médecin supprimé avec succès!");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    public void chargerMedecins() {
        modelMedecin.setRowCount(0);
        List<Medecin> medecins = medecinDAO.findAll();
        for (Medecin medecin : medecins) {
            modelMedecin.addRow(new Object[]{
                    medecin.getIdMedecin(),
                    medecin.getNom(),
                    medecin.getPrenom(),
                    medecin.getAdresse(),
                    medecin.getCodePostal(),
                    medecin.getVille(),
                    medecin.getTelephone(),
                    medecin.getEmail(),
                    medecin.getRPPS()
            });
        }
    }

    private void chargerMedecinDansFormulaire() {
        int row = tableMedecin.getSelectedRow();
        if (row != -1) {
            medecinIdSelectionne = (int) modelMedecin.getValueAt(row, 0);
            Medecin m = medecinDAO.findById(medecinIdSelectionne);
            if (m != null) {
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
    }

    private void rechercherParNom(String nom) {
        if (nom.isEmpty()) {
            chargerMedecins();
            return;
        }
        modelMedecin.setRowCount(0);
        List<Medecin> resultats = medecinDAO.findByNom(nom);
        for (Medecin m : resultats) {
            modelMedecin.addRow(new Object[]{
                    m.getIdMedecin(),
                    m.getNom(),
                    m.getPrenom(),
                    m.getAdresse(),
                    m.getCodePostal(),
                    m.getVille(),
                    m.getTelephone(),
                    m.getEmail(),
                    m.getRPPS()
            });
        }
    }

    private void rechercherParRPPS(String rpps) {
        if (rpps.isEmpty()) {
            chargerMedecins();
            return;
        }
        modelMedecin.setRowCount(0);
        List<Medecin> resultats = medecinDAO.findByRPPS(rpps);
        for (Medecin m : resultats) {
            modelMedecin.addRow(new Object[]{
                    m.getIdMedecin(),
                    m.getNom(),
                    m.getPrenom(),
                    m.getAdresse(),
                    m.getCodePostal(),
                    m.getVille(),
                    m.getTelephone(),
                    m.getEmail(),
                    m.getRPPS()
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
        medecinIdSelectionne = -1;
    }
}
