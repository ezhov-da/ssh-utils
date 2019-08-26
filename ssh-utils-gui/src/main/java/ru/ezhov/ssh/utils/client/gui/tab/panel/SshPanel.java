package ru.ezhov.ssh.utils.client.gui.tab.panel;

import ru.ezhov.ssh.utils.client.gui.tab.panel.model.SshFileTableModel;
import ru.ezhov.ssh.utils.client.gui.tab.panel.renderer.SshFileTableRenderer;
import ru.ezhov.ssh.utils.client.infrastructure.ApplicationProperties;
import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.ConfigRepositoryFactory;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepository;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepositoryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class SshPanel extends JPanel {

    private ConfigRepository configRepository;
    private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private PanelFiles panelFiles;
    private PanelFilesLog panelFilesLog;
    private String pathFile = ApplicationProperties.DEFAULT_XML_STORE;

    public SshPanel() throws ConfigRepositoryException {
        this.configRepository = ConfigRepositoryFactory.createFromFile(new File(pathFile));
        this.panelFilesLog = new PanelFilesLog();
        this.panelFiles = new PanelFiles();
        this.setLayout(new BorderLayout());

        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setTopComponent(panelFiles);
        this.splitPane.setBottomComponent(panelFilesLog);
        this.panelFilesLog.setVisible(false);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private class PanelFiles extends JPanel {
        private JToolBar toolBarTop;
        private JToolBar toolBarDown;
        private JToolBar toolBarActionDownload;
        private JTable table;
        private SshFileTableModel sshFileTableModel;

        PanelFiles() throws ConfigRepositoryException {
            this.setLayout(new BorderLayout());
            this.toolBarTop = new JToolBar();
            this.toolBarDown = new JToolBar();
            this.toolBarActionDownload = new JToolBar(JToolBar.VERTICAL);
            this.toolBarActionDownload.setFloatable(false);
            this.sshFileTableModel = new SshFileTableModel(configRepository);
            this.table = new JTable(sshFileTableModel);
            this.table.setDefaultRenderer(Object.class, new SshFileTableRenderer());
            this.table.setCellEditor(new DefaultCellEditor(new JTextField()));

            this.table.getColumn("Название").setMinWidth(150);
            this.table.getColumn("Название").setPreferredWidth(150);
            this.table.getColumn("Файл для скачивания").setMinWidth(150);
            this.table.getColumn("Файл для скачивания").setPreferredWidth(200);
            this.table.getColumn("Файл для сохранения").setMinWidth(150);
            this.table.getColumn("Файл для сохранения").setPreferredWidth(200);
            this.add(new JScrollPane(table), BorderLayout.CENTER);
            this.add(toolBarTop, BorderLayout.NORTH);
            this.add(toolBarActionDownload, BorderLayout.EAST);
            this.toolBarTop.add(actionReloadTable());
            this.toolBarTop.add(actionSaveTable());
            this.toolBarTop.add(actionAddRow());
            this.toolBarTop.add(actionRemoveRow());
            this.toolBarTop.add(actionCopyRow());
            this.toolBarTop.add(Box.createHorizontalGlue());

            JLabel labelPathToRepository = new JLabel("Конфигурация: " + pathFile);
            labelPathToRepository.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        labelPathToRepository.setForeground(Color.BLUE);
                        labelPathToRepository.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    });
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        labelPathToRepository.setForeground(Color.BLACK);
                        labelPathToRepository.setCursor(Cursor.getDefaultCursor());
                    });
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(new File(pathFile));
                        } catch (Exception e1) {
                            SwingUtilities.invokeLater(() -> {
                                panelFilesLog.addToLog("Не удалось открыть файл конфигурации.\n" + stackTrace(e1));
                            });
                        }
                    }
                }
            });
            this.toolBarDown.add(labelPathToRepository);
            this.add(toolBarDown, BorderLayout.SOUTH);


            JCheckBox checkBoxLog = new JCheckBox("Логи");
            checkBoxLog.addActionListener(a -> {
                SwingUtilities.invokeLater(() -> {
                    panelFilesLog.setVisible(checkBoxLog.isSelected());
                    splitPane.setDividerLocation(0.8);
                    splitPane.setResizeWeight(0.8);
                });
            });
            this.toolBarTop.add(checkBoxLog);

            this.toolBarActionDownload.add(actionDownload());
            this.toolBarActionDownload.add(actionDeleteFile());
            this.toolBarActionDownload.addSeparator();
            this.toolBarActionDownload.add(actionCopyPathToBuffer());
        }

        private Action actionAddRow() {
            return new ActionAddRow(sshFileTableModel);
        }

        private Action actionRemoveRow() {
            return new ActionRemoveRow(table, sshFileTableModel);
        }

        private Action actionReloadTable() {
            return new ActionReloadTable(sshFileTableModel);
        }

        private Action actionSaveTable() {
            return new ActionSaveTable(sshFileTableModel);
        }

        private Action actionCopyRow() {
            return new ActionCopyRow(table, sshFileTableModel);
        }

        private Action actionDownload() {
            return new ActionDownload(panelFilesLog, table, sshFileTableModel);
        }

        private Action actionDeleteFile() {
            return new ActionDeleteFile(sshFileTableModel, table, panelFilesLog);
        }

        private Action actionCopyPathToBuffer() {
            return new ActionCopyPathToBuffer(table, sshFileTableModel);
        }

        private String stackTrace(Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.toString();
        }
    }
}
