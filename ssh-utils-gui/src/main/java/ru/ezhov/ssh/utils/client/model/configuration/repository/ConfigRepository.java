package ru.ezhov.ssh.utils.client.model.configuration.repository;

import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;

import java.util.List;

public interface ConfigRepository {
    List<SshDownloadFile> all() throws ConfigRepositoryException;

    void add(SshDownloadFile sshDownloadFile) throws ConfigRepositoryException;

    void addAll(List<SshDownloadFile> sshDownloadFiles) throws ConfigRepositoryException;
}