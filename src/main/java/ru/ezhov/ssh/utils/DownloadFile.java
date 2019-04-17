package ru.ezhov.ssh.utils;

import com.jcraft.jsch.*;

import java.io.*;

public class DownloadFile {
    private String username;
    private String host;
    private int port;
    private String pathToPrivateKey;
    private String passphrase;

    public DownloadFile(String username, String host, int port, String pathToPrivateKey, String passphrase) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.pathToPrivateKey = pathToPrivateKey;
        this.passphrase = passphrase;
    }

    void execute(String fileFrom, String fileTo) throws Exception {
        Session session = null;
        Channel channel = null;
        try {
            JSch jsch = new JSch();
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jsch.addIdentity(pathToPrivateKey, passphrase);
            session = jsch.getSession(username, host, port);
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp) channel;
            try (BufferedInputStream inputStream = new BufferedInputStream(c.get(fileFrom), 1024)) {
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(fileTo)))) {
                    byte[] buf = new byte[1024];
                    while (inputStream.read(buf) != -1) {
                        outputStream.write(buf);
                    }
                }
            }
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
