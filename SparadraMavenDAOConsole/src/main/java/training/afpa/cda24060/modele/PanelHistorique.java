package training.afpa.cda24060.modele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorique extends JPanel {

    private JTable tableFacturation;
    private DefaultTableModel modelFacturation;

    public PanelHistorique() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de contrôles (boutons)
        JPanel panelControles = new JPanel(new FlowLayout());

        JButton btnActualiserFacturation = new JButton("🔄 Actualiser");
        btnActualiserFacturation.addActionListener(e -> chargerHistorique());

        JButton btnViderHistorique = new JButton("🗑️ Vider l'historique");
        btnViderHistorique.addActionListener(e -> viderHistorique());

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
        tableFacturation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollFacturation = new JScrollPane(tableFacturation);
        scrollFacturation.setBorder(new TitledBorder("\uD83D\uDCCB Historique des Actions"));

        add(panelControles, BorderLayout.NORTH);
        add(scrollFacturation, BorderLayout.CENTER);
    }

    public void chargerHistorique() {
        modelFacturation.setRowCount(0);
        // Placeholder pour l'historique
        // Cette méthode sera complétée avec la gestion de l'historique
        modelFacturation.addRow(new Object[]{
                new java.util.Date(),
                "Application démarrée",
                "Système",
                "Chargement des données"
        });
    }

    private void viderHistorique() {
        int choix = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir vider l'historique ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            modelFacturation.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Historique vidé avec succès!",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Méthode pour ajouter une entrée dans l'historique
     * @param action Type d'action effectuée
     * @param utilisateur Utilisateur qui a effectué l'action
     * @param details Détails supplémentaires sur l'action
     */
    public void ajouterEntreeHistorique(String action, String utilisateur, String details) {
        modelFacturation.addRow(new Object[]{
                new java.util.Date(),
                action,
                utilisateur,
                details
        });
    }
}