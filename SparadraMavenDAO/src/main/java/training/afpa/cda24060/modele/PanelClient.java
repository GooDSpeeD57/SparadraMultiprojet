package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.exception.SaisieException;

public class PanelClient extends JPanel {

    private JTable tableClient;
    private DefaultTableModel modelClient;

    private JTextField txtNomClient, txtPrenomClient, txtAdresseClient, txtCodePostalClient;
    private JTextField txtVilleClient, txtTelephoneClient, txtEmailClient, txtNssClient;
    private JTextField txtDateNaissanceClient, txtMutuelleClient, txtMedecinrefClient;
    private JTextField txtRechercheNom, txtRechercheNss, txtRechercheEmail;

    public PanelClient() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de saisie
        JPanel panelSaisieClient = creerPanelSaisie();

        // Panel de recherche
        JPanel panelRechercheClient = creerPanelRecherche();

        // Panel supérieur
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieClient);
        panelSuperior.add(panelRechercheClient);

        // Table des clients
        creerTable();
        JScrollPane scrollClient = new JScrollPane(tableClient);
        scrollClient.setBorder(new TitledBorder("Liste des Clients"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollClient, BorderLayout.CENTER);
    }

    private JPanel creerPanelSaisie() {
        JPanel panelSaisieClient = new JPanel(new GridBagLayout());
        panelSaisieClient.setBorder(new TitledBorder("Nouveau Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Colonne 1
        ajouterChamp(panelSaisieClient, "Nom :", txtNomClient = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panelSaisieClient, "Prénom :", txtPrenomClient = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panelSaisieClient, "Adresse :", txtAdresseClient = new JTextField(15), 0, 2, gbc);
        ajouterChamp(panelSaisieClient, "Code Postal :", txtCodePostalClient = new JTextField(15), 0, 3, gbc);
        ajouterChamp(panelSaisieClient, "Ville :", txtVilleClient = new JTextField(15), 0, 4, gbc);
        ajouterChamp(panelSaisieClient, "Téléphone :", txtTelephoneClient = new JTextField(15), 0, 5, gbc);

        // Colonne 2
        ajouterChamp(panelSaisieClient, "Email :", txtEmailClient = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panelSaisieClient, "N° Séc. Sociale :", txtNssClient = new JTextField(15), 2, 1, gbc);
        ajouterChamp(panelSaisieClient, "Date de Naissance :", txtDateNaissanceClient = new JTextField(15), 2, 2, gbc);
        ajouterChamp(panelSaisieClient, "Mutuelle :", txtMutuelleClient = new JTextField(15), 2, 3, gbc);
        ajouterChamp(panelSaisieClient, "Médecin Référent :", txtMedecinrefClient = new JTextField(15), 2, 4, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> supprimerClient());
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        panelSaisieClient.add(panelBoutons, gbc);

        return panelSaisieClient;
    }

    private JPanel creerPanelRecherche() {
        JPanel panelRecherche = new JPanel(new GridBagLayout());
        panelRecherche.setBorder(new TitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Recherche par nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelRecherche.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panelRecherche.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherParNom());
        panelRecherche.add(btnRechercheNom, gbc);

        // Recherche par email
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRecherche.add(new JLabel("Par email:"), gbc);
        gbc.gridx = 1;
        txtRechercheEmail = new JTextField(15);
        panelRecherche.add(txtRechercheEmail, gbc);
        gbc.gridx = 2;
        JButton btnRechercheEmail = new JButton("🔍");
        btnRechercheEmail.addActionListener(e -> rechercherParEmail());
        panelRecherche.add(btnRechercheEmail, gbc);

        // Recherche par NSS
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRecherche.add(new JLabel("Par NSS:"), gbc);
        gbc.gridx = 1;
        txtRechercheNss = new JTextField(15);
        panelRecherche.add(txtRechercheNss, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNss = new JButton("🔍");
        btnRechercheNss.addActionListener(e -> rechercherParNss());
        panelRecherche.add(btnRechercheNss, gbc);

        // Afficher tous
        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerClients());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panelRecherche.add(btnAfficherTous, gbc);

        return panelRecherche;
    }

    private void creerTable() {
        String[] colonnes = {"Nom", "Prénom", "Adresse", "Code Postal", "Ville", "Téléphone",
                "Email", "N° Séc. Sociale", "Date Naissance", "Mutuelle", "Médecin Réf"};
        modelClient = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableClient = new JTable(modelClient);
        tableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableClient.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerClientDansFormulaire();
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

    private void ajouterClient() {
        try {
            String nom = txtNomClient.getText().trim();
            String prenom = txtPrenomClient.getText().trim();
            String adresse = txtAdresseClient.getText().trim();
            String codePostal = txtCodePostalClient.getText().trim();
            String ville = txtVilleClient.getText().trim();
            String telephone = txtTelephoneClient.getText().trim();
            String email = txtEmailClient.getText().trim();
            String nSs = txtNssClient.getText().trim();
            String dateNaissance = txtDateNaissanceClient.getText().trim();
            String mutuelle = txtMutuelleClient.getText().trim();
            String medecinRef = txtMedecinrefClient.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() ||
                    ville.isEmpty() || telephone.isEmpty() || email.isEmpty() || nSs.isEmpty() ||
                    dateNaissance.isEmpty() || mutuelle.isEmpty() || medecinRef.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new Client(nom, prenom, adresse, codePostal, ville, telephone, email,
                    nSs, dateNaissance, mutuelle, medecinRef);
            chargerClients();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierClient() {
        int selectedRow = tableClient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à modifier.");
            return;
        }

        try {
            Client client = Client.getClients().get(selectedRow);
            client.setNom(txtNomClient.getText());
            client.setPrenom(txtPrenomClient.getText());
            client.setAdresse(txtAdresseClient.getText());
            client.setCodePostal(txtCodePostalClient.getText());
            client.setVille(txtVilleClient.getText());
            client.setTelephone(txtTelephoneClient.getText());
            client.setEmail(txtEmailClient.getText());
            client.setNss(txtNssClient.getText());
            client.setDateNaissance(txtDateNaissanceClient.getText());
            client.setMutuelle(txtMutuelleClient.getText());
            client.setMedecinRef(txtMedecinrefClient.getText());

            chargerClients();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Client modifié avec succès !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void supprimerClient() {
        int selectedRow = tableClient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?",
                "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Client.getClients().remove(selectedRow);
            chargerClients();
            viderChamps();
        }
    }

    public void chargerClients() {
        modelClient.setRowCount(0);
        for (Client client : Client.getClients()) {
            modelClient.addRow(new Object[]{
                    client.getNom(), client.getPrenom(), client.getAdresse(), client.getCodePostal(),
                    client.getVille(), client.getTelephone(), client.getEmail(), client.getNss(),
                    client.getDateNaissance(), client.getMutuelle(), client.getMedecinRef()
            });
        }
    }

    private void chargerClientDansFormulaire() {
        int row = tableClient.getSelectedRow();
        if (row != -1) {
            Client c = Client.getClients().get(row);
            txtNomClient.setText(c.getNom());
            txtPrenomClient.setText(c.getPrenom());
            txtAdresseClient.setText(c.getAdresse());
            txtCodePostalClient.setText(c.getCodePostal());
            txtVilleClient.setText(c.getVille());
            txtTelephoneClient.setText(c.getTelephone());
            txtEmailClient.setText(c.getEmail());
            txtNssClient.setText(c.getNss());
            txtDateNaissanceClient.setText(c.getDateNaissance());
            txtMutuelleClient.setText(c.getMutuelle());
            txtMedecinrefClient.setText(c.getMedecinRef());
        }
    }

    private void rechercherParNom() {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) {
            chargerClients();
            return;
        }

        modelClient.setRowCount(0);
        List<Client> resultats = Client.rechercherClientParNom(nom);
        for (Client c : resultats) {
            modelClient.addRow(new Object[]{
                    c.getNom(), c.getPrenom(), c.getAdresse(), c.getCodePostal(),
                    c.getVille(), c.getTelephone(), c.getEmail(), c.getNss(),
                    c.getDateNaissance(), c.getMutuelle(), c.getMedecinRef()
            });
        }
    }

    private void rechercherParEmail() {
        String email = txtRechercheEmail.getText().trim();
        if (email.isEmpty()) {
            chargerClients();
            return;
        }

        modelClient.setRowCount(0);
        List<Client> resultats = Client.rechercherClientParEmail(email);
        for (Client c : resultats) {
            modelClient.addRow(new Object[]{
                    c.getNom(), c.getPrenom(), c.getAdresse(), c.getCodePostal(),
                    c.getVille(), c.getTelephone(), c.getEmail(), c.getNss(),
                    c.getDateNaissance(), c.getMutuelle(), c.getMedecinRef()
            });
        }
    }

    private void rechercherParNss() {
        String nss = txtRechercheNss.getText().trim();
        if (nss.isEmpty()) {
            chargerClients();
            return;
        }

        modelClient.setRowCount(0);
        List<Client> resultats = Client.rechercherClientParNss(nss);
        for (Client c : resultats) {
            modelClient.addRow(new Object[]{
                    c.getNom(), c.getPrenom(), c.getAdresse(), c.getCodePostal(),
                    c.getVille(), c.getTelephone(), c.getEmail(), c.getNss(),
                    c.getDateNaissance(), c.getMutuelle(), c.getMedecinRef()
            });
        }
    }

    private void viderChamps() {
        txtNomClient.setText("");
        txtPrenomClient.setText("");
        txtAdresseClient.setText("");
        txtCodePostalClient.setText("");
        txtVilleClient.setText("");
        txtTelephoneClient.setText("");
        txtEmailClient.setText("");
        txtNssClient.setText("");
        txtDateNaissanceClient.setText("");
        txtMutuelleClient.setText("");
        txtMedecinrefClient.setText("");
    }
}