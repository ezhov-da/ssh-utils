package ru.ezhov.ssh.utils.core;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

class DownloadFileSshAction implements SshAction {
    private String username;
    private String host;
    private int port;
    private String pathToPrivateKey;
    private String passphrase;
    private String fileFrom;
    private String fileTo;
    private DownloadProgressListener downloadProgressListener = null;

    DownloadFileSshAction(String username, String host, int port, String pathToPrivateKey, String passphrase, String fileFrom, String fileTo, DownloadProgressListener downloadProgressListener) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.pathToPrivateKey = pathToPrivateKey;
        this.passphrase = passphrase;
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
        this.downloadProgressListener = downloadProgressListener;
    }

    DownloadFileSshAction(String username, String host, int port, String pathToPrivateKey, String passphrase, String fileFrom, String fileTo) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.pathToPrivateKey = pathToPrivateKey;
        this.passphrase = passphrase;
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
    }

    public void perform() throws SshUtilException {
        Session session = null;
        Channel channel = null;
        try {
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
                        int read;
                        while ((read = inputStream.read(buf)) != -1) {
                            outputStream.write(buf, 0, read);
                            if (downloadProgressListener != null) {
                                downloadProgressListener.progress(read);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new SshUtilException(e);
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
