package training.afpa.cda24060.controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import training.afpa.cda24060.modele.*;


public class MainSwing extends JFrame {

    private static final String FICHIER_PERSISTANCE = "donnees.bin";
    private static Map<String, Object> donnees;

    private JTabbedPane tabbedPane;

    // Les différents panels
    private PanelAccueil panelAccueil;
    private PanelClient panelClient;
    private PanelMedecin panelMedecin;
    private PanelMutuelle panelMutuelle;
    private PanelMedicament panelMedicament;
    private PanelOrdonnance panelOrdonnance;
    private PanelHistorique panelHistorique;

    public MainSwing() {
        chargerDonnees();
        initComponents();
        chargerDonneesDansGUI();
    }

    private void chargerDonnees() {
        donnees = PersitSerializable.charger(FICHIER_PERSISTANCE);
        List<Mutuelle> mutuelles = (List<Mutuelle>) donnees.getOrDefault("mutuelles", new ArrayList<>());
        List<Medicament> medicaments = (List<Medicament>) donnees.getOrDefault("medicaments", new ArrayList<>());
        List<Medecin> medecins = (List<Medecin>) donnees.getOrDefault("medecins", new ArrayList<>());
        List<Pharmacien> pharmaciens = (List<Pharmacien>) donnees.getOrDefault("pharmaciens", new ArrayList<>());
        List<Client> clients = (List<Client>) donnees.getOrDefault("clients", new ArrayList<>());
        List<Ordonnance> ordonnances = (List<Ordonnance>) donnees.getOrDefault("ordonnances", new ArrayList<>());

        Client.setClients(clients);
        Mutuelle.setMutuelles(mutuelles);
        Medicament.setMedicaments(medicaments);
        Medecin.setMedecins(medecins);
        Pharmacien.setPharmacien(pharmaciens);
        Ordonnance.setOrdonnances(ordonnances);
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

        // Instanciation des panels
        panelAccueil = new PanelAccueil();
        panelClient = new PanelClient();
        panelMedecin = new PanelMedecin();
        panelMutuelle = new PanelMutuelle();
        panelMedicament = new PanelMedicament();
        panelOrdonnance = new PanelOrdonnance(this);
        panelHistorique = new PanelHistorique();

        // Ajout des onglets
        tabbedPane.addTab("Accueil", panelAccueil);
        tabbedPane.addTab("Clients", panelClient);
        tabbedPane.addTab("Médecins", panelMedecin);
        tabbedPane.addTab("Mutuelles", panelMutuelle);
        tabbedPane.addTab("Médicaments", panelMedicament);
        tabbedPane.addTab("Ordonnances", panelOrdonnance);
        tabbedPane.addTab("Historique", panelHistorique);

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

    private void chargerDonneesDansGUI() {
        panelClient.chargerClients();
        panelMedecin.chargerMedecins();
        panelMutuelle.chargerMutuelles();
        panelMedicament.chargerMedicaments();
        panelOrdonnance.chargerOrdonnances();
        panelHistorique.chargerHistorique();
        panelAccueil.actualiserStatistiques();
    }

    private void sauvegarder() {
        donnees.put("clients", Client.getClients());
        donnees.put("medecins", Medecin.getMedecins());
        donnees.put("mutuelles", Mutuelle.getMutuelles());
        donnees.put("medicaments", Medicament.getMedicaments());
        donnees.put("pharmaciens", Pharmacien.getPharmacien());
        donnees.put("ordonnances", Ordonnance.getOrdonnances());

        PersitSerializable.sauvegarder(donnees, FICHIER_PERSISTANCE);
        JOptionPane.showMessageDialog(this, "Données sauvegardées avec succès!",
                "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
    }

    private void sauvegarderEtQuitter() {
        int choix = JOptionPane.showConfirmDialog(this,
                "Voulez-vous sauvegarder avant de quitter?",
                "Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            sauvegarder();
            System.exit(0);
        } else if (choix == JOptionPane.NO_OPTION) {
            System.exit(0);
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