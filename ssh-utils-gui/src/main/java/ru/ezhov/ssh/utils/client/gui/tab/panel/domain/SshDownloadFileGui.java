package ru.ezhov.ssh.utils.client.gui.tab.panel.domain;

import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;

import java.io.File;
import java.text.DecimalFormat;

public class SshDownloadFileGui {
    private FileStatus fileStatus;
    private long fileSize;

    private SshDownloadFile sshDownloadFile;

    public static SshDownloadFileGui from(SshDownloadFile sshDownloadFile) {
        SshDownloadFileGui sshDownloadFileGui = new SshDownloadFileGui();
        sshDownloadFileGui.sshDownloadFile = sshDownloadFile;

        sshDownloadFileGui.setDescription(sshDownloadFile.getDescription());
        sshDownloadFileGui.setHost(sshDownloadFile.getHost());
        sshDownloadFileGui.setPort(sshDownloadFile.getPort());
        sshDownloadFileGui.setUsername(sshDownloadFile.getUsername());
        sshDownloadFileGui.setPathToPrivateKey(sshDownloadFile.getPathToPrivateKey());
        sshDownloadFileGui.setPassphrase(sshDownloadFile.getPassphrase());
        sshDownloadFileGui.setFileFrom(sshDownloadFile.getFileFrom());
        sshDownloadFileGui.setFileTo(sshDownloadFile.getFileTo());

        sshDownloadFileGui.checkAndSetStatusExists();

        return sshDownloadFileGui;
    }

    public static SshDownloadFileGui from(SshDownloadFileGui sshDownloadFileGuiSource) {
        SshDownloadFileGui sshDownloadFileGui = new SshDownloadFileGui();

        sshDownloadFileGui.sshDownloadFile = sshDownloadFileGuiSource.getSshDownloadFile();

        sshDownloadFileGui.setFileStatus(sshDownloadFileGuiSource.getFileStatus());

        sshDownloadFileGui.setDescription(sshDownloadFileGuiSource.getDescription());
        sshDownloadFileGui.setHost(sshDownloadFileGuiSource.getHost());
        sshDownloadFileGui.setPort(sshDownloadFileGuiSource.getPort());
        sshDownloadFileGui.setUsername(sshDownloadFileGuiSource.getUsername());
        sshDownloadFileGui.setPathToPrivateKey(sshDownloadFileGuiSource.getPathToPrivateKey());
        sshDownloadFileGui.setPassphrase(sshDownloadFileGuiSource.getPassphrase());
        sshDownloadFileGui.setFileFrom(sshDownloadFileGuiSource.getFileFrom());
        sshDownloadFileGui.setFileTo(sshDownloadFileGuiSource.getFileTo());

        sshDownloadFileGui.checkAndSetStatusExists();

        return sshDownloadFileGui;
    }

    public String readableFileSize() {
        if (fileSize <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(fileSize / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void checkAndSetStatusExists() {
        fileSize = 0;
        String fileTo = sshDownloadFile.getFileTo();
        if (fileTo == null || "".equals(fileTo)) {
            fileStatus = FileStatus.EMPTY_VALUE;
        } else {
            File file = new File(fileTo);
            if (file.exists()) {
                fileStatus = FileStatus.EXISTS;
                fileSize = file.length();
            } else {
                fileStatus = FileStatus.NOT_EXISTS;
            }
        }
    }

    public void updateStatus() {
        checkAndSetStatusExists();
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public SshDownloadFile getSshDownloadFile() {
        return sshDownloadFile;
    }

    private SshDownloadFileGui() {
    }

    public void setDescription(String description) {
        sshDownloadFile.setDescription(description);
    }

    public void setHost(String host) {
        sshDownloadFile.setHost(host);
    }

    public void setPort(String port) {
        sshDownloadFile.setPort(port);
    }

    public void setUsername(String username) {
        sshDownloadFile.setUsername(username);
    }

    public void setPathToPrivateKey(String pathToPrivateKey) {
        sshDownloadFile.setPathToPrivateKey(pathToPrivateKey);
    }

    public void setPassphrase(String passphrase) {
        sshDownloadFile.setPassphrase(passphrase);
    }

    public void setFileTo(String fileTo) {
        sshDownloadFile.setFileTo(fileTo);
        checkAndSetStatusExists();
    }

    public void setFileFrom(String fileFrom) {
        sshDownloadFile.setFileFrom(fileFrom);
    }

    public String getDescription() {
        return sshDownloadFile.getDescription();
    }

    public String getHost() {
        return sshDownloadFile.getHost();
    }

    public String getPort() {
        return sshDownloadFile.getPort();
    }

    public String getUsername() {
        return sshDownloadFile.getUsername();
    }

    public String getPathToPrivateKey() {
        return sshDownloadFile.getPathToPrivateKey();
    }

    public String getPassphrase() {
        return sshDownloadFile.getPassphrase();
    }

    public String getFileTo() {
        return sshDownloadFile.getFileTo();
    }

    public String getFileFrom() {
        return sshDownloadFile.getFileFrom();
    }

}
