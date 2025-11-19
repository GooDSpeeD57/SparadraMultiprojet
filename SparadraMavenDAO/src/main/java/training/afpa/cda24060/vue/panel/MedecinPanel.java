package training.afpa.cda24060.vue.panel;

import training.afpa.cda24060.modele.Medecin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MedecinPanel extends JPanel {

    // Composants
    private JTextField txtNomMedecin, txtPrenomMedecin, txtAdresseMedecin, txtCodePostalMedecin,
            txtVilleMedecin, txtTelephoneMedecin, txtEmailMedecin, txtRPPSMedecin;
    private JTextField txtRechercheRpps;
    private JTable tableMedecin;
    private DefaultTableModel modelMedecin;

    public MedecinPanel() {
        setLayout(new BorderLayout());
        initComponents();
        chargerMedecin();
    }

    private void initComponents() {
        // Panel de saisie
        JPanel panelSaisie = new JPanel(new GridBagLayout());
        panelSaisie.setBorder(new TitledBorder("Nouveau Médecin"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs de saisie
        txtNomMedecin = new JTextField(15);
        txtPrenomMedecin = new JTextField(15);
        txtAdresseMedecin = new JTextField(15);
        txtCodePostalMedecin = new JTextField(15);
        txtVilleMedecin = new JTextField(15);
        txtTelephoneMedecin = new JTextField(15);
        txtEmailMedecin = new JTextField(15);
        txtRPPSMedecin = new JTextField(15);

        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = 0; panelSaisie.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtNomMedecin, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; panelSaisie.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtPrenomMedecin, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; panelSaisie.add(new JLabel("Adresse :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtAdresseMedecin, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; panelSaisie.add(new JLabel("Code Postal :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtCodePostalMedecin, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; panelSaisie.add(new JLabel("Ville :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtVilleMedecin, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; panelSaisie.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtTelephoneMedecin, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; panelSaisie.add(new JLabel("Email :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtEmailMedecin, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; panelSaisie.add(new JLabel("N° RPPS :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; panelSaisie.add(txtRPPSMedecin, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterMedecin());
        btnModifier.addActionListener(e -> modifierMedecin());
        btnSupprimer.addActionListener(e -> supprimerMedecin());
        btnVider.addActionListener(e -> viderChampsMedecin());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; panelSaisie.add(panelBoutons, gbc);

        // Panel recherche
        JPanel panelRecherche = new JPanel(new GridBagLayout());
        panelRecherche.setBorder(new TitledBorder("Recherche"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0; panelRecherche.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1; JTextField txtRechercheNom = new JTextField(15); panelRecherche.add(txtRechercheNom, gbc);
        gbc.gridx = 2; JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherMedecinParNom(txtRechercheNom.getText()));
        panelRecherche.add(btnRechercheNom, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelRecherche.add(new JLabel("Par RPPS:"), gbc);
        gbc.gridx = 1; txtRechercheRpps = new JTextField(15); panelRecherche.add(txtRechercheRpps, gbc);
        gbc.gridx = 2; JButton btnRechercheRpps = new JButton("🔍");
        btnRechercheRpps.addActionListener(e -> rechercherMedecinParRpps());
        panelRecherche.add(btnRechercheRpps, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerMedecin());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; panelRecherche.add(btnAfficherTous, gbc);

        JPanel panelSuperior = new JPanel(new GridLayout(1,2));
        panelSuperior.add(panelSaisie);
        panelSuperior.add(panelRecherche);

        // Table
        String[] colonnes = {"Nom","Prénom","Adresse","Code Postal","Ville","Téléphone","Email","RPPS"};
        modelMedecin = new DefaultTableModel(colonnes,0){
            @Override
            public boolean isCellEditable(int row,int column){ return false; }
        };
        tableMedecin = new JTable(modelMedecin);
        tableMedecin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMedecin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2) remplirChampsDepuisTable();
            }
        });

        JScrollPane scroll = new JScrollPane(tableMedecin);
        scroll.setBorder(new TitledBorder("Liste des Médecins"));

        // Ajout au panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void chargerMedecin() {
        modelMedecin.setRowCount(0);
        for(Medecin m : Medecin.getMedecins()){
            modelMedecin.addRow(new Object[]{
                    m.getNom(), m.getPrenom(), m.getAdresse(), m.getCodePostal(),
                    m.getVille(), m.getTelephone(), m.getEmail(), m.getRPPS()
            });
        }
    }

    private void ajouterMedecin() {
        Medecin.getMedecins().add(new Medecin(
                txtNomMedecin.getText(), txtPrenomMedecin.getText(), txtAdresseMedecin.getText(),
                txtCodePostalMedecin.getText(), txtVilleMedecin.getText(), txtTelephoneMedecin.getText(),
                txtEmailMedecin.getText(), txtRPPSMedecin.getText()
        ));
        chargerMedecin();
        viderChampsMedecin();
    }

    private void modifierMedecin() {
        int row = tableMedecin.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Sélectionnez un médecin à modifier."); return; }

        Medecin m = Medecin.getMedecins().get(row);
        m.setNom(txtNomMedecin.getText());
        m.setPrenom(txtPrenomMedecin.getText());
        m.setAdresse(txtAdresseMedecin.getText());
        m.setCodePostal(txtCodePostalMedecin.getText());
        m.setVille(txtVilleMedecin.getText());
        m.setTelephone(txtTelephoneMedecin.getText());
        m.setEmail(txtEmailMedecin.getText());
        m.setRPPS(txtRPPSMedecin.getText());
        chargerMedecin();
        viderChampsMedecin();
    }

    private void supprimerMedecin() {
        int row = tableMedecin.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Sélectionnez un médecin à supprimer."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,"Confirmer la suppression ?","Supprimer",JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            Medecin.getMedecins().remove(row);
            chargerMedecin();
            viderChampsMedecin();
        }
    }

    private void viderChampsMedecin() {
        txtNomMedecin.setText("");
        txtPrenomMedecin.setText("");
        txtAdresseMedecin.setText("");
        txtCodePostalMedecin.setText("");
        txtVilleMedecin.setText("");
        txtTelephoneMedecin.setText("");
        txtEmailMedecin.setText("");
        txtRPPSMedecin.setText("");
    }

    private void remplirChampsDepuisTable() {
        int row = tableMedecin.getSelectedRow();
        if(row != -1){
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

    private void rechercherMedecinParNom(String nom){
        if(nom.trim().isEmpty()){ chargerMedecin(); return; }
        modelMedecin.setRowCount(0);
        List<Medecin> res = Medecin.rechercherParNom(nom);
        for(Medecin m : res){
            modelMedecin.addRow(new Object[]{ m.getNom(), m.getPrenom(), m.getAdresse(), m.getCodePostal(),
                    m.getVille(), m.getTelephone(), m.getEmail(), m.getRPPS() });
        }
    }

    private void rechercherMedecinParRpps(){
        String rpps = txtRechercheRpps.getText().trim();
        if(rpps.isEmpty()){ chargerMedecin(); return; }
        modelMedecin.setRowCount(0);
        List<Medecin> res = Medecin.rechercherParRpps(rpps);
        for(Medecin m : res){
            modelMedecin.addRow(new Object[]{ m.getNom(), m.getPrenom(), m.getAdresse(), m.getCodePostal(),
                    m.getVille(), m.getTelephone(), m.getEmail(), m.getRPPS() });
        }
    }
}
