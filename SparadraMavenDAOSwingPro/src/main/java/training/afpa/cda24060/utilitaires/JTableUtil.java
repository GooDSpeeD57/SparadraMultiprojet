package training.afpa.cda24060.utilitaires;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTableUtil {


    public static void appliquerLignesAlternes(JTable table) {
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
    }

    public static void ajusterColonnesJTable(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // largeur minimum
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
    }

    static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(119, 231, 92));
            }
            return c;
        }
    }
}