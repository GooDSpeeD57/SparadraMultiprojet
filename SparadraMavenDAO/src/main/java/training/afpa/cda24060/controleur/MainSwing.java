package training.afpa.cda24060.controleur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import training.afpa.cda24060.ClasseDAO.ClientDAO;
import training.afpa.cda24060.ClasseDAO.MedecinDAO;
import training.afpa.cda24060.ClasseDAO.MedicamentDAO;
import training.afpa.cda24060.ClasseDAO.MutuelleDAO;
import training.afpa.cda24060.modele.*;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Prescription;

public class MainSwing extends JFrame {

    private static final String FICHIER_PERSISTANCE = "donnees.bin";
    private static Map<String, Object> donnees;


    private JTabbedPane tabbedPane;
    private JPanel panelAccueil, panelClient, panelMedecin, panelMutuelle, panelOrdonnance, panelMedicament, panelFacturation;

    private JTable tableClient, tableMedecin, tableMutuelle, tableMedicament, tableFacturation, tableOrdonnance;
    private DefaultTableModel modelClient, modelMedecin, modelMutuelle, modelOrdonnance, modelMedicament, modelFacturation;

    private JTextField txtNomClient, txtPrenomClient, txtAdresseClient, txtCodePostalClient, txtVilleClient, txtTelephoneClient;
    private JTextField txtEmailClient, txtNssClient, txtDateNaissanceClient, txtMutuelleClient, txtMedecinrefClient;

    private JTextField txtNomMedecin, txtPrenomMedecin, txtAdresseMedecin, txtCodePostalMedecin, txtVilleMedecin;
    private JTextField txtTelephoneMedecin, txtEmailMedecin, txtRPPSMedecin;

    private JTextField txtNomMutuelle, txtAdresseMutuelle, txtCodePostalMutuelle, txtVilleMutuelle;
    private JTextField txtTelephoneMutuelle, txtEmailMutuelle, txtDepartementMutuelle, txtTauxRbMutuelle;


    private JTextField txtNomMedicament, txtCategoriMedicament, txtPrixMedicament, txtDateMiseCirculation, txtQuantiteMedicament;
    private JComboBox<String> cbSansOrdonnanceMedicament;


    private JTextField txtRechercheNom, txtRechercheNss, txtRechercheEmail, txtRechercheRpps, txtRechercheDepartement;
    private JTextField txtRechercheNomMedicament, txtRechercheCategorieMedicament;

    public MainSwing() {
        initComponents();

    }

