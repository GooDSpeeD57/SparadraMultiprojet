package training.afpa.cda24060.controleur;

import training.afpa.cda24060.modele.PanelMedicament;

import javax.swing.*;

public class MainMedicament {

    public static void main(String[] args) {
        // On force le look & feel natif pour plus de cohérence
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestion des Médicaments");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PanelMedicament panelMedicament = new PanelMedicament();
            frame.setContentPane(panelMedicament);

            frame.pack(); // ajuste la taille automatiquement
            frame.setLocationRelativeTo(null); // centre la fenêtre
            frame.setVisible(true);
        });
    }
}
