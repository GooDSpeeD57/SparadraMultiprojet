package training.afpa.cda24060.vue.PanelSwing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import training.afpa.cda24060.ClasseDAO.ClientDAO;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;

public class PanelClient extends JPanel {

    private static final DateTimeFormatter FORMAT_FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ClientDAO clientDAO = new ClientDAO();

    private JTable tableClient;
    private DefaultTableModel modelClient;

    private JTextField txtNomClient, txtPrenomClient, txtAdresseClient, txtCodePostalClient;
    private JTextField txtVilleClient, txtTelephoneClient, txtEmailClient, txtNssClient;
    private JTextField txtDateNaissanceClient, txtIdRegimeClient, txtIdMedecinClient, txtIdMutuelleClient;
    private JTextField txtIdTitulaireClient;
    private JTextField txtRechercheNom;

    private int clientIdSelectionne = -1;

    public PanelClient() {
        initComponents();
    }

    private void initComponents() {
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
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        panelSaisieClient.add(panelBoutons, gbc);

        return panelSaisieClient;
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
        btnRechercheNom.addActionListener(e -> rechercherParNom());
        panelRecherche.add(btnRechercheNom, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerClients());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panelRecherche.add(btnAfficherTous, gbc);

        return panelRecherche;
    }

    private void creerTable() {
        String[] colonnes = {"ID","Nom", "Prénom", "Adresse", "Code Postal", "Ville",
                "Téléphone", "Email", "NSS", "Date Naissance", "Médecin", "Mutuelle",
                "Régime"
        };

        modelClient = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableClient = new JTable(modelClient);
        tableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Masquer ID
        tableClient.getColumnModel().getColumn(0).setMinWidth(0);
        tableClient.getColumnModel().getColumn(0).setMaxWidth(0);
        tableClient.getColumnModel().getColumn(0).setWidth(0);

        // Alternance couleurs
        tableClient.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

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
            Client client = new Client();
            client.setNom(txtNomClient.getText().trim());
            client.setPrenom(txtPrenomClient.getText().trim());
            client.setAdresse(txtAdresseClient.getText().trim());
            client.setCodePostal(txtCodePostalClient.getText().trim());
            client.setVille(txtVilleClient.getText().trim());
            client.setTelephone(txtTelephoneClient.getText().trim());
            client.setEmail(txtEmailClient.getText().trim());
            client.setNss(txtNssClient.getText().trim());
            client.setDateNaissance(txtDateNaissanceClient.getText().trim());
            client.setIdTitulaireMutuelle(txtIdTitulaireClient.getText().trim());

            if (!txtIdRegimeClient.getText().trim().isEmpty()) {
                Regime r = new Regime();
                r.setIdRegime(Integer.parseInt(txtIdRegimeClient.getText().trim()));
                client.setRegime(r);
            }

            if (!txtIdMedecinClient.getText().trim().isEmpty()) {
                Medecin m = new Medecin();
                m.setIdMedecin(Integer.parseInt(txtIdMedecinClient.getText().trim()));
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
                client.setDateNaissance(txtDateNaissanceClient.getText().trim());
                client.setIdTitulaireMutuelle(txtIdTitulaireClient.getText().trim());

                if (!txtIdRegimeClient.getText().trim().isEmpty()) {
                    Regime r = new Regime();
                    r.setIdRegime(Integer.parseInt(txtIdRegimeClient.getText().trim()));
                    client.setRegime(r);
                }
                if (!txtIdMedecinClient.getText().trim().isEmpty()) {
                    Medecin m = new Medecin();
                    m.setIdMedecin(Integer.parseInt(txtIdMedecinClient.getText().trim()));
                    client.setMedecin(m);
                }
                if (!txtIdMutuelleClient.getText().trim().isEmpty()) {
                    Mutuelle mu = new Mutuelle();
                    mu.setIdMutuelle(Integer.parseInt(txtIdMutuelleClient.getText().trim()));
                    client.setMutuelle(mu);
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
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
                JOptionPane.showMessageDialog(this, "Client supprimé avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    public void chargerClients() {
        modelClient.setRowCount(0);
        List<Client> clients = clientDAO.findAll();
        for (Client c : clients) {
            String nomMedecin = (c.getMedecin() != null) ? c.getMedecin().getNom() + " " + c.getMedecin().getPrenom() : "";
            String nomMutuelle = (c.getMutuelle() != null) ? c.getMutuelle().getNomMutuelle() : "";
            String nomRegime = (c.getRegime() != null) ? c.getRegime().getNomRegime() : "";
            String dateNaissance = (c.getDateNaissance() != null) ? c.getDateNaissance().format(FORMAT_FR) : "";

            modelClient.addRow(new Object[]{
                    c.getIdClient(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getAdresse(),
                    c.getCodePostal(),
                    c.getVille(),
                    c.getTelephone(),
                    c.getEmail(),
                    c.getNss(),
                    dateNaissance,
                    nomMedecin,
                    nomMutuelle,
                    nomRegime
            });
        }

        ajusterColonnesJTable(); // <-- ajuste automatiquement les colonnes
    }

    private void chargerClientDansFormulaire() {
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
                txtDateNaissanceClient.setText(c.getDateNaissance() != null ? c.getDateNaissance().format(FORMAT_FR) : "");
                txtIdTitulaireClient.setText(c.getIdTitulaireMutuelle());
                txtIdRegimeClient.setText(c.getRegime() != null ? String.valueOf(c.getRegime().getIdRegime()) : "");
                txtIdMedecinClient.setText(c.getMedecin() != null ? String.valueOf(c.getMedecin().getIdMedecin()) : "");
                txtIdMutuelleClient.setText(c.getMutuelle() != null ? String.valueOf(c.getMutuelle().getIdMutuelle()) : "");
            }
        }
    }

    private void rechercherParNom() {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) {
            chargerClients();
            return;
        }
        modelClient.setRowCount(0);
        List<Client> resultats = clientDAO.findByNom(nom);
        for (Client c : resultats) {
            String nomMedecin = (c.getMedecin() != null) ? c.getMedecin().getNom() + " " + c.getMedecin().getPrenom() : "";
            String nomMutuelle = (c.getMutuelle() != null) ? c.getMutuelle().getNomMutuelle() : "";
            String nomRegime = (c.getRegime() != null) ? c.getRegime().getNomRegime() : "";
            String dateNaissance = (c.getDateNaissance() != null) ? c.getDateNaissance().format(FORMAT_FR) : "";
            modelClient.addRow(new Object[]{
                    c.getIdClient(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getAdresse(),
                    c.getCodePostal(),
                    c.getVille(),
                    c.getTelephone(),
                    c.getEmail(),
                    c.getNss(),
                    dateNaissance,
                    nomMedecin,
                    nomMutuelle,
                    nomRegime
            });
        }

        ajusterColonnesJTable(); // <-- ajuste aussi après recherche
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


    private static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        private final Color lightGray = new Color(58, 198, 38);
        private final Color white = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? white : lightGray);
            } else {
                c.setBackground(new Color(184, 207, 229));
            }
            return c;
        }
    }
    private void ajusterColonnesJTable() {
        final int PADDING = 10; // 10 pixels (5 gauche + 5 droite)
        for (int col = 0; col < tableClient.getColumnCount(); col++) {
            if (col == 0) continue; // Ignorer l'ID, on veut le garder masqué

            int maxWidth = 0;

            TableCellRenderer headerRenderer = tableClient.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tableClient, tableClient.getColumnName(col), false, false, 0, col);
            maxWidth = headerComp.getPreferredSize().width;


            for (int row = 0; row < tableClient.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tableClient.getCellRenderer(row, col);
                Component c = tableClient.prepareRenderer(cellRenderer, row, col);
                maxWidth = Math.max(maxWidth, c.getPreferredSize().width);
            }

            maxWidth += PADDING;

            tableClient.getColumnModel().getColumn(col).setPreferredWidth(maxWidth);
        }

        tableClient.getColumnModel().getColumn(0).setMinWidth(0);
        tableClient.getColumnModel().getColumn(0).setMaxWidth(0);
        tableClient.getColumnModel().getColumn(0).setWidth(0);
        tableClient.revalidate();
        tableClient.repaint();
    }
}