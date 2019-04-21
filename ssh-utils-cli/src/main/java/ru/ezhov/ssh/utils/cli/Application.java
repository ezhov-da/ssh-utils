package ru.ezhov.ssh.utils.cli;

import ru.ezhov.ssh.utils.core.SshActionFactory;
import ru.ezhov.ssh.utils.core.SshUtilException;

public class Application {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Нет аргументов");
            return;
        }
        String arg = args[0];
        if ("-dfpk".equals(arg)) {
            String username = System.getProperty("df.username");
            String host = System.getProperty("df.host");
            int port = Integer.valueOf(System.getProperty("df.port"));
            String privateKey = System.getProperty("df.pathToPrivateKey");
            String passphrase = System.getProperty("df.passphrase");
            String fileFrom = System.getProperty("df.fileFrom");
            String fileTo = System.getProperty("df.fileTo");
            try {
                SshActionFactory
                        .downloadFileAction(username, host, port, privateKey, passphrase, fileFrom, fileTo)
                        .perform();
                System.out.println("Файл '" + fileFrom + "' скачан в '" + fileTo + "'");
            } catch (SshUtilException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Не поддерживаются другие аргументы");
        }
    }
}
