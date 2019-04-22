package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SshFileTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String text = String.valueOf(value);
        switch (column) {
            case 4:
            case 5:
                label.setText("××××××");
                break;
            default:
                label.setText(text);
        }

        return label;
    }
}
