package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepository;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepositoryException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class XmlConfigRepository implements ConfigRepository {
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
            xsw.writeStartElement("ssh-files-download-with-private-key");
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
            xsw.writeEndElement(); //ssh-files-download-with-private-key
            xsw.writeEndDocument();
        } catch (Exception e) {
            throw new ConfigRepositoryException(e);
        }
    }

    private List<SshDownloadFile> read(File source) throws ConfigRepositoryException {
        List<SshDownloadFile> sshDownloadFiles = new ArrayList<>();
        try (InputStream ins = new FileInputStream(source)) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate("//ssh-files-download-with-private-key/files/file", new InputSource(ins), XPathConstants.NODESET);
            int length = nodeList.getLength();
            for (int i = 0; i < length; i++) {
                Node item = nodeList.item(i);

                NodeList childNodes = item.getChildNodes();
                int childNodesLength = childNodes.getLength();
                String description = "";
                String host = "";
                String port = "";
                String username = "";
                String pathToPrivateKey = "";
                String passphrase = "";
                String fileFrom = "";
                String fileTo = "";
                for (int cn = 0; cn < childNodesLength; cn++) {
                    Node node = childNodes.item(cn);
                    String value = node.getTextContent().trim();
                    switch (node.getNodeName()) {
                        case "description":
                            description = value;
                            break;
                        case "host":
                            host = value;
                            break;
                        case "port":
                            port = value;
                            break;
                        case "username":
                            username = value;
                            break;
                        case "pathToPrivateKey":
                            pathToPrivateKey = value;
                            break;
                        case "passphrase":
                            passphrase = value;
                            break;
                        case "fileFrom":
                            fileFrom = value;
                            break;
                        case "fileTo":
                            fileTo = value;
                            break;
                    }
                }
                sshDownloadFiles.add(
                        new SshDownloadFile(
                                description,
                                host,
                                port,
                                username,
                                pathToPrivateKey,
                                passphrase,
                                fileFrom,
                                fileTo
                        )
                );
            }
            return sshDownloadFiles;
        } catch (Exception e) {
            throw new ConfigRepositoryException(e);
        }
    }
}
