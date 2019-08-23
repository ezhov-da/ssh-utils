package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionReloadTable extends AbstractAction {
    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Обновить список");
        putValue(AbstractAction.LONG_DESCRIPTION, "Обновить список");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/arrow-circle.png")));
    }

    private SshFileTableModel sshFileTableModel;

    public ActionReloadTable(SshFileTableModel sshFileTableModel) {
        this.sshFileTableModel = sshFileTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            sshFileTableModel.reload();
        });
    }
}
