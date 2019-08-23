package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionRemoveRow extends AbstractAction {
    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Удалить файл из списка");
        putValue(AbstractAction.LONG_DESCRIPTION, "Удалить файл из списка");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/layer--minus.png")));
    }

    private JTable table;
    private SshFileTableModel sshFileTableModel;

    public ActionRemoveRow(JTable table, SshFileTableModel sshFileTableModel) {
        this.table = table;
        this.sshFileTableModel = sshFileTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                sshFileTableModel.setValueAt(null, selectedRow, 0);
                if (sshFileTableModel.getRowCount() > 0) {
                    if (selectedRow > table.getRowCount()) {
                        table.getSelectionModel().setSelectionInterval(table.getRowCount(), table.getRowCount());
                    } else {
                        table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
        });
    }
}
