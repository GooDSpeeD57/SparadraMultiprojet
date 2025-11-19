package training.afpa.cda24060.vue.panel;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientPanel extends JPanel {

    // Champs et table
    private JTextField txtNomClient, txtPrenomClient, txtAdresseClient, txtCodePostalClient, txtVilleClient,
            txtTelephoneClient, txtEmailClient, txtNssClient, txtDateNaissanceClient, txtMutuelleClient, txtMedecinrefClient;
    private JTextField txtRechercheNom, txtRechercheEmail, txtRechercheNss;
    private JTable tableClient;
    private DefaultTableModel modelClient;

    public ClientPanel() {
        setLayout(new BorderLayout());
        creerPanelClient();
    }

    private void creerPanelClient() {
        // === Panel de saisie ===
        JPanel panelSaisieClient = new JPanel(new GridBagLayout());
        panelSaisieClient.setBorder(new TitledBorder("Nouveau Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels et champs
        txtNomClient = ajouterChamp(panelSaisieClient, gbc, 0, "Nom :");
        txtPrenomClient = ajouterChamp(panelSaisieClient, gbc, 1, "Prénom :");
        txtAdresseClient = ajouterChamp(panelSaisieClient, gbc, 2, "Adresse :");
        txtCodePostalClient = ajouterChamp(panelSaisieClient, gbc, 3, "Code Postal :");
        txtVilleClient = ajouterChamp(panelSaisieClient, gbc, 4, "Ville :");
        txtTelephoneClient = ajouterChamp(panelSaisieClient, gbc, 5, "Téléphone :");
        txtEmailClient = ajouterChamp(panelSaisieClient, gbc, 6, "Email :");
        txtNssClient = ajouterChamp(panelSaisieClient, gbc, 7, "N° Séc. Sociale :");
        txtDateNaissanceClient = ajouterChamp(panelSaisieClient, gbc, 8, "Date Naissance :");
        txtMutuelleClient = ajouterChamp(panelSaisieClient, gbc, 9, "Mutuelle :");
        txtMedecinrefClient = ajouterChamp(panelSaisieClient, gbc, 10, "Médecin Réf :");

        // Panel boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> supprimerClient());
        btnVider.addActionListener(e -> viderChampsClient());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;
        panelSaisieClient.add(panelBoutons, gbc);

        // === Panel recherche ===
        JPanel panelRechercheClient = new JPanel(new GridBagLayout());
        panelRechercheClient.setBorder(new TitledBorder("Recherche"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        txtRechercheNom = ajouterChamp(panelRechercheClient, gbc, 0, "Nom :");
        JButton btnRechNom = new JButton("Rechercher");
        gbc.gridx = 1; gbc.gridy = 0;
        panelRechercheClient.add(btnRechNom, gbc);
        btnRechNom.addActionListener(e -> rechercherClientParNom());

        txtRechercheEmail = ajouterChamp(panelRechercheClient, gbc, 1, "Email :");
        JButton btnRechEmail = new JButton("Rechercher");
        gbc.gridx = 1; gbc.gridy = 1;
        panelRechercheClient.add(btnRechEmail, gbc);
        btnRechEmail.addActionListener(e -> rechercherClientParEmail());

        txtRechercheNss = ajouterChamp(panelRechercheClient, gbc, 2, "N° Séc. Sociale :");
        JButton btnRechNss = new JButton("Rechercher");
        gbc.gridx = 1; gbc.gridy = 2;
        panelRechercheClient.add(btnRechNss, gbc);
        btnRechNss.addActionListener(e -> rechercherClientParNss());

        // === Panel supérieur ===
        JPanel panelSuperior = new JPanel(new GridLayout(1,2));
        panelSuperior.add(panelSaisieClient);
        panelSuperior.add(panelRechercheClient);

        // Table des clients
        String[] colonnesClient = {"Nom", "Prénom", "Adresse","Code Postal","Ville","Téléphone","Email",
                "N° Séc. Sociale","Date Naissance","Mutuelle","Médecin Réf"};
        modelClient = new DefaultTableModel(colonnesClient, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableClient = new JTable(modelClient);
        tableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableClient.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableClient.getSelectedRow();
                    if (row != -1) {
                        Client c = Client.getClients().get(row);
                        remplirChampsClient(c);
                    }
                }
            }
        });

        JScrollPane scrollClient = new JScrollPane(tableClient);
        scrollClient.setBorder(new TitledBorder("Liste des Clients"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollClient, BorderLayout.CENTER);

        chargerClient(); // chargement initial
    }

    // Méthode utilitaire pour créer un champ
    private JTextField ajouterChamp(JPanel panel, GridBagConstraints gbc, int y, String label) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        JTextField txt = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txt, gbc);
        return txt;
    }

    // === Actions CRUD ===
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
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new Client(nom, prenom, adresse, codePostal, ville, telephone, email, nSs, dateNaissance, mutuelle, medecinRef);
            chargerClient();
            viderChampsClient();
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

            chargerClient();
            viderChampsClient();
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

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Client.getClients().remove(selectedRow);
            chargerClient();
            viderChampsClient();
        }
    }

    private void viderChampsClient() {
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

    private void remplirChampsClient(Client c) {
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

    private void afficherClients(List<Client> clients) {
        modelClient.setRowCount(0);
        for (Client c : clients) {
            modelClient.addRow(new Object[]{
                    c.getNom(), c.getPrenom(), c.getAdresse(), c.getCodePostal(),
                    c.getVille(), c.getTelephone(), c.getEmail(), c.getNss(),
                    c.getDateNaissance(), c.getMutuelle(), c.getMedecinRef()
            });
        }
    }

    public void chargerClient() {
        afficherClients(Client.getClients());
    }

    private void rechercherClientParNom() {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) {
            chargerClient();
            return;
        }
        afficherClients(Client.rechercherClientParNom(nom));
    }

    private void rechercherClientParEmail() {
        String email = txtRechercheEmail.getText().trim();
        if (email.isEmpty()) {
            chargerClient();
            return;
        }
        afficherClients(Client.rechercherClientParEmail(email));
    }

    private void rechercherClientParNss() {
        String nss = txtRechercheNss.getText().trim();
        if (nss.isEmpty()) {
            chargerClient();
            return;
        }
        afficherClients(Client.rechercherClientParNss(nss));
    }
}
