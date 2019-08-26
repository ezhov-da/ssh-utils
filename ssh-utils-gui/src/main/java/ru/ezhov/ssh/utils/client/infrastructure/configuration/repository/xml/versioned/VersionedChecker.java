package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.versioned;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class VersionedChecker {
    SupportVersion check(File source) throws VersionedCheckerException {
        try {
            SupportVersion supportVersion = SupportVersion.UNSUPPOTED;
            if (isOldVersion(source)) {
                supportVersion = SupportVersion.OLD;
            } else if (isOneVersion(source)) {
                supportVersion = SupportVersion.ONE;
            }
            return supportVersion;
        } catch (IOException | XPathExpressionException e) {
            throw new VersionedCheckerException("Не удалось проверить версию XML файла конфигурации '" + (source == null ? null : source.getAbsolutePath()) + "'", e);
        }
    }

    private boolean isOldVersion(File source) throws IOException, XPathExpressionException {
        try (InputStream ins = new FileInputStream(source)) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate("//ssh-files-download-with-private-key", new InputSource(ins), XPathConstants.NODESET);
            return nodeList != null && nodeList.getLength() != 0;
        }
    }

    private boolean isOneVersion(File source) throws IOException, XPathExpressionException {
        try (InputStream ins = new FileInputStream(source)) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            String version = (String) xPath.evaluate("string(//ssh-utils/@version)", new InputSource(ins), XPathConstants.STRING);
            return "1".equals(version);
        }
    }
}
