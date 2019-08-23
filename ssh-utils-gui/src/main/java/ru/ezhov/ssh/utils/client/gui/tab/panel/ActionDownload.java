package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.FileStatus;
import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;
import ru.ezhov.ssh.utils.core.SshAction;
import ru.ezhov.ssh.utils.core.SshActionFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class ActionDownload extends AbstractAction {

    {
        putValue(AbstractAction.SHORT_DESCRIPTION, "Скачать выбранные файлы");
        putValue(AbstractAction.LONG_DESCRIPTION, "Скачать выбранные файлы");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/inbox-download.png")));
    }

    private PanelFilesLog panelFilesLog;
    private JTable table;
    private SshFileTableModel sshFileTableModel;

    public ActionDownload(PanelFilesLog panelFilesLog, JTable table, SshFileTableModel sshFileTableModel) {
        this.panelFilesLog = panelFilesLog;
        this.table = table;
        this.sshFileTableModel = sshFileTableModel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            int[] selectedRow = table.getSelectedRows();
            for (int r : selectedRow) {
                SshDownloadFileGui valueAt = sshFileTableModel.getBy(r);
                class DownloadSwingInvoker extends SwingWorker<String, String> {

                    @Override
                    protected String doInBackground() throws Exception {
                        try {
                            publish("start_download");
                            publish("Скачивание файла: '" + valueAt.getFileFrom() + "' в '" + valueAt.getFileTo() + "'... ");
                            SshAction sshAction = SshActionFactory.downloadFileAction(
                                    valueAt.getUsername(),
                                    valueAt.getHost(),
                                    Integer.valueOf(valueAt.getPort()),
                                    valueAt.getPathToPrivateKey(),
                                    valueAt.getPassphrase(),
                                    valueAt.getFileFrom(),
                                    valueAt.getFileTo()
                            );
                            sshAction.perform();
                            publish("end_download");
                            publish("Скачивание файла '" + valueAt.getFileFrom() + "' в '" + valueAt.getFileTo() + "' завершено");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            publish("error");
                            publish(stackTrace(e1));
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<String> chunks) {
                        for (String s : chunks) {
                            if ("start_download".equals(s)) {
                                valueAt.setFileStatus(FileStatus.DOWNLOAD);
                            } else if ("end_download".equals(s)) {
                                valueAt.updateStatus();
                            } else if ("error".equals(s)) {
                                valueAt.setFileStatus(FileStatus.DOWNLOAD_WITH_ERROR);
                            } else {
                                panelFilesLog.addToLog(s);
                            }
                        }
                        table.repaint();
                    }
                }
                DownloadSwingInvoker downloadSwingInvoker = new DownloadSwingInvoker();
                downloadSwingInvoker.execute();
            }
        });
    }

    private String stackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
