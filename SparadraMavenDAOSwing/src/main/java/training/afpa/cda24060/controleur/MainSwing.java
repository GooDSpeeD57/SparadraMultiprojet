package training.afpa.cda24060.controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import training.afpa.cda24060.vue.PanelSwing.*;

public class MainSwing extends JFrame {

    private JTabbedPane tabbedPane;

    // Panels
    private PanelAccueil panelAccueil;
    private PanelClient panelClient;
    private PanelMedecin panelMedecin;
    private PanelMutuelle panelMutuelle;
    private PanelMedicament panelMedicament;
    private PanelOrdonnance panelOrdonnance;
    private PanelHistorique panelHistorique;

    public MainSwing() {
        initComponents();
        chargerDonneesDansGUI();
    }

    private void initComponents() {
        setTitle("Système de Gestion de Pharmacie");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitter();
            }
        });

        creerMenu();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

        // Panels avec DAO
        panelAccueil = new PanelAccueil();
        panelClient = new PanelClient();
        panelMedecin = new PanelMedecin();
        panelMutuelle = new PanelMutuelle();
        panelMedicament = new PanelMedicament();
        panelOrdonnance = new PanelOrdonnance(this);
        panelHistorique = new PanelHistorique();

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
        JMenuItem itemQuitter = new JMenuItem("Quitter");

        itemQuitter.addActionListener(e -> quitter());

        menuFichier.add(itemQuitter);

        JMenu menuAide = new JMenu("Aide");
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Système de Gestion de Pharmacie\nVersion 1.0\n\nDéveloppé avec Java Swing par Julien Taesch",
                        "À propos",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        menuAide.add(itemAPropos);

        menuBar.add(menuFichier);
        menuBar.add(menuAide);
        setJMenuBar(menuBar);
    }

    private void chargerDonneesDansGUI() {
        panelClient.chargerClients();          // --> Appelle ClientDAO
        panelMedecin.chargerMedecins();        // --> Appelle MedecinDAO
        panelMutuelle.chargerMutuelles();      // --> Appelle MutuelleDAO
        panelMedicament.chargerMedicaments();  // --> Appelle MedicamentDAO
        panelOrdonnance.chargerOrdonnances();  // --> Appelle OrdonnanceDAO
        panelHistorique.chargerHistorique();
        panelAccueil.actualiserStatistiques();
    }

    private void quitter() {
        int choix = JOptionPane.showConfirmDialog(this,
                "Voulez-vous quitter l'application ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}

        SwingUtilities.invokeLater(() -> new MainSwing().setVisible(true));
    }
}
