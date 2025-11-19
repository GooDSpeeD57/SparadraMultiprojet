package training.afpa.cda24060.vue.panel;

import training.afpa.cda24060.ClasseDAO.ClientDAO;
import training.afpa.cda24060.ClasseDAO.MedicamentDAO;
import training.afpa.cda24060.ClasseDAO.MedecinDAO;
import training.afpa.cda24060.ClasseDAO.MutuelleDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AccueilPanel extends JPanel {

    private JPanel panelAccueil;
    private JLabel lblNbClient, lblNbMedicament, lblNbMedecin, lblNbMutuelle;
    private ClientDAO clientDAO = new ClientDAO();
    private MedicamentDAO medicamentDAO = new MedicamentDAO();
    private MedecinDAO medecinDAO = new MedecinDAO();
    private MutuelleDAO mutuelleDAO = new MutuelleDAO();

    public AccueilPanel() {
        setLayout(new BorderLayout());
        creerPanelAccueil();
    }

    private void creerPanelAccueil() {
        // Chargement de l'image de fond
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/test.png"));
        Image backgroundImage = backgroundIcon.getImage();

        panelAccueil = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelAccueil.setOpaque(false);

        // Panel de bienvenue
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

        // Panel statistiques
        JPanel panelStats = new JPanel(new GridLayout(2, 2, 10, 10));
        panelStats.setOpaque(false);
        panelStats.setBorder(new TitledBorder("Statistiques"));

        Font fontStats = new Font("Arial", Font.BOLD, 14);

        lblNbClient = new JLabel("", SwingConstants.CENTER);
        lblNbMedicament = new JLabel("", SwingConstants.CENTER);
        lblNbMedecin = new JLabel("", SwingConstants.CENTER);
        lblNbMutuelle = new JLabel("", SwingConstants.CENTER);

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

        // Ajouter le panelAccueil à ce JPanel
        add(panelAccueil, BorderLayout.CENTER);

        // Initialiser les statistiques
        actualiserStats();
    }

    // Méthode pour rafraîchir les statistiques
    public void actualiserStats() {
        try {
            lblNbClient.setText("Clients : " + clientDAO.countClients());
        } catch (Exception e) {
            lblNbClient.setText("Clients : N/A");
        }
        try {
            lblNbMedicament.setText("Médicaments : " + medicamentDAO.countMedicaments());
        } catch (Exception e) {
            lblNbMedicament.setText("Médicaments : N/A");
        }
        try {
            lblNbMedecin.setText("Médecins : " + medecinDAO.countMedecins());
        } catch (Exception e) {
            lblNbMedecin.setText("Médecins : N/A");
        }
        try {
            lblNbMutuelle.setText("Mutuelles : " + mutuelleDAO.countMutuelles());
        } catch (Exception e) {
            lblNbMutuelle.setText("Mutuelles : N/A");
        }
    }
}