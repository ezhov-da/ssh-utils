package ru.ezhov.ssh.utils.client.gui.tab.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelFilesLog extends JPanel {
    private String format = "yyyy-MM-dd HH:mm:ss";
    private JTextPane textPaneLog;
    private JToolBar toolBar = new JToolBar();

    PanelFilesLog() {
        setBorder(BorderFactory.createTitledBorder("Логи"));

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