package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.core.SshAction;
import ru.ezhov.ssh.utils.core.SshActionFactory;
import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepository;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepositoryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        private JTable table;
        private JButton buttonExecute;
        private SshFileTableModel sshFileTableModel;

        PanelFiles() throws ConfigRepositoryException {
            setLayout(new BorderLayout());
            toolBar = new JToolBar();
            buttonExecute = new JButton("Скачать", new ImageIcon(getClass().getResource("/images/inbox-download.png")));
            sshFileTableModel = new SshFileTableModel(configRepository);
            table = new JTable(sshFileTableModel);
            table.setDefaultRenderer(Object.class, new SshFileTableRenderer());
            table.setCellEditor(new DefaultCellEditor(new JTextField()));

            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel panelButtons = new JPanel();
            panelButtons.add(buttonExecute);
            add(panelButtons, BorderLayout.SOUTH);

            add(toolBar, BorderLayout.NORTH);

            toolBar.add(actionReloadTable());
            toolBar.add(actionSaveTable());
            toolBar.add(actionAddRow());
            toolBar.add(actionRemoveRow());
            toolBar.add(actionCopyRow());

            buttonExecute.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    int selectedRow = table.getSelectedRow();
                    SshDownloadFile valueAt = sshFileTableModel.getBy(selectedRow);
                    panelFilesLog.addToLog("Скачивание файла: '" + valueAt.getFileFrom() + "' в '" + valueAt.getFileTo() + "' ");
                    try {
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
                        panelFilesLog.addToLog("Скачивание завершено");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        panelFilesLog.addToLog(stackTrace(e1));
                    }
                });
            });
        }

        private Action actionAddRow() {
            return new AbstractAction() {
                {
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Добавить строку");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Добавить строку");
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
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Удалить строку");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Удалить строку");
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
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Обновить таблицу");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Обновить таблицу");
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
                    putValue(AbstractAction.SHORT_DESCRIPTION, "Сохранить таблицу");
                    putValue(AbstractAction.LONG_DESCRIPTION, "Сохранить таблицу");
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
                            SshDownloadFile downloadFile = sshFileTableModel.getBy(selectedRow);
                            SshDownloadFile copyDownloadFile = new SshDownloadFile(
                                    downloadFile.getDescription(),
                                    downloadFile.getHost(),
                                    downloadFile.getPort(),
                                    downloadFile.getUsername(),
                                    downloadFile.getPathToPrivateKey(),
                                    downloadFile.getPassphrase(),
                                    downloadFile.getFileFrom(),
                                    downloadFile.getFileTo()
                            );
                            sshFileTableModel.setValueAt(copyDownloadFile, sshFileTableModel.getRowCount(), 0);
                            int last = sshFileTableModel.getRowCount() - 1;
                            table.getSelectionModel().setSelectionInterval(last, last);
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

        public PanelFilesLog() {
            setLayout(new BorderLayout());
            textPaneLog = new JTextPane();
            textPaneLog.setEditable(false);
            textPaneLog.setBackground(Color.BLACK);
            textPaneLog.setForeground(Color.WHITE);
            textPaneLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            add(new JScrollPane(textPaneLog), BorderLayout.CENTER);
        }

        public void addToLog(String textAdd) {
            String date = new SimpleDateFormat(format).format(new Date());
            String text = textPaneLog.getText();
            if ("".equals(text)) {
                textPaneLog.setText(date + " " + textAdd);
            } else {
                textPaneLog.setText(date + " " + textAdd + "\n" + text);
            }
            textPaneLog.setCaretPosition(0);
        }
    }
}
