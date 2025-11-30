package training.afpa.cda24060.vue.PanelSwing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import training.afpa.cda24060.ClasseDAO.ClientDAO;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;
import training.afpa.cda24060.utilitaires.DateTimePatternFr;
import training.afpa.cda24060.utilitaires.JTableUtil;
import training.afpa.cda24060.utilitaires.RegexValidator;

import static training.afpa.cda24060.utilitaires.JTableUtil.ajusterColonnesJTable;

public class PanelClient extends JPanel {

    private final ClientDAO clientDAO = new ClientDAO();

    private JTable tableClient;
    private DefaultTableModel modelClient;

    private JTextField txtNomClient, txtPrenomClient, txtAdresseClient, txtCodePostalClient;
    private JTextField txtVilleClient, txtTelephoneClient, txtEmailClient, txtNssClient;
    private JTextField txtDateNaissanceClient, txtIdRegimeClient, txtIdMedecinClient, txtIdMutuelleClient;
    private JTextField txtIdTitulaireClient;
    private JTextField txtRechercheNom, txtRechercheNss, txtRechercheEmail;

    private int clientIdSelectionne = -1;

    public PanelClient() throws SQLException, SaisieException {
        initComponents();
    }

    private void initComponents() throws SQLException, SaisieException {
        setLayout(new BorderLayout());

        JPanel panelSaisieClient = creerPanelSaisie();
        JPanel panelRechercheClient = creerPanelRecherche();

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieClient);
        panelSuperior.add(panelRechercheClient);

        creerTable();
        JScrollPane scrollClient = new JScrollPane(tableClient);
        scrollClient.setBorder(new TitledBorder("Liste des Clients"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollClient, BorderLayout.CENTER);

        chargerClients();
    }

