package training.afpa.cda24060.vue.PanelSwing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import training.afpa.cda24060.ClasseDAO.*;
import training.afpa.cda24060.utilitaires.LogUtils;

public class PanelAccueil extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelAccueil.class);

    public PanelAccueil() throws SQLException {
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new BorderLayout());

        Image backgroundImage = loadBackgroundImage();
        if (backgroundImage == null) {
            return;
        }

        JPanel backgroundPanel = createBackgroundPanel(backgroundImage);
        JPanel panelBienvenue = createWelcomePanel();
        JPanel panelStats = createStatsPanel();

        backgroundPanel.add(panelBienvenue, BorderLayout.CENTER);
        backgroundPanel.add(panelStats, BorderLayout.SOUTH);

        add(backgroundPanel, BorderLayout.CENTER);
    }

    private Image loadBackgroundImage() {
        URL imageUrl = getClass().getResource("/test.png");
        if (imageUrl == null) {
            LogUtils.error(logger, "Image introuvable : {}", "/test.png");
            return null;
        }
        return new ImageIcon(imageUrl).getImage();
    }

    private JPanel createBackgroundPanel(Image backgroundImage) {

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createWelcomePanel() {

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

        return panelBienvenue;
    }

    private JPanel createStatsPanel() throws SQLException {

        JPanel panelStats = new JPanel(new GridLayout(2, 2, 10, 10));
        panelStats.setOpaque(false);
        panelStats.setBorder(new TitledBorder("Statistiques"));

        ClientDAO clientDao = new ClientDAO();
        MedicamentDAO medicamentDao = new MedicamentDAO();
        MedecinDAO medecinDao = new MedecinDAO();
        MutuelleDAO mutuelleDao = new MutuelleDAO();

        JLabel lblNbClient = new JLabel("Clients : " + clientDao.countClient(), SwingConstants.CENTER);
        JLabel lblNbMedicament = new JLabel("Médicaments : " + medicamentDao.countMedicaments(), SwingConstants.CENTER);
        JLabel lblNbMedecin = new JLabel("Médecins : " + medecinDao.countMedecins(), SwingConstants.CENTER);
        JLabel lblNbMutuelle = new JLabel("Mutuelles : " + mutuelleDao.countMutuelles(), SwingConstants.CENTER);

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

        return panelStats;
    }

    public void actualiserStatistiques() throws SQLException {
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }
}