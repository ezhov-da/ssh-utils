package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.core.SshAction;
import ru.ezhov.ssh.utils.core.SshActionFactory;
import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey.domain.FileStatus;
import ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepository;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepositoryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PanelSshPrivateKey extends JPanel {

    private ConfigRepository configRepository;
    private PanelFiles panelFiles;
    private PanelFilesLog panelFilesLog;

    public PanelSshPrivateKey(ConfigRepository configRepository) throws ConfigRepositoryException {
        this.configRepository = configRepository;
        panelFiles = new PanelFiles();
        panelFilesLog = new PanelFilesLog();
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(350);
        splitPane.setTopComponent(panelFiles);
        splitPane.setBottomComponent(panelFilesLog);

        add(splitPane, BorderLayout.CENTER);
    }

    private class PanelFiles extends JPanel {
        private JToolBar toolBar;
        private JToolBar toolBarActionDownload;
        private JTable table;
        private SshFileTableModel sshFileTableModel;

        PanelFiles() throws ConfigRepositoryException {
            setLayout(new BorderLayout());
            toolBar = new JToolBar();
            toolBarActionDownload = new JToolBar(JToolBar.VERTICAL);
            toolBarActionDownload.setFloatable(false);
            sshFileTableModel = new SshFileTableModel(configRepository);
            table = new JTable(sshFileTableModel);
            table.setDefaultRenderer(Object.class, new SshFileTableRenderer());
            table.setCellEditor(new DefaultCellEditor(new JTextField()));

            table.getColumn("Название").setMinWidth(150);
            table.getColumn("Название").setPreferredWidth(150);
            table.getColumn("Файл для скачивания").setMinWidth(150);
            table.getColumn("Файл для скачивания").setPreferredWidth(200);
            table.getColumn("Файл для сохранения").setMinWidth(150);
            table.getColumn("Файл для сохранения").setPreferredWidth(200);

            add(new JScrollPane(table), BorderLayout.CENTER);

            add(toolBar, BorderLayout.NORTH);
            add(toolBarActionDownload, BorderLayout.EAST);

            toolBar.add(actionReloadTable());
            toolBar.add(actionSaveTable());
            toolBar.add(actionAddRow());
            toolBar.add(actionRemoveRow());
            toolBar.add(actionCopyRow());

            toolBarActionDownload.add(actionDownload());
        }

        private Action actionAddRow() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Добавить файл");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Добавить файл");
                    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/layer--plus.png")));
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        sshFileTableModel.setValueAt(new SshDownloadFile(), sshFileTableModel.getRowCount(), 0);
                    });
                }
            };
        }

        private Action actionRemoveRow() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Удалить файл");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Удалить файл");
                    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/layer--minus.png")));
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            sshFileTableModel.setValueAt(null, selectedRow, 0);
                            if (sshFileTableModel.getRowCount() > 0) {
                                int newSelectedRow = selectedRow - 1;
                                if (newSelectedRow < 0) {
                                    table.getSelectionModel().setSelectionInterval(0, 0);
                                } else {
                                    table.getSelectionModel().setSelectionInterval(newSelectedRow, newSelectedRow);
                                }
                            }
                        }
                    });
                }
            };
        }

        private Action actionReloadTable() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Обновить данные");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Обновить данные");
                    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/arrow-circle.png")));
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        sshFileTableModel.reload();
                    });
                }
            };
        }

        private Action actionSaveTable() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Сохранить данные");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Сохранить данные");
                    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/disk-black.png")));
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        sshFileTableModel.save();
                    });
                }
            };
        }

        private Action actionCopyRow() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Дублировать выделенную строку");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Дублировать выделенную строку");
                    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/blue-document-copy.png")));
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
            };
        }

        private Action actionDownload() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Скачать выбранные файлы");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Скачать выбранные файлы");
                    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/inbox-download.png")));
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
            };
        }
    }

    private String stackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private class PanelFilesLog extends JPanel {
        private String format = "yyyy-MM-dd HH:mm:ss";
        private JTextPane textPaneLog;
        private JToolBar toolBar = new JToolBar();

        PanelFilesLog() {
            toolBar.add(actionClearLog());
            setLayout(new BorderLayout());
            textPaneLog = new JTextPane();
            textPaneLog.setEditable(false);
            textPaneLog.setBackground(Color.BLACK);
            textPaneLog.setForeground(Color.WHITE);
            textPaneLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            add(toolBar, BorderLayout.NORTH);
            add(new JScrollPane(textPaneLog), BorderLayout.CENTER);
        }

        void addToLog(String textAdd) {
            String date = new SimpleDateFormat(format).format(new Date());
            String text = textPaneLog.getText();
            if ("".equals(text)) {
                textPaneLog.setText(date + " " + textAdd);
            } else {
                textPaneLog.setText(date + " " + textAdd + "\n" + text);
            }
            textPaneLog.setCaretPosition(0);
        }

        void clearLog() {
            textPaneLog.setText("");
        }

        Action actionClearLog() {
            return new AbstractAction() {
                {
                    {
                        putValue(AbstractAction.SHORT_DESCRIPTION, "Очистить логи");
                        putValue(AbstractAction.LONG_DESCRIPTION, "Очистить логи");
                        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("/images/edit-clear.png")));
                    }
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> clearLog());
                }
            };
        }
    }
}
