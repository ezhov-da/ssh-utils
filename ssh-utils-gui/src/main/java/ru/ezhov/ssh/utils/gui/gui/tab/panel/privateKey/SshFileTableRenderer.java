package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SshFileTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String text = String.valueOf(value);
        label.setText(text);
        return label;
    }
}
