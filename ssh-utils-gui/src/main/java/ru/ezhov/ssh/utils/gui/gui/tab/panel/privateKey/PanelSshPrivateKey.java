package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.core.SshAction;
import ru.ezhov.ssh.utils.core.SshActionFactory;
import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepository;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepositoryException;

import javax.swing.*;
import java.awt.*;
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
        splitPane.setTopComponent(panelFiles);
        splitPane.setBottomComponent(panelFilesLog);

        add(splitPane, BorderLayout.CENTER);
    }

    private class PanelFiles extends JPanel {
        private JTable table;
        private JButton buttonAdd;
        private JButton buttonRemove;
        private JButton buttonSave;
        private JButton buttonReload;
        private JButton buttonExecute;
        private SshFileTableModel sshFileTableModel;

        public PanelFiles() throws ConfigRepositoryException {
            buttonAdd = new JButton("Добавить");
            buttonRemove = new JButton("Удалить");
            buttonReload = new JButton("Обновить список");
            buttonSave = new JButton("Сохранить список");
            buttonExecute = new JButton("Скачать");
            sshFileTableModel = new SshFileTableModel(configRepository);
            table = new JTable(sshFileTableModel);
            table.setDefaultRenderer(Object.class, new SshFileTableRenderer());
            table.setCellEditor(new DefaultCellEditor(new JTextField()));

            setLayout(new BorderLayout());
            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel panelButtons = new JPanel();
            panelButtons.add(buttonAdd);
            panelButtons.add(buttonRemove);
            panelButtons.add(buttonReload);
            panelButtons.add(buttonSave);
            panelButtons.add(buttonExecute);
            add(panelButtons, BorderLayout.SOUTH);

            buttonAdd.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    sshFileTableModel.setValueAt(new SshDownloadFile(), sshFileTableModel.getRowCount(), 0);
                });
            });

            buttonRemove.addActionListener(e -> {
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
            });

            buttonReload.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    sshFileTableModel.reload();
                });
            });

            buttonSave.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    sshFileTableModel.save();
                });
            });

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
