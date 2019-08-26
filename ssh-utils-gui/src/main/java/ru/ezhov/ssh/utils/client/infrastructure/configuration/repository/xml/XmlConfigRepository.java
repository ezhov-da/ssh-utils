package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml;

import ru.ezhov.ssh.utils.client.infrastructure.ApplicationProperties;
import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.versioned.VersionedCheckerException;
import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.versioned.VersionedReaderFactory;
import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepository;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepositoryException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlConfigRepository implements ConfigRepository {
    private File source;

    public XmlConfigRepository(File source) throws ConfigRepositoryException {
        this.source = source;
        if (!sourceExists(source)) {
            write(new ArrayList<>());
        }
    }

    public List<SshDownloadFile> all() throws ConfigRepositoryException {
        if (!sourceExists(source)) {
            write(new ArrayList<>());
        }
        return read(source);
    }

    public void add(SshDownloadFile sshDownloadFile) throws ConfigRepositoryException {
        List<SshDownloadFile> downloadFiles = all();
        downloadFiles.add(sshDownloadFile);
        write(downloadFiles);
    }

    public void addAll(List<SshDownloadFile> sshDownloadFiles) throws ConfigRepositoryException {
        write(sshDownloadFiles);
    }

    private boolean sourceExists(File source) {
        return source.exists();
    }

    private void write(List<SshDownloadFile> sshDownloadFiles) throws ConfigRepositoryException {
        try (OutputStream outputStream = new FileOutputStream(source)) {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream, "UTF-8");
            xsw.writeStartDocument("UTF-8", "1.0");
            xsw.writeStartElement("ssh-utils");
            xsw.writeAttribute("version", "1");
            xsw.writeStartElement("files");
            for (SshDownloadFile sshDownloadFile : sshDownloadFiles) {
                xsw.writeStartElement("file");

                xsw.writeStartElement("description");
                xsw.writeCharacters(sshDownloadFile.getDescription());
                xsw.writeEndElement(); //description

                xsw.writeStartElement("host");
                xsw.writeCharacters(sshDownloadFile.getHost());
                xsw.writeEndElement(); //host

                xsw.writeStartElement("port");
                xsw.writeCharacters(sshDownloadFile.getPort());
                xsw.writeEndElement(); //port

                xsw.writeStartElement("username");
                xsw.writeCharacters(sshDownloadFile.getUsername());
                xsw.writeEndElement(); //username

                xsw.writeStartElement("pathToPrivateKey");
                xsw.writeCharacters(sshDownloadFile.getPathToPrivateKey());
                xsw.writeEndElement(); //pathToPrivateKey

                xsw.writeStartElement("passphrase");
                xsw.writeCharacters(sshDownloadFile.getPassphrase());
                xsw.writeEndElement(); //passphrase

                xsw.writeStartElement("fileFrom");
                xsw.writeCharacters(sshDownloadFile.getFileFrom());
                xsw.writeEndElement(); //fileFrom

                xsw.writeStartElement("fileTo");
                xsw.writeCharacters(sshDownloadFile.getFileTo());
                xsw.writeEndElement(); //fileTo

                xsw.writeEndElement(); //file
            }
            xsw.writeEndElement(); //files
            xsw.writeEndElement(); //ssh-utils
            xsw.writeEndDocument();
        } catch (Exception e) {
            throw new ConfigRepositoryException(e);
        }
    }

    private List<SshDownloadFile> read(File source) throws ConfigRepositoryException {
        try {
            VersionedXmlReader versionedXmlReader = VersionedReaderFactory.byVersion(source);
            return versionedXmlReader.read();
        } catch (UnsupportedVersionedXmlReader e) {
            throw new ConfigRepositoryException("Неподдерживаемая версия конфигурации для файла '" + (source == null ? null : source.getAbsolutePath()) + "'", e);
        } catch (VersionedXmlReaderException e) {
            throw new ConfigRepositoryException("Не удалось прочесть конфигурацию определенной версии для файла '" + (source == null ? null : source.getAbsolutePath()) + "'", e);
        } catch (VersionedCheckerException e) {
            throw new ConfigRepositoryException("Не удалось определить версию конфигурации для файла '" + (source == null ? null : source.getAbsolutePath()) + "'", e);
        }
    }
}
