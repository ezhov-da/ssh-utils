package ru.ezhov.ssh.utils.client;

import ru.ezhov.ssh.utils.client.gui.tab.panel.SshPanel;
import ru.ezhov.ssh.utils.client.infrastructure.ApplicationProperties;

import javax.swing.*;
import java.awt.*;

public class Application {

    public static void main(String[] args) {

        ApplicationProperties.init();

        SwingUtilities.invokeLater(() ->
        {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable ex) {
                //
            }

            try {
                JTabbedPane tabbedPane = new JTabbedPane();
                SshPanel sshPanel = new SshPanel();

                tabbedPane.addTab("Скачивание файлов с серверов", sshPanel);

                JFrame frame = new JFrame("SSH Utils");

                frame.setIconImage(new ImageIcon(Application.class.getResource("/images/ssh_16x16.png")).getImage());

                frame.add(tabbedPane, BorderLayout.CENTER);
                frame.setSize(widthByPercentScreen(90), 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка запуска");
            }
        });
    }

    private static int widthByPercentScreen(int percent) {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        return width * percent / 100;
    }
}
