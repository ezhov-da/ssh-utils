package ru.ezhov.ssh.utils.gui.repositories;

import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;

import java.util.List;

public interface ConfigRepository {
    List<SshDownloadFile> all() throws ConfigRepositoryException;

    void add(SshDownloadFile sshDownloadFile) throws ConfigRepositoryException;

    void addAll(List<SshDownloadFile> sshDownloadFiles) throws ConfigRepositoryException;
}