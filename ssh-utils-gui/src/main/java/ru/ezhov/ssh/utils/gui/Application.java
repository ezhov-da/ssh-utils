package ru.ezhov.ssh.utils.gui;

import ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey.PanelSshPrivateKey;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepository;
import ru.ezhov.ssh.utils.gui.repositories.XmlConfigRepository;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
                String path = System.getProperty("user.home") + File.separator + "ssh-files-download-with-private-key-store.xml";
                ConfigRepository configRepository = new XmlConfigRepository(new File(path));
                PanelSshPrivateKey panelSshPrivateKey = new PanelSshPrivateKey(configRepository);

                tabbedPane.addTab("Скачивание файлов с серверов", panelSshPrivateKey);

                JFrame frame = new JFrame("SSH Utils");

                frame.setIconImage(new ImageIcon(Application.class.getResource("/images/ssh_16x16.png")).getImage());

                frame.add(tabbedPane, BorderLayout.CENTER);
                frame.setSize(1000, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка запуска");
            }
        });
    }
}
