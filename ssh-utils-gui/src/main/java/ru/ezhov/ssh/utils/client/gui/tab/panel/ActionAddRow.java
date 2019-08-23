package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;
import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionAddRow extends AbstractAction {
    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Добавить файл в список");
        putValue(AbstractAction.LONG_DESCRIPTION, "Добавить файл в список");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/layer--plus.png")));
    }

    private SshFileTableModel sshFileTableModel;

    public ActionAddRow(SshFileTableModel sshFileTableModel) {
        this.sshFileTableModel = sshFileTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            sshFileTableModel.setValueAt(
                    SshDownloadFileGui.from(SshDownloadFile.createEmpty()),
                    sshFileTableModel.getRowCount(),
                    0
            );
        });
    }
}
