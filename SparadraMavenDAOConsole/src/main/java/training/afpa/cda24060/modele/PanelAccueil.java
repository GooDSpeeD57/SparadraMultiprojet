package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import training.afpa.cda24060.modele.*;

public class PanelAccueil extends JPanel {

    public PanelAccueil() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        ImageIcon backgroundIcon = new ImageIcon(
                getClass().getResource("/test.png")
        );
        Image backgroundImage = backgroundIcon.getImage();

        JPanel panelBackground = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelBackground.setOpaque(false);

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

        JLabel lblNbClient = new JLabel("Clients : " + Client.getClients().size(), SwingConstants.CENTER);
        JLabel lblNbMedicament = new JLabel("Médicaments : " + Medicament.getMedicaments().size(), SwingConstants.CENTER);
        JLabel lblNbMedecin = new JLabel("Médecins : " + Medecin.getMedecins().size(), SwingConstants.CENTER);
        JLabel lblNbMutuelle = new JLabel("Mutuelles : " + Mutuelle.getMutuelles().size(), SwingConstants.CENTER);

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

        panelBackground.add(panelBienvenue, BorderLayout.CENTER);
        panelBackground.add(panelStats, BorderLayout.SOUTH);

        add(panelBackground);
    }

    public void actualiserStatistiques() {
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }
}