package training.afpa.cda24060.controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.vue.PanelSwing.*;

public class MainSwing extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainSwing.class);

    private JTabbedPane tabbedPane;
    private PanelAccueil panelAccueil;
    private PanelClient panelClient;
    private PanelMedecin panelMedecin;
    private PanelMutuelle panelMutuelle;
    private PanelMedicament panelMedicament;
    private PanelOrdonnance panelOrdonnance;
    private PanelHistorique panelHistorique;

    public MainSwing() {
        try {
            initComponents();
            chargerDonneesDansGUI();
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur SQL lors de l'initialisation", e);
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SaisieException e) {
            LogUtils.error(logger, "Erreur de saisie lors de l'initialisation", e);
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() throws SQLException, SaisieException {
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

        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();

            try {
                if (selected == panelClient) {
                    panelClient.chargerClients();
                } else if (selected == panelMedecin) {
                    panelMedecin.chargerMedecins();
                } else if (selected == panelMutuelle) {
                    panelMutuelle.chargerMutuelles();
                } else if (selected == panelMedicament) {
                    panelMedicament.chargerMedicaments();
                } else if (selected == panelOrdonnance) {
                    panelOrdonnance.chargerOrdonnances();
                } else if (selected == panelHistorique) {
                    panelHistorique.chargerHistorique();
                } else if (selected == panelAccueil) {
                    panelAccueil.actualiserStatistiques();
                }
            } catch (SQLException ex) {
                LogUtils.error(logger, "Erreur SQL lors du chargement des données du panneau", ex);
                JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SaisieException ex) {
                LogUtils.error(logger, "Erreur de saisie lors du chargement des données du panneau", ex);
                JOptionPane.showMessageDialog(this, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(tabbedPane);
    }

    private void chargerDonneesDansGUI() throws SQLException, SaisieException {
        panelClient.chargerClients();
        panelMedecin.chargerMedecins();
        panelMutuelle.chargerMutuelles();
        panelMedicament.chargerMedicaments();
        panelOrdonnance.chargerOrdonnances();
        panelHistorique.chargerHistorique();
        panelAccueil.actualiserStatistiques();
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

    private void quitter() {
        int choix = JOptionPane.showConfirmDialog(this,
                "Voulez-vous quitter Sparadra ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            try {
                DCSingletonHikaricp.closePool();
            } catch (Exception e) {
                LogUtils.warn(logger, "Erreur lors de la fermeture du pool de connexion", e);
            }
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