    private void initComponents() {
        setTitle("Système de Gestion de Pharmacie");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sauvegarderEtQuitter();
            }
        });


        creerMenu();

        // Création des onglets
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

        creerPanelAccueil();
        creerPanelClient();
        creerPanelMedecin();
        creerPanelMutuelle();
        creerPanelMedicament();
        creerPanelOrdonnance();
        creerPanelFacturation();

        tabbedPane.addTab("Accueil", panelAccueil);
        tabbedPane.addTab("Clients", panelClient);
        tabbedPane.addTab("Médecins", panelMedecin);
        tabbedPane.addTab("Mutuelles", panelMutuelle);
        tabbedPane.addTab("Médicaments", panelMedicament);
        tabbedPane.addTab("Ordonnances", panelOrdonnance);
        tabbedPane.addTab("Historique", panelFacturation);

        add(tabbedPane);
    }

    private void creerMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder");
        JMenuItem itemQuitter = new JMenuItem("Quitter");

        itemSauvegarder.addActionListener(e -> sauvegarder());
        itemQuitter.addActionListener(e -> sauvegarderEtQuitter());

        menuFichier.add(itemSauvegarder);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        JMenu menuAide = new JMenu("Aide");
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Système de Gestion de Pharmacie\nVersion 1.0\n\nDéveloppé avec Java Swing Par Julien Taesch",
                        "À propos",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        menuAide.add(itemAPropos);
        menuBar.add(menuFichier);
        menuBar.add(menuAide);
        setJMenuBar(menuBar);
    }
    private void creerPanelAccueil() {
        ImageIcon backgroundIcon = new ImageIcon(
                getClass().getResource("/test.png")
        );
        Image backgroundImage = backgroundIcon.getImage();

        panelAccueil = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessiner l'image pour couvrir tout le panneau
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelAccueil.setOpaque(false);

        JPanel panelBienvenue = new JPanel();
        panelBienvenue.setOpaque(false);
        panelBienvenue.setLayout(new BoxLayout(panelBienvenue, BoxLayout.Y_AXIS));

        JLabel lblTitre = new JLabel("Bienvenue dans le Système de Pharmacie");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitre.setForeground(new Color(34, 89, 7));

        JLabel lblSousTitre = new JLabel("Gestion complète de votre Pharmacie");
        lblSousTitre.setFont(new Font("Arial", Font.ITALIC, 16));
        lblSousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSousTitre.setForeground(new Color(99, 180, 70));

        panelBienvenue.add(Box.createVerticalGlue());
        panelBienvenue.add(lblTitre);
        panelBienvenue.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBienvenue.add(lblSousTitre);
        panelBienvenue.add(Box.createVerticalGlue());


        JPanel panelStats = new JPanel(new GridLayout(2, 2, 10, 10));
        panelStats.setOpaque(false);
        panelStats.setBorder(new TitledBorder("Statistiques"));


        JLabel lblNbClient = new JLabel("Clients : " + new ClientDAO().countClients(), SwingConstants.CENTER);
        JLabel lblNbMedicament = new JLabel("Médicaments : " + new MedicamentDAO().countMedicaments(), SwingConstants.CENTER);
        JLabel lblNbMedecin = new JLabel("Médecins : " + new MedecinDAO().countMedecins(), SwingConstants.CENTER);
        JLabel lblNbMutuelle = new JLabel("Mutuelles : " + new MutuelleDAO().countMutuelles(), SwingConstants.CENTER);


        Font fontStats = new Font("Arial", Font.BOLD, 14);
        lblNbClient.setFont(fontStats);
        lblNbMedicament.setFont(fontStats);
        lblNbMedecin.setFont(fontStats);
        lblNbMutuelle.setFont(fontStats);

        lblNbClient.setForeground(Color.BLACK);
        lblNbMedicament.setForeground(Color.BLACK);
        lblNbMedecin.setForeground(Color.BLACK);
        lblNbMutuelle.setForeground(Color.BLACK);

        panelStats.add(lblNbClient);
        panelStats.add(lblNbMedicament);
        panelStats.add(lblNbMedecin);
        panelStats.add(lblNbMutuelle);

        panelAccueil.add(panelBienvenue, BorderLayout.CENTER);
        panelAccueil.add(panelStats, BorderLayout.SOUTH);
    }

    private void creerPanelClient() {
        panelClient = new JPanel(new BorderLayout());

        // Panel de saisie
        JPanel panelSaisieClient = new JPanel(new GridBagLayout());
        panelSaisieClient.setBorder(new TitledBorder("Nouveau Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs de saisie
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieClient.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNomClient = new JTextField(15);
        panelSaisieClient.add(txtNomClient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPrenomClient = new JTextField(15);
        panelSaisieClient.add(txtPrenomClient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("Adresse :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAdresseClient = new JTextField(15);
        panelSaisieClient.add(txtAdresseClient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieClient.add(new JLabel("Code Postal :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCodePostalClient = new JTextField(15);
        panelSaisieClient.add(txtCodePostalClient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("Ville :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtVilleClient = new JTextField(15);
        panelSaisieClient.add(txtVilleClient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieClient.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTelephoneClient = new JTextField(15);
        panelSaisieClient.add(txtTelephoneClient, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("Email :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmailClient = new JTextField(15);
        panelSaisieClient.add(txtEmailClient, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("N° Séc. Sociale :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNssClient = new JTextField(15);
        panelSaisieClient.add(txtNssClient, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieClient.add(new JLabel("Date de Naissance :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtDateNaissanceClient = new JTextField(15);
        panelSaisieClient.add(txtDateNaissanceClient, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("Mutuelle :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtMutuelleClient = new JTextField(15);
        panelSaisieClient.add(txtMutuelleClient, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panelSaisieClient.add(new JLabel("Médecin Référent :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtMedecinrefClient = new JTextField(15);
        panelSaisieClient.add(txtMedecinrefClient, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e-> modifierClient());
        btnSupprimer.addActionListener(e->supprimerClient());
        btnVider.addActionListener(e -> viderChampsClient());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        panelSaisieClient.add(panelBoutons, gbc);

        // Panel de recherche
        JPanel panelRechercheClient = new JPanel(new GridBagLayout());
        panelRechercheClient.setBorder(new TitledBorder("Recherche"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelRechercheClient.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNom = new JTextField(15);
        panelRechercheClient.add(txtRechercheNom, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherClientParNom());
        panelRechercheClient.add(btnRechercheNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRechercheClient.add(new JLabel("Par email:"), gbc);
        gbc.gridx = 1;
        txtRechercheEmail = new JTextField(15);
        panelRechercheClient.add(txtRechercheEmail, gbc);
        gbc.gridx = 2;
        JButton btnRechercheEmail = new JButton("🔍");
        btnRechercheEmail.addActionListener(e -> rechercherClientParEmail());
        panelRechercheClient.add(btnRechercheEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRechercheClient.add(new JLabel("Par NSS:"), gbc);
        gbc.gridx = 1;
        txtRechercheNss = new JTextField(15);
        panelRechercheClient.add(txtRechercheNss, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNss = new JButton("🔍");
        btnRechercheNss.addActionListener(e -> rechercherClientParNss());
        panelRechercheClient.add(btnRechercheNss, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerClient());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panelRechercheClient.add(btnAfficherTous, gbc);

        // Panel supérieur
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieClient);
        panelSuperior.add(panelRechercheClient);

        // Table des clients
        String[] colonnesClient = {"Nom", "Prénom", "Adresse","Code Postal","Ville","Téléphone","Email",
                "N° Séc. Sociale","Date Naissance","Mutuelle","Médecin Réf"};
        modelClient = new DefaultTableModel(colonnesClient, 0) {
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
            }
        });
        JScrollPane scrollClient = new JScrollPane(tableClient);
        scrollClient.setBorder(new TitledBorder("Liste des Clients"));

        panelClient.add(panelSuperior, BorderLayout.NORTH);
        panelClient.add(scrollClient, BorderLayout.CENTER);
    }

    private void creerPanelMedecin() {
        panelMedecin = new JPanel(new BorderLayout());

        // Panel de saisie
        JPanel panelSaisieMedecin = new JPanel(new GridBagLayout());
        panelSaisieMedecin.setBorder(new TitledBorder("Nouveau Médecin"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs de saisie
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieMedecin.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNomMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtNomMedecin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedecin.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPrenomMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtPrenomMedecin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedecin.add(new JLabel("Adresse :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAdresseMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtAdresseMedecin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieMedecin.add(new JLabel("Code Postal :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCodePostalMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtCodePostalMedecin, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedecin.add(new JLabel("Ville :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtVilleMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtVilleMedecin, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelSaisieMedecin.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTelephoneMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtTelephoneMedecin, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedecin.add(new JLabel("Email :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmailMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtEmailMedecin, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedecin.add(new JLabel("N° RPPS :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtRPPSMedecin = new JTextField(15);
        panelSaisieMedecin.add(txtRPPSMedecin, gbc);

        JPanel panelBoutonsMedecin = new JPanel(new FlowLayout());

        JButton btnAjouterMedecin = new JButton("Ajouter");
        JButton btnModifierMedecin = new JButton("Modifier");
        JButton btnSupprimerMedecin = new JButton("Supprimer");
        JButton btnViderMedecin = new JButton("Vider");

        btnAjouterMedecin.addActionListener(e -> ajouterMedecin());
        btnModifierMedecin.addActionListener(e -> modifierMedecin());
        btnSupprimerMedecin.addActionListener(e -> supprimerMedecin());
        btnViderMedecin.addActionListener(e -> viderChampsMedecin());

        panelBoutonsMedecin.add(btnAjouterMedecin);
        panelBoutonsMedecin.add(btnModifierMedecin);
        panelBoutonsMedecin.add(btnSupprimerMedecin);
        panelBoutonsMedecin.add(btnViderMedecin);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        panelSaisieMedecin.add(panelBoutonsMedecin, gbc);

        // Panel de recherche
        JPanel panelRechercheMedecin = new JPanel(new GridBagLayout());
        panelRechercheMedecin.setBorder(new TitledBorder("Recherche"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelRechercheMedecin.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        JTextField txtRechercheNomMedecin = new JTextField(15);
        panelRechercheMedecin.add(txtRechercheNomMedecin, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNomMedecin = new JButton("🔍");
        btnRechercheNomMedecin.addActionListener(e -> rechercherMedecinParNom(txtRechercheNomMedecin.getText()));
        panelRechercheMedecin.add(btnRechercheNomMedecin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRechercheMedecin.add(new JLabel("Par RPPS:"), gbc);
        gbc.gridx = 1;
        txtRechercheRpps = new JTextField(15);
        panelRechercheMedecin.add(txtRechercheRpps, gbc);
        gbc.gridx = 2;
        JButton btnRechercheRpps = new JButton("🔍");
        btnRechercheRpps.addActionListener(e -> rechercherMedecinParRpps());
        panelRechercheMedecin.add(btnRechercheRpps, gbc);

        JButton btnAfficherTousMedecins = new JButton("Afficher tous");
        btnAfficherTousMedecins.addActionListener(e -> chargerMedecin());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panelRechercheMedecin.add(btnAfficherTousMedecins, gbc);

        JPanel panelSuperiorMedecin = new JPanel(new GridLayout(1, 2));
        panelSuperiorMedecin.add(panelSaisieMedecin);
        panelSuperiorMedecin.add(panelRechercheMedecin);

        // Table des Médecins
        String[] colonnesMedecins = {"Nom", "Prénom", "Adresse", "Code Postal", "Ville", "Téléphone", "Email", "RPPS"};
        modelMedecin = new DefaultTableModel(colonnesMedecins, 0) {
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
            }
        });
        JScrollPane scrollMedecin = new JScrollPane(tableMedecin);
        scrollMedecin.setBorder(new TitledBorder("Liste des Médecins"));

        panelMedecin.add(panelSuperiorMedecin, BorderLayout.NORTH);
        panelMedecin.add(scrollMedecin, BorderLayout.CENTER);
    }

        private void creerPanelMutuelle() {
            panelMutuelle = new JPanel(new BorderLayout());

            JPanel panelSaisieMutuelle = new JPanel(new GridBagLayout());
            panelSaisieMutuelle.setBorder(new TitledBorder("Nouvelle Mutuelle"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            panelSaisieMutuelle.add(new JLabel("Nom :"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtNomMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtNomMutuelle, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE;
            panelSaisieMutuelle.add(new JLabel("Adresse :"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtAdresseMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtAdresseMutuelle, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            panelSaisieMutuelle.add(new JLabel("Code Postal :"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtCodePostalMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtCodePostalMutuelle, gbc);

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE;
            panelSaisieMutuelle.add(new JLabel("Ville :"), gbc);
            gbc.gridx = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtVilleMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtVilleMutuelle, gbc);

            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE;
            panelSaisieMutuelle.add(new JLabel("Téléphone :"), gbc);
            gbc.gridx = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtTelephoneMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtTelephoneMutuelle, gbc);

            gbc.gridx = 2;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            panelSaisieMutuelle.add(new JLabel("Email :"), gbc);
            gbc.gridx = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtEmailMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtEmailMutuelle, gbc);

            gbc.gridx = 2;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.NONE;
            panelSaisieMutuelle.add(new JLabel("Département :"), gbc);
            gbc.gridx = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtDepartementMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtDepartementMutuelle, gbc);

            gbc.gridx = 2;
            gbc.gridy = 4;
            gbc.fill = GridBagConstraints.NONE;
            panelSaisieMutuelle.add(new JLabel("Taux Remboursement (%) :"), gbc);
            gbc.gridx = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            txtTauxRbMutuelle = new JTextField(15);
            panelSaisieMutuelle.add(txtTauxRbMutuelle, gbc);

            JPanel panelBoutonsMutuelle = new JPanel(new FlowLayout());

            JButton btnAjouterMutuelle = new JButton("Ajouter");
            JButton btnModifierMutuelle = new JButton("Modifier");
            JButton btnSupprimerMutuelle = new JButton("Supprimer");
            JButton btnViderMutuelle = new JButton("Vider");

            btnAjouterMutuelle.addActionListener(e -> ajouterMutuelle());
            btnModifierMutuelle.addActionListener(e -> modifierMutuelle());
            btnSupprimerMutuelle.addActionListener(e -> supprimerMutuelle());
            btnViderMutuelle.addActionListener(e -> viderChampsMutuelle());

            panelBoutonsMutuelle.add(btnAjouterMutuelle);
            panelBoutonsMutuelle.add(btnModifierMutuelle);
            panelBoutonsMutuelle.add(btnSupprimerMutuelle);
            panelBoutonsMutuelle.add(btnViderMutuelle);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 4;
            panelSaisieMutuelle.add(panelBoutonsMutuelle, gbc);

            JPanel panelRechercheMutuelle = new JPanel(new GridBagLayout());
            panelRechercheMutuelle.setBorder(new TitledBorder("Recherche"));

            gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panelRechercheMutuelle.add(new JLabel("Par nom:"), gbc);
            gbc.gridx = 1;
            JTextField txtRechercheNomMutuelle = new JTextField(15);
            panelRechercheMutuelle.add(txtRechercheNomMutuelle, gbc);
            gbc.gridx = 2;
            JButton btnRechercheNomMutuelle = new JButton("🔍");
            btnRechercheNomMutuelle.addActionListener(e -> rechercherMutuelleParNom(txtRechercheNomMutuelle.getText()));
            panelRechercheMutuelle.add(btnRechercheNomMutuelle, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panelRechercheMutuelle.add(new JLabel("Par département:"), gbc);
            gbc.gridx = 1;
            txtRechercheDepartement = new JTextField(15);
            panelRechercheMutuelle.add(txtRechercheDepartement, gbc);
            gbc.gridx = 2;
            JButton btnRechercheDepartement = new JButton("🔍");
            btnRechercheDepartement.addActionListener(e -> rechercherMutuelleParDepartement());
            panelRechercheMutuelle.add(btnRechercheDepartement, gbc);

            JButton btnAfficherToutesMutuelles = new JButton("Afficher toutes");
            btnAfficherToutesMutuelles.addActionListener(e -> chargerMutuelle());
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 3;
            panelRechercheMutuelle.add(btnAfficherToutesMutuelles, gbc);

            JPanel panelSuperiorMutuelle = new JPanel(new GridLayout(1, 2));
            panelSuperiorMutuelle.add(panelSaisieMutuelle);
            panelSuperiorMutuelle.add(panelRechercheMutuelle);

            // Table des Mutuelles
            String[] colonnesMutuelle = {"Nom", "Adresse", "Code Postal", "Ville", "Téléphone", "Email", "Département", "Taux Remb."};
            modelMutuelle = new DefaultTableModel(colonnesMutuelle, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tableMutuelle = new JTable(modelMutuelle);
            tableMutuelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableMutuelle.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = tableMutuelle.getSelectedRow();
                        if (row != -1) {
                            Mutuelle m = Mutuelle.getMutuelles().get(row); // adapte selon ta classe Mutuelle
                            txtNomMutuelle.setText(m.getNom());
                            txtAdresseMutuelle.setText(m.getAdresse());
                            txtCodePostalMutuelle.setText(m.getCodePostal());
                            txtVilleMutuelle.setText(m.getVille());
                            txtTelephoneMutuelle.setText(m.getTelephone());
                            txtEmailMutuelle.setText(m.getEmail());
                            txtDepartementMutuelle.setText(m.getDepartement());
                            txtTauxRbMutuelle.setText(String.valueOf(m.getTRemboursement()));
                        }
                    }
                }
            });

            JScrollPane scrollMutuelle = new JScrollPane(tableMutuelle);
            scrollMutuelle.setBorder(new TitledBorder("Liste des Mutuelles"));

            panelMutuelle.add(panelSuperiorMutuelle, BorderLayout.NORTH);
            panelMutuelle.add(scrollMutuelle, BorderLayout.CENTER);

        }
            private void creerPanelMedicament() {
        panelMedicament = new JPanel(new BorderLayout());

        // Panel de saisie
        JPanel panelSaisieMedicament = new JPanel(new GridBagLayout());
        panelSaisieMedicament.setBorder(new TitledBorder("Nouveau Médicament"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs de saisie
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panelSaisieMedicament.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNomMedicament = new JTextField(15);
        panelSaisieMedicament.add(txtNomMedicament, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedicament.add(new JLabel("Catégorie :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCategoriMedicament = new JTextField(15);
        panelSaisieMedicament.add(txtCategoriMedicament, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panelSaisieMedicament.add(new JLabel("Prix (€) :"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPrixMedicament = new JTextField(15);
        panelSaisieMedicament.add(txtPrixMedicament, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedicament.add(new JLabel("Date mise en circulation :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtDateMiseCirculation = new JTextField(15);
        panelSaisieMedicament.add(txtDateMiseCirculation, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panelSaisieMedicament.add(new JLabel("Quantité :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtQuantiteMedicament = new JTextField(15);
        panelSaisieMedicament.add(txtQuantiteMedicament, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelSaisieMedicament.add(new JLabel("Sans ordonnance :"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] options = {"Oui", "Non"};
        cbSansOrdonnanceMedicament = new JComboBox<>(options);
        panelSaisieMedicament.add(cbSansOrdonnanceMedicament, gbc);

        JPanel panelBoutonsMedicament = new JPanel(new FlowLayout());

        JButton btnAjouterMedicament = new JButton("Ajouter");
        JButton btnModifierMedicament = new JButton("Modifier");
        JButton btnSupprimerMedicament = new JButton("Supprimer");
        JButton btnViderMedicament = new JButton("Vider");

        btnAjouterMedicament.addActionListener(e -> ajouterMedicament());
        btnModifierMedicament.addActionListener(e -> modifierMedicament());
        btnSupprimerMedicament.addActionListener(e -> supprimerMedicament());
        btnViderMedicament.addActionListener(e -> viderChampsMedicament());

                panelBoutonsMedicament.add(btnAjouterMedicament);
                panelBoutonsMedicament.add(btnModifierMedicament);
                panelBoutonsMedicament.add(btnSupprimerMedicament);
                panelBoutonsMedicament.add(btnViderMedicament);


                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelSaisieMedicament.add(panelBoutonsMedicament, gbc);

        // Panel de recherche
        JPanel panelRechercheMedicament = new JPanel(new GridBagLayout());
        panelRechercheMedicament.setBorder(new TitledBorder("Recherche"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panelRechercheMedicament.add(new JLabel("Par nom:"), gbc);
        gbc.gridx = 1;
        txtRechercheNomMedicament = new JTextField(15);
        panelRechercheMedicament.add(txtRechercheNomMedicament, gbc);
        gbc.gridx = 2;
        JButton btnRechercheNomMedicament = new JButton("🔍");
        btnRechercheNomMedicament.addActionListener(e -> rechercherMedicamentParNom());
        panelRechercheMedicament.add(btnRechercheNomMedicament, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelRechercheMedicament.add(new JLabel("Par catégorie:"), gbc);
        gbc.gridx = 1;
        txtRechercheCategorieMedicament = new JTextField(15);
        panelRechercheMedicament.add(txtRechercheCategorieMedicament, gbc);
        gbc.gridx = 2;
        JButton btnRechercheCategorieMedicament = new JButton("🔍");
        btnRechercheCategorieMedicament.addActionListener(e -> rechercherMedicamentParCategorie());
        panelRechercheMedicament.add(btnRechercheCategorieMedicament, gbc);

        JButton btnAfficherTousMedicaments = new JButton("Afficher tous");
        btnAfficherTousMedicaments.addActionListener(e -> chargerMedicament());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        panelRechercheMedicament.add(btnAfficherTousMedicaments, gbc);

        JPanel panelSuperiorMedicament = new JPanel(new GridLayout(1, 2));
        panelSuperiorMedicament.add(panelSaisieMedicament);
        panelSuperiorMedicament.add(panelRechercheMedicament);

        String[] colonnesMedicament = {"Nom", "Catégorie", "Prix", "Date Circulation", "Quantité", "Sans Ordonnance"};
        modelMedicament = new DefaultTableModel(colonnesMedicament, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMedicament = new JTable(modelMedicament);
        tableMedicament.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMedicament.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = tableMedicament.getSelectedRow();
                        if (row != -1) {
                                Medicament med = Medicament.getMedicaments().get(row);
                                txtNomMedicament.setText(med.getNomMedicament());
                                txtCategoriMedicament.setText(med.getCategorieMedicament());
                                txtPrixMedicament.setText(String.valueOf(med.getPrixMedicament()));
                                txtDateMiseCirculation.setText(med.getDateMiseEnCirculation());
                                txtQuantiteMedicament.setText(String.valueOf(med.getQuantiteMedicament()));
                                cbSansOrdonnanceMedicament.setSelectedItem(med.getSansOrdonnanceMedicament());
                            }
                        }
                    }
                });

        JScrollPane scrollMedicament = new JScrollPane(tableMedicament);
        scrollMedicament.setBorder(new TitledBorder("Liste des Médicaments"));

        panelMedicament.add(panelSuperiorMedicament, BorderLayout.NORTH);
        panelMedicament.add(scrollMedicament, BorderLayout.CENTER);
    }

    private void creerPanelOrdonnance() {
        panelOrdonnance = new JPanel(new BorderLayout());

        // --- Panel haut (boutons) ---
        JPanel panelCreationOrdonnance = new JPanel(new FlowLayout());
        panelCreationOrdonnance.setBorder(new TitledBorder("➕ Nouvelle Ordonnance"));

        JButton btnNouvelleOrdo = new JButton("\uD83D\uDCCB Créer une ordonnance");
        btnNouvelleOrdo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnNouvelleOrdo.addActionListener(e -> ouvrirDialogueCreationOrdonnance());

        JButton btnActualiserOrdo = new JButton("🔄 Actualiser");
        btnActualiserOrdo.addActionListener(e -> chargerOrdonnances());

        JButton btnVoirMedicaments = new JButton("📦 Voir les médicaments");
        btnVoirMedicaments.addActionListener(e -> {
            int selectedRow = tableOrdonnance.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ordonnance.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Récupération de l'ordonnance depuis la liste globale
            Ordonnance ordonnance = Ordonnance.getOrdonnances().get(selectedRow);
            afficherMedicamentsDeLOrdonnance(ordonnance);
        });

        panelCreationOrdonnance.add(btnNouvelleOrdo);
        panelCreationOrdonnance.add(btnActualiserOrdo);
        panelCreationOrdonnance.add(btnVoirMedicaments);

        String[] colonnesOrdo = {"Client", "Médecin", "Date", "Nb Médicaments"};
        modelOrdonnance = new DefaultTableModel(colonnesOrdo, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableOrdonnance = new JTable(modelOrdonnance);
        tableOrdonnance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollOrdonnance = new JScrollPane(tableOrdonnance);
        scrollOrdonnance.setBorder(new TitledBorder("\uD83D\uDCCB Liste des Ordonnances"));

        panelOrdonnance.add(panelCreationOrdonnance, BorderLayout.NORTH);
        panelOrdonnance.add(scrollOrdonnance, BorderLayout.CENTER);

        // Charger les ordonnances au démarrage
        chargerOrdonnances();
    }
    private void afficherMedicamentsDeLOrdonnance(Ordonnance ordonnance) {
        JDialog dialog = new JDialog(this, "📦 Médicaments de l'ordonnance", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        String[] colonnes = {"Nom", "Quantité", "Prix Unitaire (€)", "Prix Total (€)"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        for (Prescription p : ordonnance.getPrescriptions()) {
            model.addRow(new Object[]{
                    p.getNomMedicament(),
                    p.getQuantitePrescrite(),
                    String.format("%.2f", p.getPrixUnitaire()),
                    String.format("%.2f", p.getPrixTotal())
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }


    private void creerPanelFacturation() {
        panelFacturation = new JPanel(new BorderLayout());

        JPanel panelControles = new JPanel(new FlowLayout());
        JButton btnActualiserFacturation = new JButton("🔄 Actualiser");
        btnActualiserFacturation.addActionListener(e -> chargerFacturation());

        JButton btnViderHistorique = new JButton("🗑️ Vider l'historique");
        btnViderHistorique.addActionListener(e -> viderFacturation());

        panelControles.add(btnActualiserFacturation);
        panelControles.add(btnViderHistorique);

        // Table de l'historique
        String[] colonnesFacturation = {"Date", "Action", "Utilisateur", "Détails"};
        modelFacturation = new DefaultTableModel(colonnesFacturation, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableFacturation = new JTable(modelFacturation);
        JScrollPane scrollFacturation = new JScrollPane(tableFacturation);
        scrollFacturation.setBorder(new TitledBorder("\uD83D\uDCCB Historique des Actions"));

        panelFacturation.add(panelControles, BorderLayout.NORTH);
        panelFacturation.add(scrollFacturation, BorderLayout.CENTER);
    }

    // Méthodes de chargement des données
    private void chargerDonneesDansGUI() {
        chargerClient();
        chargerMedecin();
        chargerMutuelle();
        chargerMedicament();
        chargerOrdonnances();
        chargerFacturation();
    }

    private void chargerClient() {
        modelClient.setRowCount(0);
        for (Client client : Client.getClients()) {
            modelClient.addRow(new Object[]{
                    client.getNom(),
                    client.getPrenom(),
                    client.getAdresse(),
                    client.getCodePostal(),
                    client.getVille(),
                    client.getTelephone(),
                    client.getEmail(),
                    client.getNss(),
                    client.getDateNaissance(),
                    client.getMutuelle(),
                    client.getMedecinRef()
            });
        }
    }

    private void chargerMedecin() {
        modelMedecin.setRowCount(0);
        for (Medecin medecin : Medecin.getMedecins()) {
            modelMedecin.addRow(new Object[]{
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

    private void chargerMutuelle() {
        modelMutuelle.setRowCount(0);
        for (Mutuelle mutuelle : Mutuelle.getMutuelles()) {
            modelMutuelle.addRow(new Object[]{
                    mutuelle.getNom(),
                    mutuelle.getAdresse(),
                    mutuelle.getCodePostal(),
                    mutuelle.getVille(),
                    mutuelle.getTelephone(),
                    mutuelle.getEmail(),
                    mutuelle.getDepartement(),
                    mutuelle.getTRemboursement() + "%"
            });
        }
    }

    private void chargerMedicament() {
        modelMedicament.setRowCount(0);
        for (Medicament medicament : Medicament.getMedicaments()) {
            modelMedicament.addRow(new Object[]{
                    medicament.getNomMedicament(),
                    medicament.getCategorieMedicament(),
                    medicament.getPrixMedicament() + " €",
                    medicament.getDateMiseEnCirculation(),
                    medicament.getQuantiteMedicament(),
                    medicament.getSansOrdonnanceMedicament()
            });
        }
    }

    private void chargerOrdonnances() {
        modelOrdonnance.setRowCount(0);
        for (Ordonnance ordonnance : Ordonnance.getOrdonnances()) {
            modelOrdonnance.addRow(new Object[]{
                    ordonnance.getClient().getNom() + " " + ordonnance.getClient().getPrenom(),
                    ordonnance.getMedecin().getNom() + " " + ordonnance.getMedecin().getPrenom(),
                    ordonnance.getDateOrdonnanceFormatee(),
                    ordonnance.getPrescriptions().size()
            });
        }
    }

    private void chargerFacturation() {
        modelFacturation.setRowCount(0);
        // Cette méthode sera complétée avec la gestion de l'historique
        // Pour l'instant, on ajoute juste un placeholder
        modelFacturation.addRow(new Object[]{new java.util.Date(), "Application démarrée", "Système", "Chargement des données"});
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
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new Medecin(nom, prenom, adresse, codePostal, ville, telephone, email, rPPS);
            chargerMedecin();
            viderChampsMedecin();
            JOptionPane.showMessageDialog(this, "Médecin ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur inattendue : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterMutuelle() {
        try {
            String nom = txtNomMutuelle.getText().trim();
            String adresse = txtAdresseMutuelle.getText().trim();
            String codePostal = txtCodePostalMutuelle.getText().trim();
            String ville = txtVilleMutuelle.getText().trim();
            String telephone = txtTelephoneMutuelle.getText().trim();
            String email = txtEmailMutuelle.getText().trim();
            String departement = txtDepartementMutuelle.getText().trim();
            String tauxStr = txtTauxRbMutuelle.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() || ville.isEmpty() ||
                    telephone.isEmpty() || email.isEmpty() || departement.isEmpty() || tauxStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int taux = Integer.parseInt(tauxStr);
            new Mutuelle(nom, adresse, codePostal, ville, telephone, email, departement, taux);
            chargerMutuelle();
            viderChampsMutuelle();
            JOptionPane.showMessageDialog(this, "Mutuelle ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le taux de remboursement doit être un nombre entier!", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur inattendue : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterMedicament() {
        try {
            String nom = txtNomMedicament.getText().trim();
            String categorie = txtCategoriMedicament.getText().trim();
            String prixStr = txtPrixMedicament.getText().trim();
            String dateMiseCirculation = txtDateMiseCirculation.getText().trim();
            String quantiteStr = txtQuantiteMedicament.getText().trim();
            String sansOrdonnance = (String) cbSansOrdonnanceMedicament.getSelectedItem();

            if (nom.isEmpty() || categorie.isEmpty() || prixStr.isEmpty() ||
                    dateMiseCirculation.isEmpty() || quantiteStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double prix = Double.parseDouble(prixStr);
            int quantite = Integer.parseInt(quantiteStr);

            new Medicament(nom, categorie, prix, dateMiseCirculation, quantite, sansOrdonnance);
            chargerMedicament();
            viderChampsMedicament();
            JOptionPane.showMessageDialog(this, "Médicament ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prix et quantité doivent être des nombres valides!", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur inattendue : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

            chargerMedecin();
            viderChampsMedecin();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
    private void modifierMutuelle() {
        int selectedRow = tableMutuelle.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mutuelle à modifier.");
            return;
        }

        try {
            Mutuelle mutuelle = Mutuelle.getMutuelles().get(selectedRow);
            mutuelle.setNom(txtNomMutuelle.getText());
            mutuelle.setAdresse(txtAdresseMutuelle.getText());
            mutuelle.setCodePostal(txtCodePostalMutuelle.getText());
            mutuelle.setVille(txtVilleMutuelle.getText());
            mutuelle.setTelephone(txtTelephoneMutuelle.getText());
            mutuelle.setEmail(txtEmailMutuelle.getText());
            mutuelle.setDepartement(txtDepartementMutuelle.getText());
            mutuelle.setTRemboursement(Integer.parseInt(txtTauxRbMutuelle.getText()));

            chargerMutuelle();
            viderChampsMutuelle();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
    private void modifierMedicament() {
        int selectedRow = tableMedicament.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médicament à modifier.");
            return;
        }

        try {
            Medicament medicament = Medicament.getMedicaments().get(selectedRow);

            medicament.setNomMedicament(txtNomMedicament.getText());
            medicament.setCategorieMedicament(txtCategoriMedicament.getText());
            medicament.setPrixMedicament(Double.parseDouble(txtPrixMedicament.getText()));
            medicament.setDateMiseEnCirculation(txtDateMiseCirculation.getText());
            medicament.setQuantiteMedicament(Integer.parseInt(txtQuantiteMedicament.getText()));
            medicament.setSansOrdonnanceMedicament((String) cbSansOrdonnanceMedicament.getSelectedItem());

            chargerMedicament();
            viderChampsMedicament();
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
    private void supprimerMedecin() {
        int selectedRow = tableMedecin.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Medecin.getMedecins().remove(selectedRow);
            chargerMedecin();
            viderChampsMedecin();
        }
    }
    private void supprimerMutuelle() {
        int selectedRow = tableMutuelle.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mutuelle à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Mutuelle.getMutuelles().remove(selectedRow);
            chargerMutuelle();
            viderChampsMutuelle();
        }
    }
    private void supprimerMedicament() {
        int selectedRow = tableMedicament.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médicament à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Medicament.getMedicaments().remove(selectedRow);
            chargerMedicament();
            viderChampsMedicament();
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

    private void viderChampsMutuelle() {
        txtNomMutuelle.setText("");
        txtAdresseMutuelle.setText("");
        txtCodePostalMutuelle.setText("");
        txtVilleMutuelle.setText("");
        txtTelephoneMutuelle.setText("");
        txtEmailMutuelle.setText("");
        txtDepartementMutuelle.setText("");
        txtTauxRbMutuelle.setText("");
    }

    private void viderChampsMedicament() {
        txtNomMedicament.setText("");
        txtCategoriMedicament.setText("");
        txtPrixMedicament.setText("");
        txtDateMiseCirculation.setText("");
        txtQuantiteMedicament.setText("");
        cbSansOrdonnanceMedicament.setSelectedIndex(0);
    }

    // Méthodes de recherche
    private void rechercherClientParNom() {
        String nom = txtRechercheNom.getText().trim();
        if (nom.isEmpty()) {
            chargerClient();
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

    private void rechercherClientParEmail() {
        String email = txtRechercheEmail.getText().trim();
        if (email.isEmpty()) {
            chargerClient();
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

    private void rechercherClientParNss() {
        String nss = txtRechercheNss.getText().trim();
        if (nss.isEmpty()) {
            chargerClient();
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

    private void rechercherMedecinParNom(String nom) {
        if (nom.trim().isEmpty()) {
            chargerMedecin();
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

    private void rechercherMedecinParRpps() {
        String rpps = txtRechercheRpps.getText().trim();
        if (rpps.isEmpty()) {
            chargerMedecin();
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

    private void rechercherMutuelleParNom(String nom) {
        if (nom.trim().isEmpty()) {
            chargerMutuelle();
            return;
        }

        modelMutuelle.setRowCount(0);
        for (Mutuelle m : Mutuelle.getMutuelles()) {
            if (m.getNom().toLowerCase().contains(nom.toLowerCase())) {
                modelMutuelle.addRow(new Object[]{
                        m.getNom(), m.getAdresse(), m.getCodePostal(), m.getVille(),
                        m.getTelephone(), m.getEmail(), m.getDepartement(), m.getTRemboursement() + "%"
                });
            }
        }
    }

    private void rechercherMutuelleParDepartement() {
        String departement = txtRechercheDepartement.getText().trim();
        if (departement.isEmpty()) {
            chargerMutuelle();
            return;
        }

        modelMutuelle.setRowCount(0);
        for (Mutuelle m : Mutuelle.getMutuelles()) {
            if (m.getDepartement().toLowerCase().contains(departement.toLowerCase())) {
                modelMutuelle.addRow(new Object[]{
                        m.getNom(), m.getAdresse(), m.getCodePostal(), m.getVille(),
                        m.getTelephone(), m.getEmail(), m.getDepartement(), m.getTRemboursement() + "%"
                });
            }
        }
    }

    private void rechercherMedicamentParNom() {
        String nom = txtRechercheNomMedicament.getText().trim();
        if (nom.isEmpty()) {
            chargerMedicament();
            return;
        }

        modelMedicament.setRowCount(0);
        for (Medicament m : Medicament.getMedicaments()) {
            if (m.getNomMedicament().toLowerCase().contains(nom.toLowerCase())) {
                modelMedicament.addRow(new Object[]{
                        m.getNomMedicament(), m.getCategorieMedicament(), m.getPrixMedicament() + " €",
                        m.getDateMiseEnCirculation(), m.getQuantiteMedicament(), m.getSansOrdonnanceMedicament()
                });
            }
        }
    }

    private void rechercherMedicamentParCategorie() {
        String categorie = txtRechercheCategorieMedicament.getText().trim();
        if (categorie.isEmpty()) {
            chargerMedicament();
            return;
        }

        modelMedicament.setRowCount(0);
        for (Medicament m : Medicament.getMedicaments()) {
            if (m.getCategorieMedicament().toLowerCase().contains(categorie.toLowerCase())) {
                modelMedicament.addRow(new Object[]{
                        m.getNomMedicament(), m.getCategorieMedicament(), m.getPrixMedicament() + " €",
                        m.getDateMiseEnCirculation(), m.getQuantiteMedicament(), m.getSansOrdonnanceMedicament()
                });
            }
        }
    }


    private void ouvrirDialogueCreationOrdonnance() {
        JDialog dialogOrdonnance = new JDialog(this, "📋 Créer une Ordonnance", true);
        dialogOrdonnance.setSize(1200, 800);
        dialogOrdonnance.setLocationRelativeTo(this);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));


        final Client[] clientSelectionne = {null};
        final Medecin[] medecinSelectionne = {null};


        JPanel panelClient = new JPanel(new BorderLayout());
        panelClient.setBorder(new TitledBorder("👤 Sélection du Client"));
        String[] colonnesClientsOrdo = {"Nom", "Prénom", "NSS"};
        DefaultTableModel modelClientsOrdo = new DefaultTableModel(colonnesClientsOrdo, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableClientsOrdo = new JTable(modelClientsOrdo);
        tableClientsOrdo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Client client : Client.getClients()) {
            modelClientsOrdo.addRow(new Object[]{ client.getNom(), client.getPrenom(), client.getNss() });
        }
        JScrollPane scrollClientsOrdo = new JScrollPane(tableClientsOrdo);
        scrollClientsOrdo.setPreferredSize(new Dimension(650, 150));
        panelClient.add(scrollClientsOrdo, BorderLayout.CENTER);

        JPanel panelMedecin = new JPanel(new BorderLayout());
        panelMedecin.setBorder(new TitledBorder("👨‍⚕️ Sélection du Médecin"));
        String[] colonnesMedecinsOrdo = {"Nom", "Prénom", "RPPS"};
        DefaultTableModel modelMedecinsOrdo = new DefaultTableModel(colonnesMedecinsOrdo, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableMedecinsOrdo = new JTable(modelMedecinsOrdo);
        tableMedecinsOrdo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Medecin medecin : Medecin.getMedecins()) {
            modelMedecinsOrdo.addRow(new Object[]{ medecin.getNom(), medecin.getPrenom(), medecin.getRPPS() });
        }
        JScrollPane scrollMedecinsOrdo = new JScrollPane(tableMedecinsOrdo);
        scrollMedecinsOrdo.setPreferredSize(new Dimension(650, 150));
        panelMedecin.add(scrollMedecinsOrdo, BorderLayout.CENTER);

        JPanel panelMedicament = new JPanel(new BorderLayout());
        panelMedicament.setBorder(new TitledBorder("📦 Sélection du Médicament"));
        String[] colonnesMedicamentsOrdo = {"Nom", "Prix", "Quantité Disponible"};
        DefaultTableModel modelMedicamentsOrdo = new DefaultTableModel(colonnesMedicamentsOrdo, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableMedicamentsOrdo = new JTable(modelMedicamentsOrdo);
        tableMedicamentsOrdo.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        for (Medicament medicament : Medicament.getMedicaments()) {
            modelMedicamentsOrdo.addRow(new Object[]{ medicament.getNomMedicament(), medicament.getPrixMedicament(), medicament.getQuantiteMedicament() });
        }
        JScrollPane scrollMedicamentsOrdo = new JScrollPane(tableMedicamentsOrdo);
        scrollMedicamentsOrdo.setPreferredSize(new Dimension(650, 150));
        panelMedicament.add(scrollMedicamentsOrdo, BorderLayout.CENTER);

        JPanel panelTables = new JPanel();
        panelTables.setLayout(new BoxLayout(panelTables, BoxLayout.Y_AXIS));
        panelTables.add(panelClient);
        panelTables.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTables.add(panelMedecin);
        panelTables.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTables.add(panelMedicament);

        // Boutons
        JPanel panelBoutonsOrdo = new JPanel(new FlowLayout());
        JButton btnCreerOrdo = new JButton("✅ Créer l'Ordonnance");
        JButton btnAnnulerOrdo = new JButton("❌ Annuler");

        btnCreerOrdo.addActionListener(e -> {
            int ligneSelectionnee;

            ligneSelectionnee = tableClientsOrdo.getSelectedRow();
            if (ligneSelectionnee == -1) {
                JOptionPane.showMessageDialog(dialogOrdonnance, "Veuillez sélectionner un client!", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nssSelectionne = (String) modelClientsOrdo.getValueAt(ligneSelectionnee, 2);
            for (Client client : Client.getClients()) {
                if (client.getNss().equals(nssSelectionne)) {
                    clientSelectionne[0] = client;
                    break;
                }
            }

            ligneSelectionnee = tableMedecinsOrdo.getSelectedRow();
            if (ligneSelectionnee == -1) {
                JOptionPane.showMessageDialog(dialogOrdonnance, "Veuillez sélectionner un médecin!", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String rppsSelectionne = (String) modelMedecinsOrdo.getValueAt(ligneSelectionnee, 2);
            for (Medecin medecin : Medecin.getMedecins()) {
                if (medecin.getRPPS().equals(rppsSelectionne)) {
                    medecinSelectionne[0] = medecin;
                    break;
                }
            }

            int[] lignesMedicaments = tableMedicamentsOrdo.getSelectedRows();
            if (lignesMedicaments.length == 0) {
                JOptionPane.showMessageDialog(dialogOrdonnance, "Veuillez sélectionner au moins un médicament!", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Prescription> prescriptions = new ArrayList<>();

            for (int ligne : lignesMedicaments) {
                String nomMedicament = (String) modelMedicamentsOrdo.getValueAt(ligne, 0);
                Medicament medicamentChoisi = null;
                for (Medicament m : Medicament.getMedicaments()) {
                    if (m.getNomMedicament().equals(nomMedicament)) {
                        medicamentChoisi = m;
                        break;
                    }
                }
                if (medicamentChoisi == null) continue;


                int quantitePrescrite = 0;
                boolean valid = false;
                while (!valid) {
                    String input = JOptionPane.showInputDialog(dialogOrdonnance,
                            "Quantité pour le médicament \"" + medicamentChoisi.getNomMedicament() + "\" (max " + medicamentChoisi.getQuantiteMedicament() + ") :",
                            "Quantité Médicament",
                            JOptionPane.QUESTION_MESSAGE);
                    if (input == null) {

                        return;
                    }
                    try {
                        quantitePrescrite = Integer.parseInt(input);
                        if (quantitePrescrite <= 0) {
                            JOptionPane.showMessageDialog(dialogOrdonnance, "La quantité doit être supérieure à 0.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else if (quantitePrescrite > medicamentChoisi.getQuantiteMedicament()) {
                            JOptionPane.showMessageDialog(dialogOrdonnance, "Quantité supérieure au stock disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            valid = true;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialogOrdonnance, "Veuillez entrer un nombre entier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }

                prescriptions.add(new Prescription(medicamentChoisi, quantitePrescrite));
            }

            LocalDate dateOrdonnance = LocalDate.now();
            try {

                Ordonnance ordonnances = new Ordonnance(medecinSelectionne[0], clientSelectionne[0], prescriptions, dateOrdonnance);

                chargerOrdonnances();
                dialogOrdonnance.dispose();

                String medicamentList = "";
                for (Prescription p : prescriptions) {
                    medicamentList = medicamentList + "• " + p.getNomMedicament() + " x" + p.getQuantitePrescrite() + "\n";
                }

                JOptionPane.showMessageDialog(this,
                        "Ordonnance créée avec succès!\n" +
                                "👤 Client: " + clientSelectionne[0].getNom() + " " + clientSelectionne[0].getPrenom() + "\n" +
                                "👨‍⚕️ Médecin: " + medecinSelectionne[0].getNom() + " " + medecinSelectionne[0].getPrenom() + "\n" +
                                "📦 Médicaments:\n" + medicamentList.toString(),
                        "✅ Succès", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogOrdonnance,
                        "Erreur lors de la création de l'ordonnance: " + ex.getMessage(),
                        "❌ Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAnnulerOrdo.addActionListener(e -> dialogOrdonnance.dispose());

        panelBoutonsOrdo.add(btnCreerOrdo);
        panelBoutonsOrdo.add(btnAnnulerOrdo);

        panelPrincipal.add(panelTables, BorderLayout.CENTER);
        panelPrincipal.add(panelBoutonsOrdo, BorderLayout.SOUTH);

        dialogOrdonnance.add(panelPrincipal);
        dialogOrdonnance.setVisible(true);
    }

    private void viderFacturation() {
        int choix = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir vider l'historique ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            modelFacturation.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Historique vidé avec succès!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new MainSwing().setVisible(true);
        });
    }
}