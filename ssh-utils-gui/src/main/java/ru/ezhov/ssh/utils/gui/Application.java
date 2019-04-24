package ru.ezhov.ssh.utils.gui;

import ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey.PanelSshPrivateKey;

import javax.swing.*;
import java.awt.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
        {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable ex) {
                //
            }

            try {
                JTabbedPane tabbedPane = new JTabbedPane();
                PanelSshPrivateKey panelSshPrivateKey = new PanelSshPrivateKey();

                tabbedPane.addTab("Скачивание файлов с серверов", panelSshPrivateKey);

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
