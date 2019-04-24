package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.gui.gui.utils.AnimatedIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SshFileTableRenderer extends DefaultTableCellRenderer {
    private Icon iconExists = new ImageIcon(getClass().getResource("/images/exists_16x16.png"));
    private Icon iconNotExists = new ImageIcon(getClass().getResource("/images/not_exists_16x16.png"));
    private Icon iconDownload = new AnimatedIcon(new ImageIcon(getClass().getResource("/images/wait_16x16.gif")));
    private Icon iconDownloadWithError = new ImageIcon(getClass().getResource("/images/error_16x16.png"));

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        SshFileTableModel sshFileTableModel = (SshFileTableModel) table.getModel();
        SshDownloadFileGui sshDownloadFileGui = sshFileTableModel.getBy(row);

        String text = String.valueOf(value);
        String toolTipText = text;
        label.setToolTipText("");
        label.setIcon(null);
        switch (column) {
            case 4:
            case 5:
                if ("".equals(text) || text == null) {
                    text = "";
                } else {
                    text = "××××××";
                }
                break;
            case 7:
                switch (sshDownloadFileGui.getFileStatus()) {
                    case EXISTS:
                        label.setIcon(iconExists);
                        text = text + " (" + sshDownloadFileGui.readableFileSize() + ")";
                        break;
                    case NOT_EXISTS:
                        label.setIcon(iconNotExists);
                        break;
                    case EMPTY_VALUE:
                        label.setIcon(null);
                        break;
                    case DOWNLOAD:
                        label.setIcon(iconDownload);
                        break;
                    case DOWNLOAD_WITH_ERROR:
                        label.setIcon(iconDownloadWithError);
                        break;
                }
                break;
        }
        label.setText(text);
        label.setToolTipText(toolTipText);
        return label;
    }
}
