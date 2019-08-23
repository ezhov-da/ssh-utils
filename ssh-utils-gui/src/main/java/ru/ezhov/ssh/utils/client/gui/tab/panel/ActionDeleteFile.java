package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.FileStatus;
import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class ActionDeleteFile extends AbstractAction {
    private SshFileTableModel sshFileTableModel;
    private JTable table;
    private PanelFilesLog panelFilesLog;

    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Удалить с диска выбранные файлы");
        putValue(AbstractAction.LONG_DESCRIPTION, "Удалить с диска выбранные файлы");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/trash-can-delete_16x16.png")));
    }

    public ActionDeleteFile(SshFileTableModel sshFileTableModel, JTable table, PanelFilesLog panelFilesLog) {
        this.sshFileTableModel = sshFileTableModel;
        this.table = table;
        this.panelFilesLog = panelFilesLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            int[] selectedRow = table.getSelectedRows();

            int confirmDialog = JOptionPane.showConfirmDialog(table, "Будет удалено " + selectedRow.length + " файлов(а). Продолжить?", "Удаление файлов", JOptionPane.YES_NO_OPTION);
            if (confirmDialog != JOptionPane.YES_OPTION) {
                return;
            }

            for (int r : selectedRow) {
                SshDownloadFileGui valueAt = sshFileTableModel.getBy(r);
                class DeletedFileSwingInvoker extends SwingWorker<String, String> {

                    @Override
                    protected String doInBackground() throws Exception {
                        try {
                            String fileTo = valueAt.getFileTo();
                            publish("Удаление файла: '" + fileTo);
                            publish("start_deleting");
                            if (fileTo != null && !"".equals(fileTo)) {
                                File file = new File(fileTo);
                                boolean delete = file.delete();
                                if (delete) {
                                    publish("Файл: '" + fileTo + " удален.");
                                } else {
                                    publish("Не удалось удалить файл: '" + fileTo + "");
                                }
                            }
                            publish("end_deleting");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            publish(stackTrace(e1));
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<String> chunks) {
                        for (String s : chunks) {
                            if ("start_deleting".equals(s)) {
                                valueAt.setFileStatus(FileStatus.DELETED);
                            } else if ("end_deleting".equals(s)) {
                                valueAt.updateStatus();
                            } else {
                                panelFilesLog.addToLog(s);
                            }
                        }
                        table.repaint();
                    }
                }
                DeletedFileSwingInvoker deletedFileSwingInvoker = new DeletedFileSwingInvoker();
                deletedFileSwingInvoker.execute();
            }
        });
    }

    private String stackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