    private JPanel creerPanelSaisie() {
        JPanel panelSaisieClient = new JPanel(new GridBagLayout());
        panelSaisieClient.setBorder(new TitledBorder("Nouveau Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        creerChampsSaisie(panelSaisieClient, gbc);
        creerPanelBoutonsSaisie(panelSaisieClient, gbc);

        return panelSaisieClient;
    }

    private void creerChampsSaisie(JPanel panelSaisieClient, GridBagConstraints gbc) {
        ajouterChamp(panelSaisieClient, "Nom :", txtNomClient = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panelSaisieClient, "Prénom :", txtPrenomClient = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panelSaisieClient, "Adresse :", txtAdresseClient = new JTextField(15), 0, 2, gbc);
        ajouterChamp(panelSaisieClient, "Code Postal :", txtCodePostalClient = new JTextField(15), 0, 3, gbc);
        ajouterChamp(panelSaisieClient, "Ville :", txtVilleClient = new JTextField(15), 0, 4, gbc);
        ajouterChamp(panelSaisieClient, "Téléphone :", txtTelephoneClient = new JTextField(15), 0, 5, gbc);

        ajouterChamp(panelSaisieClient, "Email :", txtEmailClient = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panelSaisieClient, "N° Séc. Sociale :", txtNssClient = new JTextField(15), 2, 1, gbc);
        ajouterChamp(panelSaisieClient, "Date de Naissance :", txtDateNaissanceClient = new JTextField(15), 2, 2, gbc);
        ajouterChamp(panelSaisieClient, "ID Régime :", txtIdRegimeClient = new JTextField(15), 2, 3, gbc);
        ajouterChamp(panelSaisieClient, "ID Médecin :", txtIdMedecinClient = new JTextField(15), 2, 4, gbc);
        ajouterChamp(panelSaisieClient, "ID Mutuelle :", txtIdMutuelleClient = new JTextField(15), 2, 5, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("ID Titulaire :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtIdTitulaireClient = new JTextField(15);
        panelSaisieClient.add(txtIdTitulaireClient, gbc);
    }

    private void creerPanelBoutonsSaisie(JPanel panelSaisieClient, GridBagConstraints gbc) {
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> {
            try {
                supprimerClient();
            } catch (SQLException | SaisieException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        panelSaisieClient.add(panelBoutons, gbc);
    }

    private JPanel creerPanelRecherche() {
        JPanel panelRecherche = new JPanel(new GridBagLayout());
        panelRecherche.setBorder(new TitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelRecherche.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panelRecherche.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> {
            try {
                rechercherParNom();
            } catch (SQLException | SaisieException ex) {
                throw new RuntimeException(ex);
            }
        });
        panelRecherche.add(btnRechercheNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRecherche.add(new JLabel("Par N° Séc. Sociale:"), gbc);
        gbc.gridx = 1;
        txtRechercheNss = new JTextField(15);
        panelRecherche.add(txtRechercheNss, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNss = new JButton("🔍");
        btnRechercheNss.addActionListener(e -> {
            try {
                rechercherParNss();
            } catch (SQLException | SaisieException ex) {
                throw new RuntimeException(ex);
            }
        });
        panelRecherche.add(btnRechercheNss, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRecherche.add(new JLabel("Par Email:"), gbc);
        gbc.gridx = 1;
        txtRechercheEmail = new JTextField(15);
        panelRecherche.add(txtRechercheEmail, gbc);
        gbc.gridx = 2;
        JButton btnRechercheEmail = new JButton("🔍");
        btnRechercheEmail.addActionListener(e -> {
            try {
                rechercherParEmail();
            } catch (SQLException | SaisieException ex) {
                throw new RuntimeException(ex);
            }
        });
        panelRecherche.add(btnRechercheEmail, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> {
            try {
                chargerClients();
            } catch (SQLException | SaisieException ex) {
                throw new RuntimeException(ex);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panelRecherche.add(btnAfficherTous, gbc);
        return panelRecherche;
    }

    private void creerTable() {
        String[] colonnes = {"ID", "Nom Prénom", "Adresse", "Code Postal", "Ville",
                "Téléphone", "Email", "NSS", "Date Naissance", "Médecin", "Mutuelle",
                "Régime","N°Assuré"
        };

        modelClient = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableClient = new JTable(modelClient);
        tableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableUtil.appliquerLignesAlternes(tableClient);
        ajusterColonnesJTable(tableClient);
        tableClient.getColumnModel().getColumn(0).setMinWidth(0);
        tableClient.getColumnModel().getColumn(0).setMaxWidth(0);
        tableClient.getColumnModel().getColumn(0).setWidth(0);
        tableClient.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        chargerClientDansFormulaire();
                    } catch (SQLException | SaisieException ex) {
                        throw new RuntimeException(ex);
                    }
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
            validerChampsClient();
            Client client = new Client();

            client.setNom(txtNomClient.getText().trim());
            client.setPrenom(txtPrenomClient.getText().trim());
            client.setAdresse(txtAdresseClient.getText().trim());
            client.setCodePostal(txtCodePostalClient.getText().trim());
            client.setVille(txtVilleClient.getText().trim());
            client.setTelephone(txtTelephoneClient.getText().trim());
            client.setEmail(txtEmailClient.getText().trim());
            client.setNss(txtNssClient.getText().trim());

            String dateStr = txtDateNaissanceClient.getText().trim();
            if (!dateStr.isEmpty()) {
                client.setDateNaissance(dateStr); // ✅ string direct
            }
            client.setIdTitulaireMutuelle(txtIdTitulaireClient.getText().trim());
            if (!txtIdRegimeClient.getText().trim().isEmpty()) {
                Regime r = new Regime();
                r.setIdRegime(Integer.parseInt(txtIdRegimeClient.getText().trim()));
                client.setRegime(r);
            }
            if (!txtIdMedecinClient.getText().trim().isEmpty()) {
                Medecin m = new Medecin();
                m.setId(Integer.parseInt(txtIdMedecinClient.getText().trim()));
                client.setMedecin(m);
            }
            if (!txtIdMutuelleClient.getText().trim().isEmpty()) {
                Mutuelle mu = new Mutuelle();
                mu.setIdMutuelle(Integer.parseInt(txtIdMutuelleClient.getText().trim()));
                client.setMutuelle(mu);
            }
            if (clientDAO.insert(client)) {
                chargerClients();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client.");
            }
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur : un ID doit être un nombre entier.");
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
            validerChampsClient();
            Client client = clientDAO.findById(clientIdSelectionne);
            if (client != null) {
                client.setNom(txtNomClient.getText().trim());
                client.setPrenom(txtPrenomClient.getText().trim());
                client.setAdresse(txtAdresseClient.getText().trim());
                client.setCodePostal(txtCodePostalClient.getText().trim());
                client.setVille(txtVilleClient.getText().trim());
                client.setTelephone(txtTelephoneClient.getText().trim());
                client.setEmail(txtEmailClient.getText().trim());
                client.setNss(txtNssClient.getText().trim());

                String dateStr = txtDateNaissanceClient.getText().trim();
                if (!dateStr.isEmpty()) {
                    client.setDateNaissance(dateStr);
                } else {
                    client.setDateNaissance("");
                }

                client.setIdTitulaireMutuelle(txtIdTitulaireClient.getText().trim());

                if (!txtIdRegimeClient.getText().trim().isEmpty()) {
                    Regime r = new Regime();
                    r.setIdRegime(Integer.parseInt(txtIdRegimeClient.getText().trim()));
                    client.setRegime(r);
                } else {
                    client.setRegime(null);
                }

                if (!txtIdMedecinClient.getText().trim().isEmpty()) {
                    Medecin m = new Medecin();
                    m.setId(Integer.parseInt(txtIdMedecinClient.getText().trim()));
                    client.setMedecin(m);
                } else {
                    client.setMedecin(null);
                }

                if (!txtIdMutuelleClient.getText().trim().isEmpty()) {
                    Mutuelle mu = new Mutuelle();
                    mu.setIdMutuelle(Integer.parseInt(txtIdMutuelleClient.getText().trim()));
                    client.setMutuelle(mu);
                } else {
                    client.setMutuelle(null);
                }

                if (clientDAO.update(client)) {
                    chargerClients();
                    viderChamps();
                    clientIdSelectionne = -1;
                    JOptionPane.showMessageDialog(this, "Client modifié avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
                }
            }
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur : un ID doit être un nombre entier.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerClient() throws SQLException, SaisieException {
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
                JOptionPane.showMessageDialog(this, "Client supprimé avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    public void chargerClients() throws SQLException, SaisieException {
        modelClient.setRowCount(0);
        List<Client> clients = clientDAO.findAll();
        for (Client c : clients) {
            modelClient.addRow(convertirClientEnRow(c));
        }
        JTableUtil.appliquerLignesAlternes(tableClient);
        JTableUtil.ajusterColonnesJTable(tableClient);
    }

    private void chargerClientDansFormulaire() throws SQLException, SaisieException {
        int row = tableClient.getSelectedRow();
        if (row != -1) {
            clientIdSelectionne = (int) modelClient.getValueAt(row, 0);
            Client c = clientDAO.findById(clientIdSelectionne);
            if (c != null) {
                txtNomClient.setText(c.getNom());
                txtPrenomClient.setText(c.getPrenom());
                txtAdresseClient.setText(c.getAdresse());
                txtCodePostalClient.setText(c.getCodePostal());
                txtVilleClient.setText(c.getVille());
                txtTelephoneClient.setText(c.getTelephone());
                txtEmailClient.setText(c.getEmail());
                txtNssClient.setText(c.getNss());
                txtDateNaissanceClient.setText(c.getDateNaissance() != null ?
                        DateTimePatternFr.formatDate(c.getDateNaissance(), "dd/MM/yyyy") : "");
                txtIdTitulaireClient.setText(c.getIdTitulaireMutuelle());
                txtIdRegimeClient.setText(c.getRegime() != null ? String.valueOf(c.getRegime().getIdRegime()) : "");
                txtIdMedecinClient.setText(c.getMedecin() != null ? String.valueOf(c.getMedecin().getId()) : "");
                txtIdMutuelleClient.setText(c.getMutuelle() != null ? String.valueOf(c.getMutuelle().getIdMutuelle()) : "");
            }
        }
    }

    private void rechercherParNom() throws SQLException, SaisieException {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) {
            chargerClients();
            return;
        }

        modelClient.setRowCount(0);
        List<Client> resultats = clientDAO.findBy("nomClient", nom, false);
        resultats.forEach(c -> modelClient.addRow(convertirClientEnRow(c)));

        JTableUtil.appliquerLignesAlternes(tableClient);
        JTableUtil.ajusterColonnesJTable(tableClient);
    }

    private void rechercherParNss() throws SQLException, SaisieException {
        String nss = txtRechercheNss.getText().trim();
        if (nss.isEmpty()) {
            chargerClients();
            return;
        }

        modelClient.setRowCount(0);
        List<Client> resultats = clientDAO.findBy("nssClient", nss, true);
        resultats.forEach(c -> modelClient.addRow(convertirClientEnRow(c)));

        JTableUtil.appliquerLignesAlternes(tableClient);
        JTableUtil.ajusterColonnesJTable(tableClient);
    }

    private void rechercherParEmail() throws SQLException, SaisieException {
        String email = txtRechercheEmail.getText().trim();
        if (email.isEmpty()) {
            chargerClients();
            return;
        }

        modelClient.setRowCount(0);
        List<Client> resultats = clientDAO.findBy("mailClient", email, true);
        resultats.forEach(c -> modelClient.addRow(convertirClientEnRow(c)));

        JTableUtil.appliquerLignesAlternes(tableClient);
        JTableUtil.ajusterColonnesJTable(tableClient);
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
        txtIdRegimeClient.setText("");
        txtIdMedecinClient.setText("");
        txtIdMutuelleClient.setText("");
        txtIdTitulaireClient.setText("");
        clientIdSelectionne = -1;
    }

    private Object[] convertirClientEnRow(Client c) {
        String nomComplet = c.getNom() + " " + c.getPrenom();
        String nomMedecin = (c.getMedecin() != null) ? c.getMedecin().getNom() + " " + c.getMedecin().getPrenom() : "";
        String nomMutuelle = (c.getMutuelle() != null) ? c.getMutuelle().getNomMutuelle() : "";
        String nomRegime = (c.getRegime() != null) ? c.getRegime().getNomRegime() : "";
        return new Object[]{
                c.getId(),
                nomComplet,
                c.getAdresse(),
                c.getCodePostal(),
                c.getVille(),
                c.getTelephone(),
                c.getEmail(),
                c.getNss(),
                c.getDateNaissance() != null ? DateTimePatternFr.formatDate(c.getDateNaissance(), "dd/MM/yyyy") : "",
                nomMedecin,
                nomMutuelle,
                nomRegime,
                c.getIdTitulaireMutuelle()
        };
    }

    private void validerChampsClient() throws SaisieException {

        if (!RegexValidator.validerMots(txtNomClient.getText()))
            throw new SaisieException("Nom invalide.");

        if (!RegexValidator.validerMots(txtPrenomClient.getText()))
            throw new SaisieException("Prénom invalide.");

        if (!RegexValidator.validerAdresse(txtAdresseClient.getText()))
            throw new SaisieException("Adresse invalide.");

        if (!RegexValidator.validerCodePostal(txtCodePostalClient.getText()))
            throw new SaisieException("Code postal invalide.");

        if (!RegexValidator.validerVille(txtVilleClient.getText()))
            throw new SaisieException("Ville invalide.");

        if (!RegexValidator.validerTelephone(txtTelephoneClient.getText()))
            throw new SaisieException("Téléphone invalide.");

        if (!RegexValidator.validerEmail(txtEmailClient.getText()))
            throw new SaisieException("Email invalide.");

        if (!RegexValidator.validerNSS(txtNssClient.getText()))
            throw new SaisieException("Numéro de Sécurité Sociale invalide.");

        String date = txtDateNaissanceClient.getText().trim();
        if (!date.isEmpty() && !RegexValidator.validerDateNaissance(date))
            throw new SaisieException("Date de naissance invalide (JJ/MM/AAAA).");
    }

}