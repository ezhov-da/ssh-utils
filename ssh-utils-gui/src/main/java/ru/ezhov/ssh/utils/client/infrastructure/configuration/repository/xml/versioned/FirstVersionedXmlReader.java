package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.versioned;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.VersionedXmlReader;
import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.VersionedXmlReaderException;
import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FirstVersionedXmlReader implements VersionedXmlReader {
    private File file;

    public FirstVersionedXmlReader(File file) {
        this.file = file;
    }

    @Override
    public List<SshDownloadFile> read() throws VersionedXmlReaderException {
        List<SshDownloadFile> sshDownloadFiles = new ArrayList<>();
        try (InputStream ins = new FileInputStream(file)) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate("//ssh-utils/files/file", new InputSource(ins), XPathConstants.NODESET);
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
            throw new VersionedXmlReaderException("Ошибка чтения файла конфигурации '" + (file == null ? null : file.getAbsolutePath()) + "' для старой версии", e);
        }
    }
}
