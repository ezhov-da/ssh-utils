package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.gui.gui.utils.AnimatedIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SshFileTableRenderer extends DefaultTableCellRenderer {
    private Icon iconExists = new ImageIcon(getClass().getResource("/images/exists_16x16.png"));
    private Icon iconNotExists = new ImageIcon(getClass().getResource("/images/not_exists_16x16.png"));
    private Icon iconWaite = new AnimatedIcon(new ImageIcon(getClass().getResource("/images/wait_16x16.gif")));
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
                toolTipText = "";
                break;
            case 7:
                switch (sshDownloadFileGui.getFileStatus()) {
                    case EXISTS:
                        label.setIcon(iconExists);
                        text = text + " (" + sshDownloadFileGui.readableFileSize() + ")";
                        toolTipText = "Файл существует";
                        break;
                    case NOT_EXISTS:
                        label.setIcon(iconNotExists);
                        toolTipText = "Файл не существует";
                        break;
                    case EMPTY_VALUE:
                        label.setIcon(null);
                        toolTipText = "Файл не объявлен";
                        break;
                    case DOWNLOAD:
                        label.setIcon(iconWaite);
                        toolTipText = "Файл загружается";
                        break;
                    case DELETED:
                        label.setIcon(iconWaite);
                        toolTipText = "Файл удаляется";
                        break;
                    case DOWNLOAD_WITH_ERROR:
                        label.setIcon(iconDownloadWithError);
                        toolTipText = "Загрузка файла прошла с ошибками";
                        break;
                }
                break;
        }
        label.setText(text);
        label.setToolTipText(toolTipText);
        return label;
    }
}
