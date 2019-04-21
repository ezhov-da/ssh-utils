package ru.ezhov.ssh.utils.core;

public abstract class SshActionFactory {
    public static SshAction downloadFileAction(String username, String host, int port, String pathToPrivateKey, String passphrase, String fileFrom, String fileTo) {
        return new DownloadFileSshAction(username, host, port, pathToPrivateKey, passphrase, fileFrom, fileTo);
    }
}
