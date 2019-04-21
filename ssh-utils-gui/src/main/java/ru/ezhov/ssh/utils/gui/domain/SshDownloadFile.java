package ru.ezhov.ssh.utils.gui.domain;

public class SshDownloadFile {
    private String description;
    private String host;
    private String port;
    private String username;
    private String pathToPrivateKey;
    private String passphrase;
    private String fileTo;
    private String fileFrom;

    public SshDownloadFile() {
    }

    public SshDownloadFile(
            String description,
            String host,
            String port,
            String username,
            String pathToPrivateKey,
            String passphrase,
            String fileFrom,
            String fileTo
    ) {
        this.description = description;
        this.host = host;
        this.port = port;
        this.username = username;
        this.pathToPrivateKey = pathToPrivateKey;
        this.passphrase = passphrase;
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPathToPrivateKey(String pathToPrivateKey) {
        this.pathToPrivateKey = pathToPrivateKey;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public void setFileTo(String fileTo) {
        this.fileTo = fileTo;
    }

    public void setFileFrom(String fileFrom) {
        this.fileFrom = fileFrom;
    }

    public String getDescription() {
        return description;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPathToPrivateKey() {
        return pathToPrivateKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getFileTo() {
        return fileTo;
    }

    public String getFileFrom() {
        return fileFrom;
    }
}
