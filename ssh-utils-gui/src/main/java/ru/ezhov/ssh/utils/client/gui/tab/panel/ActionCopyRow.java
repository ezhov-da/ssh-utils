package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionCopyRow extends AbstractAction {
    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Дублировать выделенную строку");
        putValue(AbstractAction.LONG_DESCRIPTION, "Дублировать выделенную строку");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/blue-document-copy.png")));
    }

    private JTable table;
    private SshFileTableModel sshFileTableModel;

    public ActionCopyRow(JTable table, SshFileTableModel sshFileTableModel) {
        this.table = table;
        this.sshFileTableModel = sshFileTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                SshDownloadFileGui downloadFile = sshFileTableModel.getBy(selectedRow);
                SshDownloadFileGui copyDownloadFile = SshDownloadFileGui.from(downloadFile);
                sshFileTableModel.setValueAt(copyDownloadFile, sshFileTableModel.getRowCount(), 0);
                int last = sshFileTableModel.getRowCount() - 1;
                table.getSelectionModel().setSelectionInterval(last, last);
            }
        });
    }
}
