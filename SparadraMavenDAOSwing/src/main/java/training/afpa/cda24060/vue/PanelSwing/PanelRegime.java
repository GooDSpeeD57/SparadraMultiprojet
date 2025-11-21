package training.afpa.cda24060.vue.PanelSwing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import training.afpa.cda24060.ClasseDAO.RegimeDAO;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Regime;

public class PanelRegime extends JPanel {

    private final RegimeDAO regimeDAO = new RegimeDAO();

    private JTable tableRegime;
    private DefaultTableModel modelRegime;

    private JTextField txtNomRegime, txtTauxRemboursement;
    private JTextField txtRechercheNom;

    private int regimeIdSelectionne = -1;

    public PanelRegime() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSaisieRegime = creerPanelSaisie();
        JPanel panelRechercheRegime = creerPanelRecherche();

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieRegime);
        panelSuperior.add(panelRechercheRegime);

        creerTable();
        JScrollPane scrollRegime = new JScrollPane(tableRegime);
        scrollRegime.setBorder(new TitledBorder("Liste des Régimes"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollRegime, BorderLayout.CENTER);

        chargerRegimes();
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouveau Régime"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        ajouterChamp(panel, "Nom :", txtNomRegime = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Taux remboursement :", txtTauxRemboursement = new JTextField(15), 0, 1, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterRegime());
        btnModifier.addActionListener(e -> modifierRegime());
        btnSupprimer.addActionListener(e -> supprimerRegime());
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(panelBoutons, gbc);

        return panel;
    }

    private JPanel creerPanelRecherche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panel.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherParNom());
        panel.add(btnRechercheNom, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerRegimes());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"ID", "Nom", "Taux remboursement"};
        modelRegime = new DefaultTableModel(colonnes,0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRegime = new JTable(modelRegime);
        tableRegime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRegime.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerRegimeDansFormulaire();
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

        gbc.gridx = colLabel+1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void ajouterRegime() {
        try {
            Regime r = new Regime();
            r.setNomRegime(txtNomRegime.getText().trim());
            r.setTauxRemboursement(Double.parseDouble(txtTauxRemboursement.getText().trim()));

            if (regimeDAO.insert(r)) {
                chargerRegimes();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Régime ajouté avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du régime.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Taux remboursement invalide.");
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void modifierRegime() {
        if (regimeIdSelectionne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un régime à modifier.");
            return;
        }
        try {
            Regime r = regimeDAO.findById(regimeIdSelectionne);
            if (r != null) {
                r.setNomRegime(txtNomRegime.getText().trim());
                r.setTauxRemboursement(Double.parseDouble(txtTauxRemboursement.getText().trim()));

                if (regimeDAO.update(r)) {
                    chargerRegimes();
                    viderChamps();
                    regimeIdSelectionne = -1;
                    JOptionPane.showMessageDialog(this, "Régime modifié avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Taux remboursement invalide.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerRegime() {
        if (regimeIdSelectionne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un régime à supprimer.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?",
                "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (regimeDAO.delete(regimeIdSelectionne)) {
                chargerRegimes();
                viderChamps();
                regimeIdSelectionne = -1;
                JOptionPane.showMessageDialog(this, "Régime supprimé avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    private void chargerRegimes() {
        modelRegime.setRowCount(0);
        List<Regime> regimes = regimeDAO.findAll();
        for (Regime r : regimes) {
            modelRegime.addRow(new Object[]{
                    r.getIdRegime(),
                    r.getNomRegime(),
                    r.getTauxRemboursement()
            });
        }
    }

    private void chargerRegimeDansFormulaire() {
        int row = tableRegime.getSelectedRow();
        if (row != -1) {
            regimeIdSelectionne = (int) modelRegime.getValueAt(row, 0);
            Regime r = regimeDAO.findById(regimeIdSelectionne);
            if (r != null) {
                txtNomRegime.setText(r.getNomRegime());
                txtTauxRemboursement.setText(String.valueOf(r.getTauxRemboursement()));
            }
        }
    }

    private void rechercherParNom() {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) {
            chargerRegimes();
            return;
        }
        modelRegime.setRowCount(0);
        List<Regime> resultats = regimeDAO.findByNom(nom);
        for (Regime r : resultats) {
            modelRegime.addRow(new Object[]{
                    r.getIdRegime(),
                    r.getNomRegime(),
                    r.getTauxRemboursement()
            });
        }
    }

    private void viderChamps() {
        txtNomRegime.setText("");
        txtTauxRemboursement.setText("");
        txtRechercheNom.setText("");
        regimeIdSelectionne = -1;
    }
}
