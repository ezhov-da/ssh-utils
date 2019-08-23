package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionSaveTable extends AbstractAction {
    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Сохранить список");
        putValue(AbstractAction.LONG_DESCRIPTION, "Сохранить список");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/disk-black.png")));
    }

    private SshFileTableModel sshFileTableModel;

    public ActionSaveTable(SshFileTableModel sshFileTableModel) {
        this.sshFileTableModel = sshFileTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            sshFileTableModel.save();
        });
    }
}
