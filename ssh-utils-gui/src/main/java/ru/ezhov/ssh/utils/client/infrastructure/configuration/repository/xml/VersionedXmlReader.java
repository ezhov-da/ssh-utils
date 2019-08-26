package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml;

import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;

import java.util.List;

public interface VersionedXmlReader {
    List<SshDownloadFile> read() throws VersionedXmlReaderException;
}
