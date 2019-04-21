package ru.ezhov.ssh.utils.gui.repositories;

import org.junit.Ignore;
import org.junit.Test;
import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class XmlConfigRepositoryIT {

    @Test
    public void allWithoutErrors() throws Exception {
        String path = System.getProperty("user.home") + File.separator + "ssh-files-download-with-private-key-store.xml";
        XmlConfigRepository xmlConfigRepository = new XmlConfigRepository(new File(path));

        List<SshDownloadFile> all = xmlConfigRepository.all();
        System.out.println(all);
    }

    @Test
    public void addWithoutErrors() throws Exception {
        String path = System.getProperty("user.home") + File.separator + "ssh-files-download-with-private-key-store.xml";
        XmlConfigRepository xmlConfigRepository = new XmlConfigRepository(new File(path));

        xmlConfigRepository.add(new SshDownloadFile(
                "description",
                "host",
                "22",
                "username",
                "pathToPrivateKey",
                "passphrase",
                "fileFrom",
                "fileTo"
        ));
    }

    @Test
    public void addAllWithoutErrors() throws Exception {
        String path = System.getProperty("user.home") + File.separator + "ssh-files-download-with-private-key-store.xml";
        XmlConfigRepository xmlConfigRepository = new XmlConfigRepository(new File(path));

        xmlConfigRepository.addAll(Collections.singletonList(new SshDownloadFile(
                "description",
                "host",
                "22",
                "username",
                "pathToPrivateKey",
                "passphrase",
                "fileFrom",
                "fileTo"
        )));
    }
}