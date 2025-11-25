package training.afpa.cda24060.vue.PanelSwing;

import training.afpa.cda24060.ClasseDAO.ClientDAO;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelClient extends JPanel {

    private final ClientDAO clientDAO = new ClientDAO();

    private JTable tableClient;
    private DefaultTableModel modelClient;

    private JTextField txtNom, txtPrenom, txtAdresse, txtCodePostal, txtVille,
            txtTelephone, txtEmail, txtNss, txtDateNaissance,
            txtIdRegime, txtIdMedecin, txtIdMutuelle, txtIdTitulaire;
    private JTextField txtRechercheNom;

    private int clientIdSelectionne = -1;

    public PanelClient() {
        initComponents();
        chargerClients();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSaisie = creerPanelSaisie();
        JPanel panelRecherche = creerPanelRecherche();

        JPanel panelNorth = new JPanel(new GridLayout(1, 2));
        panelNorth.add(panelSaisie);
        panelNorth.add(panelRecherche);

        creerTable();
        JScrollPane scrollTable = new JScrollPane(tableClient);
        scrollTable.setBorder(new TitledBorder("Liste des Clients"));

        add(panelNorth, BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouveau Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Colonne 1
        ajouterChamp(panel, "Nom :", txtNom = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Prénom :", txtPrenom = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panel, "Adresse :", txtAdresse = new JTextField(15), 0, 2, gbc);
        ajouterChamp(panel, "Code Postal :", txtCodePostal = new JTextField(15), 0, 3, gbc);
        ajouterChamp(panel, "Ville :", txtVille = new JTextField(15), 0, 4, gbc);
        ajouterChamp(panel, "Téléphone :", txtTelephone = new JTextField(15), 0, 5, gbc);

        // Colonne 2
        ajouterChamp(panel, "Email :", txtEmail = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panel, "N° Séc. Sociale :", txtNss = new JTextField(15), 2, 1, gbc);
        ajouterChamp(panel, "Date de Naissance :", txtDateNaissance = new JTextField(15), 2, 2, gbc);
        ajouterChamp(panel, "ID Régime :", txtIdRegime = new JTextField(15), 2, 3, gbc);
        ajouterChamp(panel, "ID Médecin :", txtIdMedecin = new JTextField(15), 2, 4, gbc);
        ajouterChamp(panel, "ID Mutuelle :", txtIdMutuelle = new JTextField(15), 2, 5, gbc);

        // ID Titulaire
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("ID Titulaire :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtIdTitulaire = new JTextField(15);
        panel.add(txtIdTitulaire, gbc);

        // Boutons
        JPanel panelBtns = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> supprimerClient());
        btnVider.addActionListener(e -> viderChamps());

        panelBtns.add(btnAjouter);
        panelBtns.add(btnModifier);
        panelBtns.add(btnSupprimer);
        panelBtns.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        panel.add(panelBtns, gbc);

        return panel;
    }

    private JPanel creerPanelRecherche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Par nom :"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panel.add(txtRechercheNom, gbc);

        gbc.gridx = 2;
        JButton btnRecherche = new JButton("🔍");
        btnRecherche.addActionListener(e -> rechercherParNom());
        panel.add(btnRecherche, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerClients());
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"ID", "Nom", "Prénom", "Adresse", "Code Postal", "Ville",
                "Téléphone", "Email", "NSS", "Date Naissance", "Médecin", "Mutuelle", "Régime"};
        modelClient = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableClient = new JTable(modelClient);
        tableClient.setAutoCreateRowSorter(true);
        tableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableClient.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) chargerClientDansFormulaire();
            }
        });
    }

    private void ajouterChamp(JPanel panel, String label, JTextField field, int colLabel, int row, GridBagConstraints gbc) {
        gbc.gridx = colLabel; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = colLabel + 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void ajouterClient() {
        try {
            Client c = new Client();
            c.setNom(txtNom.getText().trim());
            c.setPrenom(txtPrenom.getText().trim());
            c.setAdresse(txtAdresse.getText().trim());
            c.setCodePostal(txtCodePostal.getText().trim());
            c.setVille(txtVille.getText().trim());
            c.setTelephone(txtTelephone.getText().trim());
            c.setEmail(txtEmail.getText().trim());
            c.setNss(txtNss.getText().trim());
            c.setDateNaissance(txtDateNaissance.getText().trim());
            c.setIdTitulaireMutuelle(txtIdTitulaire.getText().trim());

            if (!txtIdRegime.getText().trim().isEmpty()) {
                Regime r = new Regime();
                r.setIdRegime(Integer.parseInt(txtIdRegime.getText().trim()));
                c.setRegime(r);
            }
            if (!txtIdMedecin.getText().trim().isEmpty()) {
                Medecin m = new Medecin();
                m.setIdMedecin(Integer.parseInt(txtIdMedecin.getText().trim()));
                c.setMedecin(m);
            }
            if (!txtIdMutuelle.getText().trim().isEmpty()) {
                Mutuelle mu = new Mutuelle();
                mu.setIdMutuelle(Integer.parseInt(txtIdMutuelle.getText().trim()));
                c.setMutuelle(mu);
            }

            if (clientDAO.insert(c)) {
                chargerClients();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
            }
        } catch (SaisieException se) {
            JOptionPane.showMessageDialog(this, "Erreur saisie : " + se.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void modifierClient() {
        if (clientIdSelectionne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à modifier.");
            return;
        }

        try {
            Client c = clientDAO.findById(clientIdSelectionne);
            if (c != null) {
                c.setNom(txtNom.getText().trim());
                c.setPrenom(txtPrenom.getText().trim());
                c.setAdresse(txtAdresse.getText().trim());
                c.setCodePostal(txtCodePostal.getText().trim());
                c.setVille(txtVille.getText().trim());
                c.setTelephone(txtTelephone.getText().trim());
                c.setEmail(txtEmail.getText().trim());
                c.setNss(txtNss.getText().trim());
                c.setDateNaissance(txtDateNaissance.getText().trim());
                c.setIdTitulaireMutuelle(txtIdTitulaire.getText().trim());

                if (!txtIdRegime.getText().trim().isEmpty()) {
                    Regime r = new Regime(); r.setIdRegime(Integer.parseInt(txtIdRegime.getText().trim())); c.setRegime(r);
                }
                if (!txtIdMedecin.getText().trim().isEmpty()) {
                    Medecin m = new Medecin(); m.setIdMedecin(Integer.parseInt(txtIdMedecin.getText().trim())); c.setMedecin(m);
                }
                if (!txtIdMutuelle.getText().trim().isEmpty()) {
                    Mutuelle mu = new Mutuelle(); mu.setIdMutuelle(Integer.parseInt(txtIdMutuelle.getText().trim())); c.setMutuelle(mu);
                }

                if (clientDAO.update(c)) {
                    chargerClients();
                    viderChamps();
                    clientIdSelectionne = -1;
                    JOptionPane.showMessageDialog(this, "Client modifié avec succès !");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerClient() {
        if (clientIdSelectionne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (clientDAO.delete(clientIdSelectionne)) {
                chargerClients();
                viderChamps();
                clientIdSelectionne = -1;
                JOptionPane.showMessageDialog(this, "Client supprimé !");
            }
        }
    }

    public void chargerClients() {
        modelClient.setRowCount(0);
        List<Client> clients = clientDAO.findAll();
        for (Client c : clients) {
            modelClient.addRow(new Object[]{
                    c.getIdClient(), c.getNom(), c.getPrenom(), c.getAdresse(),
                    c.getCodePostal(), c.getVille(), c.getTelephone(), c.getEmail(),
                    c.getNss(), c.getDateNaissance(),
                    c.getMedecin() != null ? c.getMedecin().getNom() : "",
                    c.getMutuelle() != null ? c.getMutuelle().getNomMutuelle() : "",
                    c.getRegime() != null ? c.getRegime().getNomRegime() : ""
            });
        }
    }

    private void chargerClientDansFormulaire() {
        int row = tableClient.getSelectedRow();
        if (row != -1) {
            clientIdSelectionne = (int) modelClient.getValueAt(row, 0);
            Client c = clientDAO.findById(clientIdSelectionne);
            if (c != null) {
                txtNom.setText(c.getNom());
                txtPrenom.setText(c.getPrenom());
                txtAdresse.setText(c.getAdresse());
                txtCodePostal.setText(c.getCodePostal());
                txtVille.setText(c.getVille());
                txtTelephone.setText(c.getTelephone());
                txtEmail.setText(c.getEmail());
                txtNss.setText(c.getNss());
                txtDateNaissance.setText(c.getDateNaissance() != null ? c.getDateNaissance().toString() : "");
                txtIdTitulaire.setText(c.getIdTitulaireMutuelle());
                txtIdRegime.setText(c.getRegime() != null ? String.valueOf(c.getRegime().getIdRegime()) : "");
                txtIdMedecin.setText(c.getMedecin() != null ? String.valueOf(c.getMedecin().getIdMedecin()) : "");
                txtIdMutuelle.setText(c.getMutuelle() != null ? String.valueOf(c.getMutuelle().getIdMutuelle()) : "");
            }
        }
    }

    private void rechercherParNom() {
        String nom = txtRechercheNom.getText().trim();
        modelClient.setRowCount(0);
        List<Client> resultats = nom.isEmpty() ? clientDAO.findAll() : clientDAO.findByNom(nom);
        for (Client c : resultats) {
            modelClient.addRow(new Object[]{
                    c.getIdClient(), c.getNom(), c.getPrenom(), c.getAdresse(),
                    c.getCodePostal(), c.getVille(), c.getTelephone(), c.getEmail(),
                    c.getNss(), c.getDateNaissance(),
                    c.getMedecin() != null ? c.getMedecin().getNom() : "",
                    c.getMutuelle() != null ? c.getMutuelle().getNomMutuelle() : "",
                    c.getRegime() != null ? c.getRegime().getNomRegime() : ""
            });
        }
    }

    private void viderChamps() {
        txtNom.setText(""); txtPrenom.setText(""); txtAdresse.setText(""); txtCodePostal.setText("");
        txtVille.setText(""); txtTelephone.setText(""); txtEmail.setText(""); txtNss.setText(""); txtDateNaissance.setText("");
        txtIdRegime.setText(""); txtIdMedecin.setText(""); txtIdMutuelle.setText(""); txtIdTitulaire.setText("");
        clientIdSelectionne = -1;
    }
}
