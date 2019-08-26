package ru.ezhov.ssh.utils.client.infrastructure;

import java.io.File;

public class ApplicationProperties {
    public static final String APPLICATION_WORK_DIRECTORY = System.getProperty("ssh-utils.application.work.directory", System.getProperty("user.home") + File.separator + ".ssh-utils");
    public static final String DEFAULT_XML_STORE = System.getProperty("ssh-utils.application.default.xml.store", APPLICATION_WORK_DIRECTORY + File.separator + "ssh-utils-store.xml");

    public static void init() {
        new File(APPLICATION_WORK_DIRECTORY).mkdirs();
    }
}
