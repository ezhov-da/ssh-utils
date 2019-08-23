package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

public class ActionCopyPathToBuffer extends AbstractAction {
    private JTable table;
    private SshFileTableModel sshFileTableModel;

    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Скопировать пути выбранных файлов в буфер");
        putValue(AbstractAction.LONG_DESCRIPTION, "Скопировать пути выбранных файлов в буфер");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/copy_path_ doc_16x16.png")));
    }

    public ActionCopyPathToBuffer(JTable table, SshFileTableModel sshFileTableModel) {
        this.table = table;
        this.sshFileTableModel = sshFileTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            int[] selectedRow = table.getSelectedRows();
            StringBuilder stringBuilder = new StringBuilder();
            for (int r : selectedRow) {
                SshDownloadFileGui valueAt = sshFileTableModel.getBy(r);
                try {
                    String fileTo = valueAt.getFileTo();
                    stringBuilder.append(fileTo).append("\n");
                    String filesTo = stringBuilder.toString().trim();
                    StringSelection stringSelection = new StringSelection(filesTo);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
